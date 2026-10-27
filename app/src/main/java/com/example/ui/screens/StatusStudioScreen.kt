package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketingStatusOption
import com.example.ui.AdvocateViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatusStudioScreen(
    viewModel: AdvocateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val statusOptions by viewModel.statusOptions.collectAsState()

    var selectedStatusIndex by remember { mutableStateOf(0) }
    val activeStatus = statusOptions.getOrElse(selectedStatusIndex) { statusOptions.first() }

    // Custom Generator State
    var showCustomEditor by remember { mutableStateOf(false) }
    var customDeveloperName by remember { mutableStateOf("Al Shafi GSM / AppStudio") }
    var customPhone by remember { mutableStateOf("+92 300 0000000") }
    var customClientName by remember { mutableStateOf("Rashid Mukhtiar Advocate") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GoldBright),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "WhatsApp Marketing & Status Studio",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "High-converting promo copy for your app development business",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        // Live WhatsApp Story / Status Mockup Preview
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("whatsapp_preview_frame"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                border = androidx.compose.foundation.BorderStroke(2.dp, GoldBright.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    NavySecondary,
                                    NavyPrimary,
                                    Color(0xFF070B12)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    // Simulated WhatsApp Status top bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) { i ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (i == 0) GoldBright else Color.Gray.copy(alpha = 0.4f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status User Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GoldBright),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⚖️",
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "My Status",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Just now • WhatsApp Story",
                                color = Color.LightGray,
                                fontSize = 10.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GreenSuccess.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "LIVE PREVIEW",
                                color = GreenSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status Content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .border(1.dp, GoldBright.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = activeStatus.rawContent,
                            color = Color(0xFFF1F5F9),
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instant Share Action Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.shareToWhatsApp(context, activeStatus.rawContent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenSuccess,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("btn_share_active_whatsapp")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Post to WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.copyToClipboard(context, activeStatus.title, activeStatus.rawContent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldBright,
                                contentColor = NavyPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_copy_active_status")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Text", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.shareText(context, activeStatus.rawContent, "App Development Portfolio")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(0.6f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        }
                    }
                }
            }
        }

        // Section Title: Available Status Templates
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status Options (4 Presets)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (showCustomEditor) "Close Editor" else "+ Create Custom",
                    color = GoldBright,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { showCustomEditor = !showCustomEditor }
                )
            }
        }

        // Custom Status Editor (Expandable)
        if (showCustomEditor) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldBright)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Custom Marketing Post Generator",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        OutlinedTextField(
                            value = customClientName,
                            onValueChange = { customClientName = it },
                            label = { Text("Client Name / Business") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = customDeveloperName,
                            onValueChange = { customDeveloperName = it },
                            label = { Text("Your Dev Brand / Company") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = customPhone,
                            onValueChange = { customPhone = it },
                            label = { Text("WhatsApp Contact Number") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                val generatedContent = """
🚀 New Milestone Reached!
Custom Android Application successfully built for $customClientName! ⚖️📱
Looking to take your business, clinic, school or firm digital?
📲 Contact $customDeveloperName ($customPhone) for premium Android app development today!
                                """.trimIndent()
                                val newOption = MarketingStatusOption(
                                    id = UUID.randomUUID().toString(),
                                    title = "Custom Brand Status ($customClientName)",
                                    subtitle = "Generated for $customDeveloperName",
                                    category = "Custom",
                                    emoji = "⭐",
                                    tags = listOf("Custom", "Branded"),
                                    rawContent = generatedContent
                                )
                                viewModel.addCustomStatus(newOption)
                                selectedStatusIndex = 0
                                showCustomEditor = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Generate & Add to List", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // List of Status Cards (The exact options from the prompt!)
        items(statusOptions) { option ->
            val index = statusOptions.indexOf(option)
            val isSelected = selectedStatusIndex == index

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedStatusIndex = index }
                    .testTag("status_card_$index"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) NavySecondary else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, GoldBright) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(option.emoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = option.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldBright)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("SELECTED", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = NavyPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = option.subtitle,
                        fontSize = 11.sp,
                        color = if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Raw Content Display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) Color.Black.copy(alpha = 0.25f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(10.dp)
                    ) {
                        Text(
                            text = option.rawContent,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tags
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        option.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldBright.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(tag, fontSize = 10.sp, color = GoldBright, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.copyToClipboard(context, option.title, option.rawContent)
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Text", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.shareToWhatsApp(context, option.rawContent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share to WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Developer Showcase & App Studio Inquiry Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBright.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Smartphone, contentDescription = null, tint = GoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Need an App for Your Business?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Android App developed with Modern Jetpack Compose, Material 3, and lightning performance. Turn your business idea into a live mobile application!",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val msg = "Hello! I saw the Android App developed for Rashid Mukhtiar Advocate and want to get an app built for my business."
                            viewModel.shareToWhatsApp(context, msg)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_contact_developer")
                    ) {
                        Text("💬 Contact Developer / Order Your App", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
