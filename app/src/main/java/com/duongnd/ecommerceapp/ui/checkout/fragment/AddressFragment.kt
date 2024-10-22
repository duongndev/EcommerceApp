package com.duongnd.ecommerceapp.ui.checkout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duongnd.ecommerceapp.AppApplication
import com.duongnd.ecommerceapp.adapter.AddressAdapter
import com.duongnd.ecommerceapp.data.model.user.address.AddressItem
import com.duongnd.ecommerceapp.databinding.FragmentAddressBinding
import com.duongnd.ecommerceapp.ui.dialog.AddAddressFragment
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.utils.UiState
import com.duongnd.ecommerceapp.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private var addressItemList = ArrayList<AddressItem>()
    private lateinit var addressAdapter: AddressAdapter
    private val sessionManager = SessionManager()

//    private val userViewModel: UserViewModel by viewModels {
//        UserViewModelFactory(usersRepository = UsersRepository(ecommerceApiService = AppModule.provideApi()))
//    }

    private lateinit var userViewModel: UserViewModel

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddressBinding.inflate(inflater, container, false)

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

        userViewModel = (requireActivity().application as AppApplication).userViewModel


        progressDialog.start("Loading Address...")
        val token = sessionManager.getToken()!!
        showAddress(token)


        binding.bttApplyAddress.setOnClickListener {
            val addressSelected = addressAdapter.getSelectedItem()
            if (addressSelected != null) {
                Toast.makeText(requireContext(), "$addressSelected", Toast.LENGTH_SHORT).show()
                userViewModel.setSelectedAddress(addressSelected)
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please select address", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddAddress.setOnClickListener {
            // show dialog add address
            val dialog = AddAddressFragment()
            dialog.show(parentFragmentManager, "add_address")
        }
    }

    private fun showAddress(token: String) {
        lifecycleScope.launch {
            userViewModel.getAddressUser(token)
            userViewModel.addressUser.collectLatest { state ->
                when (state) {
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        progressDialog.stop()
                    }

                    UiState.Loading -> {
                        progressDialog.start("Loading Address...")
                    }

                    is UiState.Success -> {
                        addressItemList.clear()
                        addressItemList.addAll(state.data)
                        addressAdapter.notifyDataSetChanged()
                        progressDialog.stop()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}