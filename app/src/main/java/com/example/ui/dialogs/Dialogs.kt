package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Appointment
import com.example.model.CasePriority
import com.example.model.CaseStatus
import com.example.model.LegalCase
import com.example.ui.components.CourtBadge
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.RedUrgent
import java.util.UUID

@Composable
fun AddCaseDialog(
    onDismiss: () -> Unit,
    onConfirm: (LegalCase) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var caseNumber by remember { mutableStateOf("") }
    var court by remember { mutableStateOf("Lahore High Court") }
    var bench by remember { mutableStateOf("Single Bench") }
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var clientRole by remember { mutableStateOf("Petitioner") }
    var opposingParty by remember { mutableStateOf("") }
    var opposingCounsel by remember { mutableStateOf("") }
    var stage by remember { mutableStateOf("Preliminary Hearing") }
    var nextHearingDate by remember { mutableStateOf("Next Week") }
    var feeAgreedStr by remember { mutableStateOf("100000") }
    var feePaidStr by remember { mutableStateOf("50000") }
    var priority by remember { mutableStateOf(CasePriority.NORMAL) }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = GoldBright)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Legal Case", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Case Title (e.g. Aslam vs. State)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_case_title")
                )
                OutlinedTextField(
                    value = caseNumber,
                    onValueChange = { caseNumber = it },
                    label = { Text("Case / Petition No. (e.g. W.P. 1290/26)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_case_number")
                )
                OutlinedTextField(
                    value = court,
                    onValueChange = { court = it },
                    label = { Text("Court (e.g. Lahore High Court, Sessions)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_court")
                )
                OutlinedTextField(
                    value = bench,
                    onValueChange = { bench = it },
                    label = { Text("Bench / Judge (e.g. Single Bench III)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Client Name") },
                        modifier = Modifier.weight(1f).testTag("input_client_name")
                    )
                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("Phone") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = opposingParty,
                        onValueChange = { opposingParty = it },
                        label = { Text("Opposing Party") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stage,
                        onValueChange = { stage = it },
                        label = { Text("Case Stage") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nextHearingDate,
                        onValueChange = { nextHearingDate = it },
                        label = { Text("Next Hearing Date") },
                        modifier = Modifier.weight(1f).testTag("input_next_date")
                    )
                    OutlinedTextField(
                        value = feeAgreedStr,
                        onValueChange = { feeAgreedStr = it },
                        label = { Text("Agreed Fee (PKR)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Case Brief & Remarks") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newCase = LegalCase(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            caseNumber = if (caseNumber.isNotBlank()) caseNumber else "Suit ${System.currentTimeMillis() % 10000}/2026",
                            court = court,
                            bench = bench,
                            clientName = if (clientName.isNotBlank()) clientName else "Walk-in Client",
                            clientPhone = clientPhone,
                            clientRole = clientRole,
                            opposingParty = opposingParty,
                            opposingCounsel = opposingCounsel,
                            stage = stage,
                            nextHearingDate = nextHearingDate,
                            priority = priority,
                            feeAgreed = feeAgreedStr.toLongOrNull() ?: 100000L,
                            feePaid = feePaidStr.toLongOrNull() ?: 50000L,
                            notes = notes
                        )
                        onConfirm(newCase)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                modifier = Modifier.testTag("btn_save_case")
            ) {
                Text("Save Case", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BookAppointmentDialog(
    onDismiss: () -> Unit,
    onConfirm: (Appointment) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var matterType by remember { mutableStateOf("Bail / Criminal Matter") }
    var date by remember { mutableStateOf("Tomorrow") }
    var time by remember { mutableStateOf("05:00 PM") }
    var consultationMode by remember { mutableStateOf("Chambers Meeting") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GoldBright)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Schedule Client Consultation", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Client Full Name") },
                    modifier = Modifier.fillMaxWidth().testTag("input_appointment_name")
                )
                OutlinedTextField(
                    value = clientPhone,
                    onValueChange = { clientPhone = it },
                    label = { Text("Contact Phone / WhatsApp") },
                    modifier = Modifier.fillMaxWidth().testTag("input_appointment_phone")
                )
                OutlinedTextField(
                    value = matterType,
                    onValueChange = { matterType = it },
                    label = { Text("Legal Matter / Practice Area") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time Slot") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = consultationMode,
                    onValueChange = { consultationMode = it },
                    label = { Text("Mode (Chambers / WhatsApp Video / Call)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Consultation Notes / Documents Expected") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (clientName.isNotBlank()) {
                        val app = Appointment(
                            id = UUID.randomUUID().toString(),
                            clientName = clientName,
                            clientPhone = clientPhone,
                            date = date,
                            time = time,
                            category = matterType,
                            consultationMode = consultationMode,
                            notes = notes
                        )
                        onConfirm(app)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                modifier = Modifier.testTag("btn_confirm_appointment")
            ) {
                Text("Confirm Booking", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun UpdateHearingDialog(
    case: LegalCase,
    onDismiss: () -> Unit,
    onConfirm: (newDate: String, newStage: String, notes: String) -> Unit
) {
    var newDate by remember { mutableStateOf(case.nextHearingDate) }
    var newStage by remember { mutableStateOf(case.stage) }
    var outcomeNotes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Update Hearing & Diary Record", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = case.title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2
                )
                Text(
                    text = "${case.caseNumber} • ${case.court}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = newDate,
                    onValueChange = { newDate = it },
                    label = { Text("Next Hearing Date") },
                    modifier = Modifier.fillMaxWidth().testTag("input_new_hearing_date")
                )
                OutlinedTextField(
                    value = newStage,
                    onValueChange = { newStage = it },
                    label = { Text("Next Stage (e.g. Final Arguments)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = outcomeNotes,
                    onValueChange = { outcomeNotes = it },
                    label = { Text("Today's Court Proceedings & Orders") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(newDate, newStage, outcomeNotes) },
                colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary),
                modifier = Modifier.testTag("btn_save_hearing_update")
            ) {
                Text("Update Diary", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailBottomSheet(
    case: LegalCase,
    onDismiss: () -> Unit,
    onUpdateHearingClick: () -> Unit,
    onRecordFeeClick: (amount: Long) -> Unit,
    onShareToWhatsApp: (text: String, phone: String) -> Unit,
    onCallClient: (phone: String) -> Unit
) {
    var feeInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CourtBadge(courtName = case.court)
                PriorityBadge(priority = case.priority)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = case.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "${case.caseNumber} • ${case.bench}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = GoldBright,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Next Hearing:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(case.nextHearingDate, color = RedUrgent, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Current Stage:", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Text(case.stage, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Client Name:", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Text("${case.clientName} (${case.clientRole})", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                    if (case.opposingParty.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Opposing Party:", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                            Text(case.opposingParty, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fee Tracker Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Legal Professional Fee", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Rs. %,d / %,d".format(case.feePaid, case.feeAgreed), fontWeight = FontWeight.Bold, color = GreenSuccess)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    val progress = if (case.feeAgreed > 0) (case.feePaid.toFloat() / case.feeAgreed.toFloat()).coerceIn(0f, 1f) else 1f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = GreenSuccess,
                        trackColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Remaining: Rs. %,d".format((case.feeAgreed - case.feePaid).coerceAtLeast(0L)),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = feeInput,
                                onValueChange = { feeInput = it },
                                placeholder = { Text("Amount", fontSize = 11.sp) },
                                modifier = Modifier.width(110.dp).height(50.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = {
                                    val amt = feeInput.toLongOrNull() ?: 0L
                                    if (amt > 0) {
                                        onRecordFeeClick(amt)
                                        feeInput = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess, contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("+ Pay", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onUpdateHearingClick,
                    modifier = Modifier.weight(1f).testTag("btn_detail_update_hearing"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = NavyPrimary)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Update Date", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                if (case.clientPhone.isNotBlank()) {
                    OutlinedButton(
                        onClick = { onCallClient(case.clientPhone) },
                        modifier = Modifier.weight(0.8f)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val msg = """
⚖️ *Case Update from Rashid Mukhtiar Law Chambers*
Case: ${case.title}
Suit No: ${case.caseNumber}
Court: ${case.court}
Next Date of Hearing: *${case.nextHearingDate}*
Current Stage: ${case.stage}
Chamber Contact: +92 300 7861234
                            """.trimIndent()
                            onShareToWhatsApp(msg, case.clientPhone)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavySecondary, contentColor = Color.White),
                        modifier = Modifier.weight(1f).testTag("btn_detail_whatsapp_client")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WhatsApp", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hearing History
            Text("Hearing History & Previous Orders", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (case.hearingHistory.isEmpty()) {
                Text(
                    text = "No previous history logged yet. Tap 'Update Date' after court hearing.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                case.hearingHistory.forEach { h ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(h.date, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GoldBright)
                                if (h.nextDate.isNotBlank()) {
                                    Text("Next: ${h.nextDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Text(h.courtAction, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            if (h.judgeRemarks.isNotBlank()) {
                                Text(h.judgeRemarks, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
