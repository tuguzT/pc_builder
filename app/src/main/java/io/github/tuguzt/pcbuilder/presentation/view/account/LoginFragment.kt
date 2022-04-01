package io.github.tuguzt.pcbuilder.presentation.view.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import io.github.tuguzt.pcbuilder.databinding.FragmentLoginBinding
import io.github.tuguzt.pcbuilder.domain.interactor.checkPassword
import io.github.tuguzt.pcbuilder.domain.interactor.checkUsername
import io.github.tuguzt.pcbuilder.presentation.model.user.UserCredentialsData
import io.github.tuguzt.pcbuilder.presentation.model.user.toIntent
import io.github.tuguzt.pcbuilder.presentation.model.user.toUser
import io.github.tuguzt.pcbuilder.presentation.view.googleSignInOptions
import io.github.tuguzt.pcbuilder.presentation.view.snackbarShort
import io.github.tuguzt.pcbuilder.presentation.view.userSharedPreferences
import io.github.tuguzt.pcbuilder.presentation.viewmodel.account.AuthViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {
    companion object {
        @JvmStatic
        private val LOG_TAG = AuthActivity::class.simpleName
    }

    private val authViewModel: AuthViewModel by sharedViewModel()

    private var _binding: FragmentLoginBinding? = null

    // This helper property is only valid between `onCreateView` and `onDestroyView`.
    private inline val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentLoginBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        val contract = ActivityResultContracts.StartActivityForResult()
        val googleSignInLauncher = registerForActivityResult(contract) {
            if (it.resultCode != AppCompatActivity.RESULT_OK) {
                snackbarShort(binding.root) { "User was not signed in!" }.show()
                return@registerForActivityResult
            }
            lifecycleScope.launch {
                try {
                    val data = it.data
                    val googleAccount = GoogleSignIn.getSignedInAccountFromIntent(data).await()

                    val sharedPreferences = requireActivity().application.userSharedPreferences
                    sharedPreferences.edit {
                        putString("google_token", googleAccount.idToken)
                        putString("google_username", googleAccount.displayName)
                    }
                    val user = googleAccount.toUser()
                    with(requireActivity()) {
                        setResult(AppCompatActivity.RESULT_OK, user.toIntent())
                        finish()
                    }
                } catch (exception: ApiException) {
                    val message = "Google authorization failed"
                    Log.e(LOG_TAG, message, exception)
                    snackbarShort(binding.root) { message }.show()
                }
            }
        }

        binding.run {
            googleButton.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }

            register.setOnClickListener {
                val action = LoginFragmentDirections.actionRegisterFragment()
                findNavController().navigate(action)
            }

            @Suppress("NAME_SHADOWING")
            signIn.setOnClickListener {
                val username = username.text.toString()
                val password = password.text.toString()
                if (username.isNotBlank() && password.isNotBlank()) {
                    val username = username.trim()
                    val password = password.trim()
                    if (checkUsername(username) && checkPassword(password)) {
                        val credentials = UserCredentialsData(username, password)
                        lifecycleScope.launch {
                            authViewModel.auth(requireActivity().application, credentials)
                            with(requireActivity()) {
                                setResult(AppCompatActivity.RESULT_OK)
                                finish()
                            }
                        }
                        return@setOnClickListener
                    }
                    snackbarShort(root) { "Incorrect input for username/password!" }.show()
                    return@setOnClickListener
                }
                snackbarShort(root) { "Username/password are empty!" }.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
