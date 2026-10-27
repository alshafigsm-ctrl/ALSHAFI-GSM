package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Appointment
import com.example.model.LegalCase
import com.example.ui.AdvocateViewModel
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.RedUrgent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChambersScreen(
    viewModel: AdvocateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profile = viewModel.profile
    val cases = viewModel.cases.value
    val appointments = viewModel.appointments.value

    val todayHearings = cases.filter { it.nextHearingDate.contains("Today", ignoreCase = true) || it.nextHearingDate.contains("Tomorrow", ignoreCase = true) }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        // Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.advocate_hero_banner_1793136281983),
                    contentDescription = "Chambers Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    NavyPrimary.copy(alpha = 0.85f),
                                    NavyPrimary
                                )
                            )
                        )
                )

                // Chamber Brand Overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 120.dp, bottom = 12.dp, end = 16.dp)
                ) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = profile.designation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GoldBright,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = profile.barCouncilReg,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.LightGray,
                            fontSize = 10.sp
                        )
                    )
                }

                // Portrait Avatar overlapping
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp)
                        .offset(y = (20).dp)
                        .size(92.dp)
                        .clip(CircleShape)
                        .border(3.dp, GoldBright, CircleShape)
                        .background(NavySecondary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.advocate_portrait_1793136299943),
                        contentDescription = "Rashid Mukhtiar Advocate",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }

        // Quick Action Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.dialPhone(context, profile.phone) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_call_chamber"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldBright,
                        contentColor = NavyPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Chamber", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val welcomeMsg = "Assalam-o-Alaikum! I want to schedule a legal consultation with Rashid Mukhtiar Advocate."
                        viewModel.shareToWhatsApp(context, welcomeMsg, profile.whatsapp)
                    },
                    modifier = Modifier
                        .weight(1.1f)
                        .testTag("btn_whatsapp_chamber"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenSuccess,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp Consult", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.setShowBookAppointmentDialog(true) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_book_appointment"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Book Meeting", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Urgent Cause List Alert Banner (if any)
        if (todayHearings.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { viewModel.setSelectedTab(2) },
                    colors = CardDefaults.cardColors(containerColor = RedUrgent.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedUrgent.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RedUrgent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Urgent Cause List Alert (${todayHearings.size} Hearings)",
                                fontWeight = FontWeight.Bold,
                                color = RedUrgent,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${todayHearings.first().title} • ${todayHearings.first().court}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                        }
                        Text("View Board →", color = RedUrgent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Metric Statistics Grid
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricCard(
                    title = "Active Suits",
                    value = "${cases.size}",
                    subtitle = "High Court & Sessions",
                    icon = Icons.Default.Gavel,
                    iconTint = GoldBright,
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    title = "Success Rate",
                    value = profile.successRate,
                    subtitle = "480+ Cases Resolved",
                    icon = Icons.Default.CheckCircle,
                    iconTint = GreenSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMetricCard(
                    title = "Chambers Exp.",
                    value = "${profile.experienceYears}+ Yrs",
                    subtitle = "Senior High Court Bar",
                    icon = Icons.Default.AccountBalance,
                    iconTint = Color(0xFF60A5FA),
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    title = "Consultations",
                    value = "${appointments.size}",
                    subtitle = "Scheduled This Week",
                    icon = Icons.Default.CalendarToday,
                    iconTint = Color(0xFFA78BFA),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Practice Areas Chips
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Areas of Legal Practice",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    profile.practiceAreas.forEach { area ->
                        SuggestionChip(
                            onClick = { viewModel.setSelectedTab(1) },
                            label = { Text(area, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        // Chamber Details Card
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Chamber Office & Contact",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(profile.officeAddress, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${profile.phone} (Chambers / Appointments)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(profile.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Scheduled Appointments Section
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scheduled Client Consultations",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "+ Book New",
                    color = GoldBright,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { viewModel.setShowBookAppointmentDialog(true) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(appointments.take(3)) { app ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavySecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(app.clientName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${app.category} • ${app.consultationMode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${app.date} at ${app.time}", fontSize = 11.sp, color = GoldBright, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { viewModel.dialPhone(context, app.clientPhone) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavySecondary, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Call", fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
