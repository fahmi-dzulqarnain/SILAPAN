package com.midcores.silapan.presentation.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.midcores.silapan.presentation.ui.home.HomeTab
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanTab
import com.midcores.silapan.presentation.ui.profile.ProfileTab
import com.midcores.silapan.presentation.ui.theme.Surface

@Composable
fun AppBottomBar() {
    LocalTabNavigator.current
    val allTabs = listOf(HomeTab, PengaduanTab, ProfileTab)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        allTabs.forEach { tab ->
            TabItem(tab)
        }
    }
}

@Composable
private fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = (tabNavigator.current == tab),
        onClick = { tabNavigator.current = tab },
        icon = { Icon(tab.options.icon!!, tab.options.title) },
        label = { Text(tab.options.title) }
    )
}