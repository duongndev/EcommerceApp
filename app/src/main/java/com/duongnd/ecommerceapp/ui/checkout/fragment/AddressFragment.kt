package com.duongnd.ecommerceapp.ui.checkout.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.duongnd.ecommerceapp.adapter.AddressAdapter
import com.duongnd.ecommerceapp.data.api.RetrofitClient
import com.duongnd.ecommerceapp.data.model.address.AddressItem
import com.duongnd.ecommerceapp.data.repository.MyRepository
import com.duongnd.ecommerceapp.databinding.FragmentAddressBinding
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModel
import com.duongnd.ecommerceapp.viewmodel.checkout.CheckoutViewModelFactory

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private var addressItemList = ArrayList<AddressItem>()
    private lateinit var addressAdapter: AddressAdapter
    private val sessionManager = SessionManager()

    private val checkoutViewModel: CheckoutViewModel by viewModels {
        CheckoutViewModelFactory(MyRepository(apiService = RetrofitClient.apiService))
    }
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)

        sessionManager.SessionManager(requireContext())

        addressAdapter = AddressAdapter(addressItemList, requireContext())
        binding.recyclerViewAddress.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewAddress.setHasFixedSize(true)
        binding.recyclerViewAddress.adapter = addressAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = sessionManager.getToken()!!
        val userId = sessionManager.getUserId()!!
        val handler = Handler(Looper.getMainLooper())
        progressDialog.start("Loading Address...")
        handler.postDelayed({
            getAllAddresses(token, userId)
        }, 3000)

        binding.bttApplyAddress.setOnClickListener {

//            val addressSelected = addressAdapter.getSelectedItem()
//            if (addressSelected!= null) {
//                Toast.makeText(requireContext(), addressSelected.addressLine, Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "Please select address", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun getAllAddresses(token: String, userId: String) {
        checkoutViewModel.getAllAddresses(token, userId)
        checkoutViewModel._liveDataAddressItem.observe(viewLifecycleOwner, Observer {
            addressItemList.clear()
            addressItemList.addAll(it)
            addressAdapter.notifyDataSetChanged()
            progressDialog.stop()
        })
    }

}