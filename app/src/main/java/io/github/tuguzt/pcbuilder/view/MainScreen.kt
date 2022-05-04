package io.github.tuguzt.pcbuilder.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.domain.interactor.randomNanoId
import io.github.tuguzt.pcbuilder.domain.model.user.UserRole
import io.github.tuguzt.pcbuilder.model.user.UserData
import io.github.tuguzt.pcbuilder.view.account.AccountScreen
import io.github.tuguzt.pcbuilder.view.builds.BuildsScreen
import io.github.tuguzt.pcbuilder.view.components.ComponentsScreen
import io.github.tuguzt.pcbuilder.view.navigation.MainScreenDestinations.*
import io.github.tuguzt.pcbuilder.view.theme.PCBuilderTheme

/**
 * Main screen of the PC Builder application.
 */
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val showSearch by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                showSearch = showSearch,
                onSearchClick = { /* TODO */ },
            )
        },
        bottomBar = {
            DestinationsBottomNavigation(
                navController = navController,
                destinations = listOf(Components, Builds, Account),
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Components.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Components.route) { ComponentsScreen() }
            composable(Builds.route) { BuildsScreen() }
            composable(Account.route) {
                // TODO get current user from the database
                val user = UserData(
                    id = randomNanoId(),
                    role = UserRole.Administrator,
                    username = "tuguzT",
                    email = "timurka.tugushev@gmail.com",
                    imageUri = "https://avatars.githubusercontent.com/u/56771526",
                )
                AccountScreen(user, onSignOut = { /* TODO */ })
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MainScreenPreview() {
    PCBuilderTheme { MainScreen() }
}

@Composable
private fun MainScreenTopAppBar(showSearch: Boolean, onSearchClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            if (showSearch) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.search_components),
                    )
                }
            }
        }
    )
}
