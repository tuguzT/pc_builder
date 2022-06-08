package io.github.tuguzt.pcbuilder.presentation.view.root.main.builds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.tuguzt.pcbuilder.domain.model.build.BuildData
import io.github.tuguzt.pcbuilder.domain.model.component.data.CaseData
import io.github.tuguzt.pcbuilder.domain.model.component.data.MotherboardData
import io.github.tuguzt.pcbuilder.presentation.R
import io.github.tuguzt.pcbuilder.presentation.viewmodel.root.main.MainViewModel
import io.github.tuguzt.pcbuilder.presentation.viewmodel.root.main.builds.AddBuildViewModel
import io.github.tuguzt.pcbuilder.presentation.viewmodel.root.main.builds.isValid
import io.github.tuguzt.pcbuilder.presentation.viewmodel.root.main.builds.toData
import io.github.tuguzt.pcbuilder.presentation.viewmodel.root.main.components.ComponentsViewModel

@Composable
fun AddBuildScreen(
    onAdd: (BuildData) -> Unit,
    mainViewModel: MainViewModel,
    componentsViewModel: ComponentsViewModel,
    addBuildViewModel: AddBuildViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val title = stringResource(R.string.add_build)
    SideEffect {
        mainViewModel.updateTitle(title)
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = focusManager::clearFocus,
                ),
        ) {
            OutlinedTextField(
                value = addBuildViewModel.uiState.name,
                onValueChange = addBuildViewModel::updateName,
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))

            val cases = remember(componentsViewModel.uiState) {
                componentsViewModel.uiState.components.filterIsInstance<CaseData>()
            }
            ComponentExposedDropdown(
                items = cases,
                label = stringResource(R.string.case_label),
                currentItem = addBuildViewModel.uiState.case,
                onItemChoose = { addBuildViewModel.updateCase(it) },
            )
            Spacer(modifier = Modifier.height(8.dp))

            val motherboards = remember(componentsViewModel.uiState) {
                componentsViewModel.uiState.components.filterIsInstance<MotherboardData>()
            }
            ComponentExposedDropdown(
                items = motherboards,
                label = stringResource(R.string.motherboard),
                currentItem = addBuildViewModel.uiState.motherboard,
                onItemChoose = { addBuildViewModel.updateMotherboard(it) },
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .weight(1f),
            )
            Button(
                onClick = {
                    val build = addBuildViewModel.uiState.toData()
                    onAdd(build)
                },
                modifier = Modifier.align(Alignment.End),
                enabled = addBuildViewModel.uiState.isValid,
            ) {
                Text(stringResource(id = R.string.add_build))
            }
        }
    }
}
