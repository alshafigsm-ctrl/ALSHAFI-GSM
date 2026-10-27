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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LegalDraft
import com.example.model.LimitationRule
import com.example.ui.AdvocateViewModel
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary

@Composable
fun LegalToolsScreen(
    viewModel: AdvocateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedToolTab by remember { mutableStateOf(0) }
    val toolTabs = listOf("Court Fee", "Limitation", "Legal Drafts", "Fee Receipt")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedToolTab,
            containerColor = NavyPrimary,
            contentColor = GoldBright
        ) {
            toolTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedToolTab == index,
                    onClick = { selectedToolTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedToolTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedToolTab == index) GoldBright else Color.LightGray
                        )
                    }
                )
            }
        }

        when (selectedToolTab) {
            0 -> CourtFeeCalculatorView(viewModel)
            1 -> LimitationPeriodView(viewModel)
            2 -> LegalDraftsView(viewModel)
            3 -> FeeReceiptGeneratorView(viewModel)
        }
    }
}

@Composable
fun CourtFeeCalculatorView(viewModel: AdvocateViewModel) {
    val context = LocalContext.current
    var valuationText by remember { mutableStateOf("250000") }
    var suitType by remember { mutableStateOf("Suit for Declaration & Injunction") }

    val valuation = valuationText.toDoubleOrNull() ?: 0.0
    val resultText = viewModel.calculateCourtFee(valuation)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Calculate, contentDescription = null, tint = GoldBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Court Fee Schedule Calculator", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Text(
                    text = "As per Court Fees Act 1870 (Punjab / High Court Amendment). Up to Rs. 25,000 valuation is exempt; otherwise 7.5% ad valorem capped at maximum Rs. 15,000.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = suitType,
                    onValueChange = { suitType = it },
                    label = { Text("Nature of Suit / Matter") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = valuationText,
                    onValueChange = { valuationText = it },
                    label = { Text("Suit Valuation in PKR") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("input_suit_valuation")
                )

                // Quick preset buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("25000", "100000", "500000", "2000000").forEach { preset ->
                        OutlinedButton(
                            onClick = { valuationText = preset },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Rs. $preset", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Calculation Result Card
        Card(
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Calculated Court Fee Payable", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = resultText,
                    color = GoldBright,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Note: In addition to ad valorem court fee, process fee (Talbana) of Rs. 50-100 and Court Stamp on Vakalatnama (Rs. 100) are affixed at institution.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        val text = "Court Fee Calculation for $suitType (Valuation Rs. $valuationText): $resultText"
                        viewModel.copyToClipboard(context, "Court Fee", text)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Calculation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LimitationPeriodView(viewModel: AdvocateViewModel) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    val rules = viewModel.limitationRules.filter {
        query.isBlank() || it.actionType.contains(query, ignoreCase = true) || it.court.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Filter limitation periods (appeal, review, revision)...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(rules) { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(r.actionType, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NavySecondary)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(r.duration, color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Forum: ${r.court}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Provision: ${r.provision}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(r.notes, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            OutlinedButton(
                                onClick = {
                                    val text = "${r.actionType}\nCourt: ${r.court}\nLimitation Duration: ${r.duration}\nLaw: ${r.provision}\n${r.notes}"
                                    viewModel.copyToClipboard(context, "Limitation Rule", text)
                                },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Copy Rule", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegalDraftsView(viewModel: AdvocateViewModel) {
    val context = LocalContext.current
    val drafts = viewModel.legalDrafts
    var selectedDraft by remember { mutableStateOf<LegalDraft?>(null) }

    if (selectedDraft != null) {
        val d = selectedDraft!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { selectedDraft = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("← Back to Drafts")
                }
                Row {
                    Button(
                        onClick = { viewModel.copyToClipboard(context, d.title, d.content) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = { viewModel.shareText(context, d.content, d.title) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavySecondary, contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(d.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(d.category, fontSize = 12.sp, color = GoldBright)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = d.content,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(drafts) { draft ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedDraft = draft },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = draft.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldBright.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(draft.category, fontSize = 10.sp, color = GoldBright, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = draft.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Tap to view & edit draft", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            OutlinedButton(
                                onClick = { viewModel.copyToClipboard(context, draft.title, draft.content) },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Copy Text", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeeReceiptGeneratorView(viewModel: AdvocateViewModel) {
    val context = LocalContext.current
    var clientName by remember { mutableStateOf("Malik Jahangir") }
    var suitNumber by remember { mutableStateOf("W.P. No. 18492/2025") }
    var court by remember { mutableStateOf("Lahore High Court") }
    var amountReceived by remember { mutableStateOf("50000") }
    var paymentMode by remember { mutableStateOf("Bank Transfer / Cash") }
    var onAccountOf by remember { mutableStateOf("Vakalatnama & High Court Arguments Fee") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Generate Client Fee Voucher & Receipt", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text("Create instant digital payment receipt with Chambers seal and send to client via WhatsApp.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        OutlinedTextField(
            value = clientName,
            onValueChange = { clientName = it },
            label = { Text("Client Name") },
            modifier = Modifier.fillMaxWidth().testTag("input_receipt_client")
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = suitNumber,
                onValueChange = { suitNumber = it },
                label = { Text("Case / Suit No.") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = court,
                onValueChange = { court = it },
                label = { Text("Court") },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = amountReceived,
                onValueChange = { amountReceived = it },
                label = { Text("Amount Received (PKR)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).testTag("input_receipt_amount")
            )
            OutlinedTextField(
                value = paymentMode,
                onValueChange = { paymentMode = it },
                label = { Text("Payment Mode") },
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedTextField(
            value = onAccountOf,
            onValueChange = { onAccountOf = it },
            label = { Text("On Account Of (Description)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Receipt Preview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("RECEIPT VOUCHER PREVIEW", color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("RASHID MUKHTIAR LAW CHAMBERS", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Text("Advocate High Court • Chamber #18, High Court Bar", color = Color.LightGray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Received with thanks from: $clientName", color = Color.White, fontSize = 12.sp)
                Text("Amount: PKR Rs. $amountReceived", color = GreenSuccess, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Matter: $suitNumber ($court)", color = Color.White, fontSize = 12.sp)
                Text("Mode: $paymentMode", color = Color.LightGray, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val receipt = """
📜 *LEGAL FEE RECEIPT VOUCHER*
*RASHID MUKHTIAR LAW CHAMBERS*
Advocate High Court, Lahore
Chamber #18, High Court Bar Association

Receipt Date: Today
Received with thanks from: *$clientName*
The sum of: *PKR Rs. $amountReceived*
Mode: $paymentMode
Case Matter: $suitNumber ($court)
Account: $onAccountOf

Thank you for trusting Rashid Mukhtiar Law Chambers.
Contact: +92 300 7861234
                            """.trimIndent()
                            viewModel.shareToWhatsApp(context, receipt)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("btn_send_receipt_whatsapp")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Send on WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val receipt = "Receipt: Rs. $amountReceived received from $clientName for $suitNumber by Rashid Mukhtiar Advocate."
                            viewModel.copyToClipboard(context, "Receipt", receipt)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Copy", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
