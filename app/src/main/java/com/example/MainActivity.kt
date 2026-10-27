package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Schedule
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AdvocateViewModel
import com.example.ui.components.ChambersTopAppBar
import com.example.ui.dialogs.AddCaseDialog
import com.example.ui.dialogs.BookAppointmentDialog
import com.example.ui.dialogs.CaseDetailBottomSheet
import com.example.ui.dialogs.UpdateHearingDialog
import com.example.ui.screens.CaseDiaryScreen
import com.example.ui.screens.CauseListScreen
import com.example.ui.screens.ChambersScreen
import com.example.ui.screens.LegalToolsScreen
import com.example.ui.screens.StatusStudioScreen
import com.example.ui.theme.GoldBright
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AdvocateApp()
            }
        }
    }
}

@Composable
fun AdvocateApp(viewModel: AdvocateViewModel = viewModel()) {
    val context = LocalContext.current
    val selectedTab by viewModel.selectedTab.collectAsState()
    val showAddCase by viewModel.showAddCaseDialog.collectAsState()
    val showBookApp by viewModel.showBookAppointmentDialog.collectAsState()
    val updateHearingCase by viewModel.showUpdateHearingDialog.collectAsState()
    val selectedCaseForDetail by viewModel.selectedCaseForDetail.collectAsState()

    val navItems = listOf(
        NavigationItem("Chambers", Icons.Default.AccountBalance, Icons.Outlined.AccountBalance, "nav_chambers"),
        NavigationItem("Case Diary", Icons.Default.Gavel, Icons.Outlined.Gavel, "nav_cases"),
        NavigationItem("Cause List", Icons.Default.Schedule, Icons.Outlined.Schedule, "nav_cause_list"),
        NavigationItem("Legal Tools", Icons.AutoMirrored.Filled.Assignment, Icons.AutoMirrored.Outlined.Assignment, "nav_tools"),
        NavigationItem("Status Studio", Icons.Default.Campaign, Icons.Outlined.Campaign, "nav_status_studio")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChambersTopAppBar(
                title = viewModel.profile.name,
                subtitle = "Advocate High Court • Legal Chambers",
                onCallClick = { viewModel.dialPhone(context, viewModel.profile.phone) },
                onShareAppClick = {
                    val shareAppText = """
⚖️ *Rashid Mukhtiar Advocate - Mobile App*
Official Legal Practice & Case Diary App for Rashid Mukhtiar Advocate (High Court Bar Association).
Case Tracking, Cause List, Consultation Booking & Legal Reference.
Contact: ${viewModel.profile.phone}
                    """.trimIndent()
                    viewModel.shareText(context, shareAppText, "Rashid Mukhtiar Advocate App")
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NavyPrimary,
                contentColor = Color.White,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                contentDescription = item.label,
                                tint = if (isSelected) NavyPrimary else Color.LightGray
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) GoldBright else Color.LightGray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = GoldBright
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (selectedTab) {
                0 -> ChambersScreen(viewModel = viewModel)
                1 -> CaseDiaryScreen(viewModel = viewModel)
                2 -> CauseListScreen(viewModel = viewModel)
                3 -> LegalToolsScreen(viewModel = viewModel)
                4 -> StatusStudioScreen(viewModel = viewModel)
            }
        }

        // Add Case Modal
        if (showAddCase) {
            AddCaseDialog(
                onDismiss = { viewModel.setShowAddCaseDialog(false) },
                onConfirm = { newCase -> viewModel.addNewCase(newCase) }
            )
        }

        // Book Appointment Modal
        if (showBookApp) {
            BookAppointmentDialog(
                onDismiss = { viewModel.setShowBookAppointmentDialog(false) },
                onConfirm = { newApp -> viewModel.bookAppointment(newApp) }
            )
        }

        // Update Hearing Modal
        if (updateHearingCase != null) {
            UpdateHearingDialog(
                case = updateHearingCase!!,
                onDismiss = { viewModel.setShowUpdateHearingDialog(null) },
                onConfirm = { newDate, newStage, outcome ->
                    viewModel.updateHearing(updateHearingCase!!.id, newDate, newStage, outcome)
                }
            )
        }

        // Case Details Bottom Sheet
        if (selectedCaseForDetail != null) {
            CaseDetailBottomSheet(
                case = selectedCaseForDetail!!,
                onDismiss = { viewModel.selectCaseForDetail(null) },
                onUpdateHearingClick = {
                    val targetCase = selectedCaseForDetail!!
                    viewModel.selectCaseForDetail(null)
                    viewModel.setShowUpdateHearingDialog(targetCase)
                },
                onRecordFeeClick = { amount ->
                    viewModel.recordFeePayment(selectedCaseForDetail!!.id, amount)
                },
                onShareToWhatsApp = { text, phone ->
                    viewModel.shareToWhatsApp(context, text, phone)
                },
                onCallClient = { phone ->
                    viewModel.dialPhone(context, phone)
                }
            )
        }
    }
}

data class NavigationItem(
    val label: String,
    val filledIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val outlinedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val tag: String
)
