package io.github.tuguzt.pcbuilder.view.components

import android.content.res.Configuration
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.view.collectAsStateLifecycleAware
import io.github.tuguzt.pcbuilder.view.navigation.ComponentScreenDestinations.ComponentDetails
import io.github.tuguzt.pcbuilder.view.navigation.ComponentScreenDestinations.ComponentList
import io.github.tuguzt.pcbuilder.view.theme.PCBuilderTheme
import io.github.tuguzt.pcbuilder.viewmodel.ComponentListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Application screen which represents *Components* main application destination.
 */
@Composable
fun ComponentsScreen(
    componentListViewModel: ComponentListViewModel = viewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
) {
    val components by componentListViewModel.components
        .collectAsStateLifecycleAware(scope.coroutineContext)
    val scaffoldState = rememberScaffoldState()
    var showAddComponentDialog by rememberSaveable { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = ComponentList.route) {
        composable(ComponentList.route) {
            Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(onClick = { showAddComponentDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add_component),
                        )
                    }
                }
            ) {
                ComponentList(
                    components = components,
                    onComponentClick = {
                        navController.navigate("${ComponentDetails.route}/${it.id}")
                    },
                )

                if (showAddComponentDialog) {
                    val componentAddedMessage = stringResource(R.string.component_added)
                    AddComponentDialog(
                        onDismissRequest = {
                            showAddComponentDialog = false
                        },
                        onAddComponent = { component ->
                            componentListViewModel += component
                            showAddComponentDialog = false
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(componentAddedMessage)
                            }
                        }
                    )
                }
            }
        }
        composable(
            route = "${ComponentDetails.route}/{componentId}",
            arguments = listOf(
                navArgument("componentId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("componentId")
            val component = checkNotNull(components.find { it.id == id })
            ComponentDetailsScreen(component)
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ComponentsScreenPreview() {
    PCBuilderTheme {
        ComponentsScreen()
    }
}
