package com.duongnd.ecommerceapp.ui.checkout.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.CheckoutAdapter
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.data.repository.CheckoutRepository
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.databinding.FragmentCheckoutHomeBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModel
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import timber.log.Timber
import java.net.URISyntaxException

class CheckoutHomeFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutHomeBinding
    private var cartItemList = ArrayList<ItemCart>()
    private lateinit var checkoutAdapter: CheckoutAdapter
    private val sessionManager = SessionManager()

    private val checkoutViewModel: CheckoutViewModel by viewModels {
        CheckoutViewModelFactory(checkoutRepository = CheckoutRepository(ecommerceApiService = AppModule.provideApi()))
    }

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    val TAG = "CheckoutHomeFragment"
    private var paymentMethod = ""

    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mSocket = IO.socket("http://192.168.99.52:8080/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutHomeBinding.inflate(inflater, container, false)
        arguments.let {
            cartItemList = (it!!.getParcelableArrayList("items") ?: arrayListOf())
        }

        val total = cartItemList.sumOf {
            it.price * it.quantity
        }

        val formatPrice = total.toString().replace("\\D+".toRegex(), "")
            .toLong().toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ",")

        binding.txtTotalPriceOrder.text = "$formatPrice vnđ"

        Log.d(TAG, "onCreateView: $cartItemList")
        cartItemList = ArrayList(cartItemList)

        checkoutAdapter = CheckoutAdapter(cartItemList, requireContext())

        binding.recyclerViewItemsCheckout.setHasFixedSize(true)
        binding.recyclerViewItemsCheckout.adapter = checkoutAdapter

        sessionManager.SessionManager(requireContext())

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("newOrder", newOrder)
        mSocket.connect()
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = sessionManager.getToken()!!
        val userId = sessionManager.getUserId()!!

        layoutPayment()
        progressDialog.start("Loading Address...")
        getAddress(token, userId)

        binding.listViewItemsCheckoutPayment.setOnItemClickListener { _, _, position, _ ->
//            val showFullPayment = formatCardNumber(users[position])
//            AlertDialog.Builder(requireContext())
//                .setTitle("Full Card Number")
//                .setMessage(showFullPayment)
//                .setPositiveButton(android.R.string.ok, null)
//                .show()

            val paymentFragment = PaymentFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_checkout, paymentFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.txtChangeAddress.setOnClickListener {
            val addressFragment = AddressFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_checkout, addressFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.bttPlaceOrder.setOnClickListener {
            progressDialog.start("Placing Order...")
            val addressLine = binding.txtDeliveryAddress.text.toString()
            val phoneNumber = binding.txtDeliveryAddressPhone.text.toString()
            addOrder(token, OrderItemRequest(userId, paymentMethod, addressLine, phoneNumber))
        }

    }

    private fun getAddress(token: String, userId: String) {
        with(checkoutViewModel) {
            getALlAddresses(token, userId)
            addressItem.observe(viewLifecycleOwner) {
                Log.d(TAG, "onViewCreated: $it")
                if (it.isEmpty()) {
                    progressDialog.stop()
                    Toast.makeText(requireContext(), "No address found", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.stop()
                    it.forEach { address ->
                        binding.txtTitleHomeDeliveryAddress.text = address.title
                        binding.txtDeliveryAddress.text = address.addressLine
                        binding.txtDeliveryAddressPhone.text = address.phoneNumber
                    }
                }
            }
            errorMessage.observe(viewLifecycleOwner) {
                progressDialog.stop()
                Log.d(TAG, "observeIncrementQuantityError: $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            loading.observe(viewLifecycleOwner) {
                if (it) progressDialog.start("Loading...")
            }
        }
    }

    private fun addOrder(token: String, orderItemRequest: OrderItemRequest) {
        with(checkoutViewModel) {
            getOrder(token, orderItemRequest)

            orderItem.observe(viewLifecycleOwner) {
                progressDialog.stop()
                mSocket.emit("newOrder", it)
                // emit notification
            }
            errorMessage.observe(viewLifecycleOwner) {
                progressDialog.stop()
                Log.d(TAG, "observeIncrementQuantityError: $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            loading.observe(viewLifecycleOwner) {
                if (it) progressDialog.start("Placing Order...")
            }
        }
    }

    private fun layoutPayment() {
        binding.chipGroupPayment.addChip(requireContext(), "Card", R.drawable.ic_card)
        binding.chipGroupPayment.addChip(
            requireContext(),
            "Cash",
            R.drawable.cash
        )
        binding.chipGroupPayment.addChip(requireContext(), "ZaloPay", R.drawable.zalo_pay_logo1)
        binding.chipGroupPayment.addChip(requireContext(), "MoMo", R.drawable.ic_logo_momo)

        val firstChip: Chip = binding.chipGroupPayment.getChildAt(0) as Chip
        firstChip.isChecked = true

        if (firstChip.isChecked) {
            binding.listViewItemsCheckoutPayment.visibility = View.VISIBLE
            paymentMethod = firstChip.text.toString()
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
        binding.chipGroupPayment.setOnCheckedStateChangeListener { group, checkedIds ->
            for (id in checkedIds) {
                val chip = group.findViewById<Chip>(id)
                if (chip.id == firstChip.id) {
                    binding.listViewItemsCheckoutPayment.visibility = View.VISIBLE
                    paymentMethod = chip.text.toString()
//                    Toast.makeText(requireContext(), chip.text, Toast.LENGTH_SHORT).show()
                } else {
                    binding.listViewItemsCheckoutPayment.visibility = View.GONE
//                    Toast.makeText(requireContext(), chip.text, Toast.LENGTH_SHORT).show()
                    paymentMethod = chip.text.toString()
                }
            }
        }

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "1234567812345678"
        )

        val maskCardNumber = users.map {
            maskCardNumber(it)
        }

        arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, maskCardNumber)
        binding.listViewItemsCheckoutPayment.adapter = arrayAdapter
    }

    private fun maskCardNumber(cardNumber: String): String {
        val maskedNumber = "*".repeat(cardNumber.length - 4) + cardNumber.takeLast(4)
        return maskedNumber.chunked(4).joinToString(" ")
    }

    private fun formatCardNumber(cardNumber: String): String {
        return cardNumber.chunked(4).joinToString(" ")
    }

    private fun ChipGroup.addChip(context: Context, label: String, drawable: Int) {
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = true
            isChipIconVisible = true
            isCheckedIconVisible = false
            chipIcon = ContextCompat.getDrawable(context, drawable)
            addView(this)
            chipIconTint = ContextCompat.getColorStateList(context, R.color.black)
            chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.black)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    chipIconTint = ContextCompat.getColorStateList(context, R.color.white)
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
                    chipIconTint = ContextCompat.getColorStateList(context, R.color.black)
                }
            }
        }

    }

    private val onConnect = Emitter.Listener {
        Timber.d("onConnect: $it")
    }


    private val newOrder = Emitter.Listener {
        val data = it[0] as JSONObject
        Log.d(TAG, "newOrder: $data")
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off("newOrder", newOrder)
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
    }

}