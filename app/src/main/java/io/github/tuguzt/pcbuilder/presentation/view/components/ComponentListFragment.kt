package io.github.tuguzt.pcbuilder.presentation.view.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.databinding.FragmentComponentListBinding
import io.github.tuguzt.pcbuilder.domain.model.component.Component
import io.github.tuguzt.pcbuilder.presentation.view.components.adapters.ComponentListAdapter
import io.github.tuguzt.pcbuilder.presentation.view.decorations.MarginDecoration
import io.github.tuguzt.pcbuilder.presentation.view.snackbarShort
import io.github.tuguzt.pcbuilder.presentation.viewmodel.components.ComponentsSharedViewModel

/**
 * A [Fragment] subclass which represents list of [components][Component].
 *
 * @see Component
 */
class ComponentListFragment : Fragment() {
    private val sharedViewModel: ComponentsSharedViewModel by navGraphViewModels(R.id.main_nav_graph)

    private var _binding: FragmentComponentListBinding? = null

    // This helper property is only valid between
    // `onCreateView` and `onDestroyView`.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentComponentListBinding.inflate(inflater, container, false)

        val adapter = ComponentListAdapter()
        binding.list.adapter = adapter

        val spaceSize = resources.getDimensionPixelSize(R.dimen.list_item_margin)
        binding.list.addItemDecoration(MarginDecoration(spaceSize))

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val item = adapter.removeAt(position)

                snackbarShort { "Component was successfully deleted" }
                    .apply {
                        setAction("Undo") {
                            adapter.add(position, item)
                        }
                        addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                if (event != DISMISS_EVENT_ACTION) {
                                    sharedViewModel.deleteComponent(item)
                                }
                            }
                        })
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.list)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_component_add_fragment)
        }

        sharedViewModel.allComponents.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}