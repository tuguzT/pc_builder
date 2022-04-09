package io.github.tuguzt.pcbuilder.presentation.view.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.databinding.FragmentAccountListBinding
import io.github.tuguzt.pcbuilder.presentation.view.account.adapters.AccountListAdapter
import io.github.tuguzt.pcbuilder.presentation.view.decorations.MarginDecoration
import io.github.tuguzt.pcbuilder.presentation.view.showSnackbar
import io.github.tuguzt.pcbuilder.presentation.viewmodel.account.AccountListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class AccountListFragment : Fragment() {
    companion object {
        @JvmStatic
        private val LOG_TAG = AccountListFragment::class.simpleName
    }

    private val viewModel: AccountListViewModel by koinNavGraphViewModel(R.id.account_nav_graph)

    private var _binding: FragmentAccountListBinding? = null

    // This helper property is only valid between `onCreateView` and `onDestroyView`.
    private inline val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = AccountListAdapter()
        binding.list.adapter = adapter

        val spaceSize = resources.getDimensionPixelSize(R.dimen.list_item_margin)
        binding.list.addItemDecoration(MarginDecoration(spaceSize))

        lifecycleScope.launch {
            when (val result = viewModel.getAllUsers()) {
                is NetworkResponse.Success -> adapter.submitList(result.body)
                is NetworkResponse.ServerError -> {
                    Log.e(LOG_TAG, "Server error", result.error)
                    showSnackbar(binding.root, R.string.server_error)
                }
                is NetworkResponse.NetworkError -> {
                    Log.e(LOG_TAG, "Network error", result.error)
                    showSnackbar(binding.root, R.string.network_error)
                }
                is NetworkResponse.UnknownError -> {
                    Log.e(LOG_TAG, "Application error", result.error)
                    showSnackbar(binding.root, R.string.application_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
