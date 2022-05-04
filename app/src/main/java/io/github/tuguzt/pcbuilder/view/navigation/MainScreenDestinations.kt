package io.github.tuguzt.pcbuilder.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.github.tuguzt.pcbuilder.R
import io.github.tuguzt.pcbuilder.view.MainScreen

/**
 * Bottom navigation destinations of the application's [main screen][MainScreen].
 */
sealed class MainScreenDestinations(
    override val route: String,
    override val icon: ImageVector,
) : BottomNavigationDestination {

    object Components : MainScreenDestinations(
        route = "components",
        icon = Icons.Rounded.Memory,
    ) {
        override val description: String
            @Composable get() = stringResource(R.string.components)
    }

    object Builds : MainScreenDestinations(
        route = "builds",
        icon = Icons.Rounded.Computer,
    ) {
        override val description: String
            @Composable get() = stringResource(R.string.builds)
    }

    object Account : MainScreenDestinations(
        route = "account",
        icon = Icons.Rounded.AccountCircle,
    ) {
        override val description: String
            @Composable get() = stringResource(R.string.account)
    }
}
