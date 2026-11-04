package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.screens.BookingFlowScreen
import com.example.ui.screens.DigitalContractScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HouseRulesScreen
import com.example.ui.screens.KycOnboardingScreen
import com.example.ui.screens.MyBookingsScreen
import com.example.ui.screens.OwnerDashboardScreen
import com.example.ui.screens.PropertyDetailScreen
import com.example.ui.screens.ServicesAndUtilitiesScreen
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryTeal

@Composable
fun MainAppScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.screen.collectAsState()
    val activeRole by viewModel.activeRole.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val showBottomBar = currentScreen is Screen.Home ||
            currentScreen is Screen.Explore ||
            currentScreen is Screen.MyBookings ||
            currentScreen is Screen.ServicesAndUtilities ||
            currentScreen is Screen.OwnerDashboard

    Scaffold(
        topBar = {
            if (showBottomBar) {
                AppHeader(
                    userRole = activeRole,
                    walletBalance = currentUser?.walletBalance ?: 3500.0,
                    isKycVerified = currentUser?.kycStatus == "verified",
                    onRoleSwitch = { newRole ->
                        viewModel.switchRole(newRole)
                        if (newRole == "owner") {
                            viewModel.navigateTo(Screen.OwnerDashboard)
                        } else {
                            viewModel.navigateTo(Screen.Home)
                        }
                    },
                    onWalletClick = {
                        viewModel.navigateTo(Screen.ServicesAndUtilities)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.testTag("main_navigation_bar"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen is Screen.Home,
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "الرئيسية") },
                        label = { Text("الرئيسية", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryTeal,
                            selectedTextColor = PrimaryTeal,
                            indicatorColor = PrimaryTeal.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.Explore,
                        onClick = { viewModel.navigateTo(Screen.Explore) },
                        icon = { Icon(Icons.Default.Search, contentDescription = "استكشف") },
                        label = { Text("استكشف", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryTeal,
                            selectedTextColor = PrimaryTeal,
                            indicatorColor = PrimaryTeal.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_explore")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.MyBookings,
                        onClick = { viewModel.navigateTo(Screen.MyBookings) },
                        icon = { Icon(Icons.Default.EventNote, contentDescription = "حجوزاتي") },
                        label = { Text("حجوزاتي", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryTeal,
                            selectedTextColor = PrimaryTeal,
                            indicatorColor = PrimaryTeal.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_bookings")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.ServicesAndUtilities,
                        onClick = { viewModel.navigateTo(Screen.ServicesAndUtilities) },
                        icon = { Icon(Icons.Default.CleaningServices, contentDescription = "الخدمات") },
                        label = { Text("الخدمات", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryTeal,
                            selectedTextColor = PrimaryTeal,
                            indicatorColor = PrimaryTeal.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_services")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.OwnerDashboard,
                        onClick = {
                            viewModel.switchRole("owner")
                            viewModel.navigateTo(Screen.OwnerDashboard)
                        },
                        icon = { Icon(Icons.Default.Apartment, contentDescription = "المالك") },
                        label = { Text("المالك", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentGold,
                            selectedTextColor = PrimaryTeal,
                            indicatorColor = AccentGold.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("nav_item_owner")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Explore -> ExploreScreen(viewModel = viewModel)
                is Screen.PropertyDetail -> PropertyDetailScreen(propertyId = screen.propertyId, viewModel = viewModel)
                is Screen.BookingFlow -> BookingFlowScreen(propertyId = screen.propertyId, bedId = screen.bedId, viewModel = viewModel)
                is Screen.DigitalContract -> DigitalContractScreen(bookingId = screen.bookingId, viewModel = viewModel)
                is Screen.MyBookings -> MyBookingsScreen(viewModel = viewModel)
                is Screen.ServicesAndUtilities -> ServicesAndUtilitiesScreen(viewModel = viewModel)
                is Screen.KycOnboarding -> KycOnboardingScreen(viewModel = viewModel)
                is Screen.OwnerDashboard -> OwnerDashboardScreen(viewModel = viewModel)
                is Screen.HouseRules -> HouseRulesScreen(viewModel = viewModel)
            }
        }
    }
}
