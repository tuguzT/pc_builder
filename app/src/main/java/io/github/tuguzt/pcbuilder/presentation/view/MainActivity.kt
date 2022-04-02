package io.github.tuguzt.pcbuilder.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.databinding.ActivityMainBinding
import io.github.tuguzt.pcbuilder.presentation.view.account.AuthActivity
import io.github.tuguzt.pcbuilder.presentation.viewmodel.account.AccountViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Entry point of the application.
 */
class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        private val LOG_TAG = MainActivity::class.simpleName
    }

    private val accountViewModel: AccountViewModel by viewModel()

    private lateinit var _binding: ActivityMainBinding
    private inline val binding get() = _binding

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val navController: NavController
        get() {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
            return navHostFragment.navController
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.bottomNavigation.setupWithNavController(navController)

        val contract = ActivityResultContracts.StartActivityForResult()
        launcher = registerForActivityResult(contract) {
            if (it.resultCode == RESULT_CANCELED) {
                finish()
                return@registerForActivityResult
            }
            lifecycleScope.launch {
                accountViewModel.updateUserFromBackend(application)
            }
        }

        lifecycleScope.launch {
            handleUser()
        }
    }

    private suspend fun handleUser() {
        when (val result = accountViewModel.findUser(application)) {
            is NetworkResponse.Success -> return
            is NetworkResponse.ServerError -> {
                Log.e(LOG_TAG, "Server error", result.error)
                snackbarShort(binding.root) { getString(R.string.server_error) }.show()
            }
            is NetworkResponse.NetworkError -> {
                Log.e(LOG_TAG, "Network error", result.error)
                snackbarShort(binding.root) { getString(R.string.network_error) }.show()
            }
            is NetworkResponse.UnknownError -> {
                Log.e(LOG_TAG, "Application error", result.error)
                snackbarShort(binding.root) { getString(R.string.application_error) }.show()
            }
        }

        val loginIntent = Intent(this, AuthActivity::class.java)
        launcher.launch(loginIntent)
    }
}