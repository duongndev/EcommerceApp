package com.duongnd.ecommerceapp.ui.checkout.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.duongnd.ecommerceapp.AppApplication
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.adapter.CheckoutAdapter
import com.duongnd.ecommerceapp.adapter.CityAdapter
import com.duongnd.ecommerceapp.adapter.DistrictsAdapter
import com.duongnd.ecommerceapp.data.model.cart.ItemCart
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipCity
import com.duongnd.ecommerceapp.data.model.goship.DataGoShipDistricts
import com.duongnd.ecommerceapp.data.model.user.address.AddressItem
import com.duongnd.ecommerceapp.data.repository.CheckoutRepository
import com.duongnd.ecommerceapp.data.request.OrderItemRequest
import com.duongnd.ecommerceapp.data.request.goship.GoShipRequest
import com.duongnd.ecommerceapp.data.request.shipping.AddressToShip
import com.duongnd.ecommerceapp.data.request.shipping.ParcelShip
import com.duongnd.ecommerceapp.data.request.shipping.ShippingRequest
import com.duongnd.ecommerceapp.databinding.FragmentCheckoutHomeBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.utils.UiState
import com.duongnd.ecommerceapp.viewmodel.GoShipViewModel
import com.duongnd.ecommerceapp.viewmodel.UserViewModel
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModel
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.socket.client.Socket
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import taimoor.sultani.sweetalert2.Sweetalert
import java.text.NumberFormat
import java.util.Locale

class CheckoutHomeFragment : Fragment() {

    private var _binding: FragmentCheckoutHomeBinding? = null
    private val binding get() = _binding!!

    private var cartItemList = ArrayList<ItemCart>()
    private lateinit var checkoutAdapter: CheckoutAdapter
    private val sessionManager = SessionManager()
    private var addressUser: AddressItem? = null

    private val checkoutViewModel: CheckoutViewModel by viewModels {
        CheckoutViewModelFactory(checkoutRepository = CheckoutRepository(ecommerceApiService = AppModule.provideApi()))
    }


    private lateinit var userViewModel: UserViewModel

    private lateinit var goShipViewModel: GoShipViewModel

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private lateinit var cityAdapter: CityAdapter
    private var districtsAdapter: DistrictsAdapter? = null

    private var listCities = ArrayList<DataGoShipCity>()
    private var listDistricts = ArrayList<DataGoShipDistricts>()

    val TAG = "CheckoutHomeFragment"
    private var paymentMethod = ""

    private lateinit var mSocket: Socket

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCheckoutHomeBinding.inflate(inflater, container, false)
        arguments.let {
            cartItemList = (it!!.getParcelableArrayList("items") ?: arrayListOf())
        }

        goShipViewModel = (requireActivity().application as AppApplication).goShipViewModel

        userViewModel = (requireActivity().application as AppApplication).userViewModel

        val total = cartItemList.sumOf {
            it.price * it.quantity
        }

        val locale = Locale("vi", "VN")
        val numberFormat = NumberFormat.getInstance(locale)
        val formattedPrice = numberFormat.format(total)

        binding.txtTotalPriceOrder.text = "$formattedPrice vnđ"

        Log.d(TAG, "onCreateView: $cartItemList")
        cartItemList = ArrayList(cartItemList)

        checkoutAdapter = CheckoutAdapter(cartItemList, requireContext())

        binding.recyclerViewItemsCheckout.setHasFixedSize(true)
        binding.recyclerViewItemsCheckout.adapter = checkoutAdapter

        sessionManager.SessionManager(requireContext())
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = sessionManager.getToken()!!

        layoutPayment()
//        progressDialog.start("Loading Address...")
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
                .addToBackStack(null).commit()

        }

        binding.txtChangeAddress.setOnClickListener {
            val addressFragment = AddressFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_checkout, addressFragment)
                .addToBackStack(null).commit()
        }

        binding.bttPlaceOrder.setOnClickListener {
//            progressDialog.start("Placing Order...")
            val addressLine = binding.txtDeliveryAddress.text.toString()
            val phoneNumber = binding.txtDeliveryAddressPhone.text.toString()
//            addOrder(token, OrderItemRequest(userId, paymentMethod, addressLine, phoneNumber))
            val sweetAlertDialog = Sweetalert(requireContext(), Sweetalert.PROGRESS_TYPE)
            sweetAlertDialog.progressHelper?.setBarColor(Color.parseColor("#A5DC86"))
            sweetAlertDialog.setCancelable(false)
            sweetAlertDialog.titleText = "Placing Order..."
            sweetAlertDialog.show()
        }

        getAllCities()
        getAllCityDistrict()
        loadAddressUser(token)
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val city = cityAdapter.getItem(position)
                val cityId = city?.id.toString()
                goShipViewModel.getAllDistricts(cityId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        userViewModel.selectedData.observe(viewLifecycleOwner) { selectedData ->
            if (selectedData != null) {
                binding.txtDeliveryAddress.text = selectedData.street
                addressUser = selectedData
                val shippingRequest = ShippingRequest(
                    addressTo = AddressToShip(
                        district = selectedData.district,
                        city = selectedData.city
                    ),
                    parcel = ParcelShip(cod = 10, amount = 50000, weight = 1)
                )
                getAllShippingFee(shippingRequest)
            } else {
                binding.txtDeliveryAddress.text = "No address selected"

            }
        }

    }


    private fun loadAddressUser(token: String) {
        lifecycleScope.launch {
            userViewModel.getAddressUser(token)
            userViewModel.addressUser.collectLatest { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "loadAddressUser:  ${state.message}")
                        progressDialog.stop()
                    }

                    UiState.Loading -> {
                        progressDialog.start("Loading Address...")
                    }

                    is UiState.Success -> {
                        val address = state.data
                        addressUser = address.firstOrNull() ?: "No address" as AddressItem
                        address.forEach {
                            val shippingRequest = ShippingRequest(
                                addressTo = AddressToShip(district = it.district, city = it.city),
                                parcel = ParcelShip(cod = 10, amount = 50000, weight = 1)
                            )
                            getAllShippingFee(shippingRequest)
                            binding.txtDeliveryAddress.text = it.street
                        }

                        progressDialog.stop()

                    }
                }
            }
        }
    }

    private fun getAllCityDistrict() {
        lifecycleScope.launch {
            goShipViewModel.districts.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "getAllCityDistrict:  ${state.message}")
                    }

                    is UiState.Loading -> {
                        Log.d(TAG, "getAllCityDistrict:  Loading")
                    }

                    is UiState.Success -> {
                        listDistricts.clear()
                        listDistricts.addAll(state.data)
                        districtsAdapter = DistrictsAdapter(requireContext(), listDistricts)
                        binding.spinnerDistrict.adapter = districtsAdapter
                        districtsAdapter?.notifyDataSetChanged()
                        Log.d(TAG, "getAllCityDistrict:  ${state.data}")
                    }
                }
            }
        }
    }

    private fun getAllCities() {
        lifecycleScope.launch {
            goShipViewModel.citiesState.collectLatest { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "getAllCities:  ${state.message}")
                    }

                    UiState.Loading -> {
                        Log.d(TAG, "getAllCities:  Loading")
                    }

                    is UiState.Success -> {
                        listCities.clear()
                        listCities.addAll(state.data)
                        cityAdapter = CityAdapter(requireContext(), listCities)
                        binding.spinnerCity.adapter = cityAdapter
                        cityAdapter.notifyDataSetChanged()
                        Log.d(TAG, "getAllCities:  ${state.data}")
                    }
                }
            }
        }
    }

    private fun addOrder(token: String, orderItemRequest: OrderItemRequest) {
        with(checkoutViewModel) {
            getOrder(token, orderItemRequest)

            orderItem.observe(viewLifecycleOwner) {
                progressDialog.stop()
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
            requireContext(), "Cash", R.drawable.cash
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

    private fun getAllCarries(goShipRequest: GoShipRequest) {
        lifecycleScope.launch {
            goShipViewModel.getAllCarriers(goShipRequest)
            goShipViewModel.carriers.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "getAllCarries:  ${state.message}")
                    }

                    is UiState.Loading -> {
                        Log.d(TAG, "getAllCarries:  Loading")
                    }

                    is UiState.Success -> {
                        Log.d(TAG, "getAllCarries:  ${state.data}")
                    }
                }
            }
        }
    }

    private fun getAllShippingFee(shippingRequest: ShippingRequest) {
        lifecycleScope.launch {
            checkoutViewModel.getAllShippingFee(shippingRequest)
            checkoutViewModel.carriers.collectLatest { state ->
                when (state) {
                    is UiState.Error -> {
                        Log.d(TAG, "getAllCarries:  ${state.message}")
                    }

                    is UiState.Loading -> {
                        Log.d(TAG, "getAllCarries:  Loading")
                    }

                    is UiState.Success -> {
                        Log.d(TAG, "getAllCarries:  ${state.data}")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}