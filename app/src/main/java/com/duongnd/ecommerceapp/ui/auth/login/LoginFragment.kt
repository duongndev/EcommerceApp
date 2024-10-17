package com.duongnd.ecommerceapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.duongnd.ecommerceapp.R
import com.duongnd.ecommerceapp.data.model.login.LoginRequest
import com.duongnd.ecommerceapp.data.repository.AuthRepository
import com.duongnd.ecommerceapp.databinding.FragmentLoginBinding
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.ui.MainActivity
import com.duongnd.ecommerceapp.utils.CustomProgressDialog
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.view.auth.signup.SignupFragment
import com.duongnd.ecommerceapp.viewmodel.auth.login.LoginViewModel
import com.duongnd.ecommerceapp.viewmodel.auth.login.LoginViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val sessionManager = SessionManager()

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepository(ecommerceApiService = AppModule.provideApi()))
    }
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        sessionManager.SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_auth_activity, SignupFragment())
                .addToBackStack(SignupFragment::class.java.simpleName)
                .commit()
        }

        binding.inputLayoutEmailLogin.editText?.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.inputLayoutEmailLogin.error = "Email is required"
                binding.btnLogin.isEnabled = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                binding.inputLayoutEmailLogin.error = "Invalid email address"
                binding.btnLogin.isEnabled = false
            } else {
                binding.inputLayoutEmailLogin.error = null
                binding.btnLogin.isEnabled = true
            }
        }
        binding.inputLayoutPasswordLogin.editText?.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.inputLayoutPasswordLogin.error = "Password is required"
                binding.btnLogin.isEnabled = false
            } else if (it.toString().length < 6) {
                binding.inputLayoutPasswordLogin.error = "Password must be at least 6 characters"
                binding.btnLogin.isEnabled = false

            } else {
                binding.inputLayoutPasswordLogin.error = null
                binding.btnLogin.isEnabled = true
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString().trim()
            val password = binding.edtPasswordLogin.text.toString().trim()

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() && password.isEmpty()
            ) {
                binding.inputLayoutEmailLogin.error = "Email is required"
                binding.inputLayoutPasswordLogin.error = "Password is required"
                binding.btnLogin.isEnabled = false
            } else {
                binding.inputLayoutEmailLogin.error = null
                binding.inputLayoutPasswordLogin.error = null
                val loginRequest = LoginRequest(email, password)
                progressDialog.start("Loading...")
                Handler(Looper.getMainLooper()).postDelayed({
                    getLogin(loginRequest)
                }, 3000)

            }
        }
    }

    private fun getLogin(loginRequest: LoginRequest) {
        with(loginViewModel){
            loginUser(loginRequest)
            dataLogin.observe(viewLifecycleOwner){

                    binding.edtEmailLogin.setText("")
                    binding.edtPasswordLogin.setText("")
                    binding.inputLayoutEmailLogin.error = null
                    binding.inputLayoutPasswordLogin.error = null
                    sessionManager.setToken(it.token)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                    progressDialog.stop()
            }
            loading.observe(viewLifecycleOwner){
                progressDialog.stop()
                if(it) progressDialog.start("Loading...")
            }
            errorMessage.observe(viewLifecycleOwner){
                progressDialog.stop()
                Toast.makeText(context, "Wrong email or password", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "getLogin: $it")
            }

        }
    }

}