package com.midcores.silapan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.midcores.silapan.api.ConnectivityObserver
import com.midcores.silapan.presentation.ui.component.NetworkStatusBanner
import com.midcores.silapan.presentation.ui.home.HomeTab
import com.midcores.silapan.presentation.ui.login.LoginScreen
import com.midcores.silapan.presentation.ui.pengaduan.PengaduanTab
import com.midcores.silapan.presentation.ui.profile.ProfileTab
import com.midcores.silapan.presentation.ui.theme.SilapanTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    SilapanTheme {
        val connectivityObserver: ConnectivityObserver = koinInject()
        val settingsRepository: SettingsRepository = koinInject()
        val isOnline by connectivityObserver.isNetworkAvailable.collectAsState()
        val authToken by settingsRepository.authTokenFlow.collectAsState()
        val isLoggedOut = authToken.isEmpty()
        val allTabs = listOf(HomeTab, PengaduanTab, ProfileTab)

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoggedOut) {
                LoginScreen { }
            } else {
                TabNavigator(HomeTab) { tabNavigator ->
                    val pagerState = rememberPagerState(pageCount = { allTabs.size })

                    LaunchedEffect(tabNavigator.current) {
                        val selectedIndex = allTabs.indexOf(tabNavigator.current)

                        if (pagerState.currentPage != selectedIndex) {
                            pagerState.animateScrollToPage(selectedIndex)
                        }
                    }

                    LaunchedEffect(
                        pagerState.currentPage,
                        pagerState.isScrollInProgress
                    ) {
                        if (!pagerState.isScrollInProgress) {
                            tabNavigator.current = allTabs[pagerState.currentPage]
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = true
                    ) { pageIndex ->
                        val saveableStateHolder = rememberSaveableStateHolder()
                        val tab = allTabs[pageIndex]

                        saveableStateHolder.SaveableStateProvider(key = tab.key) {
                            tab.Content()
                        }
                    }
                }
            }

            NetworkStatusBanner(
                isOnline = isOnline,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}
