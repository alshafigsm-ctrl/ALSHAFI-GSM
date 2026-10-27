package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CasePriority
import com.example.model.LegalCase
import com.example.ui.AdvocateViewModel
import com.example.ui.components.CourtBadge
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.RedUrgent

@Composable
fun CauseListScreen(
    viewModel: AdvocateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allCases by viewModel.cases.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Today", "Tomorrow", "This Week", "All Scheduled")

    val filteredList = remember(selectedTab, allCases) {
        when (selectedTab) {
            0 -> allCases.filter { it.nextHearingDate.contains("Today", ignoreCase = true) }
            1 -> allCases.filter { it.nextHearingDate.contains("Tomorrow", ignoreCase = true) }
            2 -> allCases.filter {
                it.nextHearingDate.contains("Today", ignoreCase = true) ||
                it.nextHearingDate.contains("Tomorrow", ignoreCase = true) ||
                it.nextHearingDate.contains("Nov", ignoreCase = true) ||
                it.nextHearingDate.contains("Oct", ignoreCase = true)
            }
            else -> allCases
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = NavyPrimary,
            contentColor = GoldBright
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) GoldBright else Color.LightGray
                        )
                    }
                )
            }
        }

        // Cause List Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Daily Cause List / Roznamcha",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${filteredList.size} Hearings scheduled in ${tabTitles[selectedTab]}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Share Cause List Summary
            OutlinedButton(
                onClick = {
                    val summary = buildString {
                        append("⚖️ *CAUSE LIST - RASHID MUKHTIAR ADVOCATE*\n")
                        append("Date: ${tabTitles[selectedTab]}\n\n")
                        filteredList.forEachIndexed { i, c ->
                            append("${i + 1}. ${c.title}\n")
                            append("   Suit No: ${c.caseNumber}\n")
                            append("   Court: ${c.court} (${c.courtroomNumber})\n")
                            append("   Stage: ${c.stage}\n")
                            append("   Client: ${c.clientName} (${c.clientPhone})\n\n")
                        }
                        append("Chamber #18, High Court Bar Association")
                    }
                    viewModel.shareText(context, summary, "Daily Cause List")
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("btn_share_cause_list")
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share Board", fontSize = 11.sp)
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GreenSuccess,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No hearings scheduled for ${tabTitles[selectedTab]}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Check the 'This Week' or 'All Scheduled' tab for upcoming court dates.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { c ->
                    CauseListCard(
                        case = c,
                        onUpdateHearing = { viewModel.setShowUpdateHearingDialog(c) },
                        onNotifyClient = {
                            val msg = """
⚖️ *Daily Cause List Notice*
Assalam-o-Alaikum ${c.clientName},
Your case *${c.title}* (${c.caseNumber}) is on board today at:
Court: ${c.court} (${c.courtroomNumber})
Stage: ${c.stage}
Counsels: Rashid Mukhtiar Advocate
                            """.trimIndent()
                            viewModel.shareToWhatsApp(context, msg, c.clientPhone)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CauseListCard(
    case: LegalCase,
    onUpdateHearing: () -> Unit,
    onNotifyClient: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NavySecondary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = case.courtroomNumber,
                            color = GoldBright,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    CourtBadge(courtName = case.court)
                }
                PriorityBadge(priority = case.priority)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = case.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )

            Text(
                text = "${case.caseNumber} • ${case.bench}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Hearing details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Date / Time", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(case.nextHearingDate, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = RedUrgent)
                }
                Column {
                    Text("Current Stage", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(case.stage, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
                Column {
                    Text("Client", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(case.clientName, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            if (case.opposingCounsel.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Opposing Counsel: ${case.opposingCounsel}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onNotifyClient,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp Client", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onUpdateHearing,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adjourn / Outcome", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
