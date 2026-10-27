package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun CaseDiaryScreen(
    viewModel: AdvocateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val courtFilter by viewModel.courtFilter.collectAsState()
    val cases by viewModel.filteredCases.collectAsState()

    val filterOptions = listOf("All", "High Court", "Sessions", "Civil", "Family", "Urgent")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowAddCaseDialog(true) },
                containerColor = GoldBright,
                contentColor = NavyPrimary,
                modifier = Modifier.testTag("fab_add_case")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Case")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("search_cases_input"),
                placeholder = { Text("Search case, client, suit number, court...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = GoldBright)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Filter Chips Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { opt ->
                    val isSelected = courtFilter == opt
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCourtFilter(opt) },
                        label = { Text(opt, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavySecondary,
                            selectedLabelColor = GoldBright
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Cases Count Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${cases.size} Active Cases",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "Sorted by Hearing Date",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = GoldBright
                    )
                )
            }

            // Case List
            if (cases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No cases found",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Try another search keyword or tap '+' to create a new case.",
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
                        top = 6.dp,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cases, key = { it.id }) { c ->
                        CaseCardItem(
                            case = c,
                            onClick = { viewModel.selectCaseForDetail(c) },
                            onUpdateHearing = { viewModel.setShowUpdateHearingDialog(c) },
                            onWhatsAppNotify = {
                                val msg = """
⚖️ *Hearing Reminder: Rashid Mukhtiar Law Chambers*
Case: ${c.title}
Suit/Writ: ${c.caseNumber}
Court: ${c.court} (${c.courtroomNumber})
Date of Hearing: *${c.nextHearingDate}*
Stage: ${c.stage}
Please bring all relevant record.
                                """.trimIndent()
                                viewModel.shareToWhatsApp(context, msg, c.clientPhone)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CaseCardItem(
    case: LegalCase,
    onClick: () -> Unit,
    onUpdateHearing: () -> Unit,
    onWhatsAppNotify: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("case_card_${case.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CourtBadge(courtName = case.court)
                PriorityBadge(priority = case.priority)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = case.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2
            )

            Text(
                text = "${case.caseNumber} • ${case.bench}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GoldBright,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Next Hearing Banner inside card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = RedUrgent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Next Hearing: ${case.nextHearingDate}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = RedUrgent
                        )
                    }
                    Text(
                        text = case.courtroomNumber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stage & Client
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Stage: ${case.stage}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Client: ${case.clientName}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fee Mini-bar
            val feePct = if (case.feeAgreed > 0) (case.feePaid.toFloat() / case.feeAgreed.toFloat()).coerceIn(0f, 1f) else 1f
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fee Paid: Rs. %,d / %,d".format(case.feePaid, case.feeAgreed),
                    fontSize = 11.sp,
                    color = GreenSuccess,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${(feePct * 100).toInt()}%",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { feePct },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = GreenSuccess,
                trackColor = Color.LightGray.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Card Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onUpdateHearing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adjourn / Next Date", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onWhatsAppNotify,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavySecondary,
                        contentColor = GoldBright
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Notify Client", fontSize = 11.sp)
                }
            }
        }
    }
}
