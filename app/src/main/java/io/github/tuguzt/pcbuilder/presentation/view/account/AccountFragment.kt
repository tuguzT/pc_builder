package io.github.tuguzt.pcbuilder.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.databinding.FragmentAccountBinding
import io.github.tuguzt.pcbuilder.presentation.viewmodel.account.AccountViewModel

/**
 * A [Fragment] subclass which represents information about user account.
 */
class AccountFragment : Fragment() {
    private val viewModel: AccountViewModel by navGraphViewModels(R.id.main_nav_graph)

    private var _binding: FragmentAccountBinding? = null

    // This helper property is only valid between
    // `onCreateView` and `onDestroyView`.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}