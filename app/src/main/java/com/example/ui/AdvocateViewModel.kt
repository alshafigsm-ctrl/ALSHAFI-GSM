package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AdvocateRepository
import com.example.model.Appointment
import com.example.model.LegalCase
import com.example.model.MarketingStatusOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AdvocateViewModel(
    private val repository: AdvocateRepository = AdvocateRepository()
) : ViewModel() {

    val profile = repository.profile
    val legalDrafts = repository.legalDrafts
    val limitationRules = repository.limitationRules

    val cases: StateFlow<List<LegalCase>> = repository.cases
    val appointments: StateFlow<List<Appointment>> = repository.appointments
    val statusOptions: StateFlow<List<MarketingStatusOption>> = repository.statusOptions

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _courtFilter = MutableStateFlow("All")
    val courtFilter: StateFlow<String> = _courtFilter.asStateFlow()

    private val _selectedCaseForDetail = MutableStateFlow<LegalCase?>(null)
    val selectedCaseForDetail: StateFlow<LegalCase?> = _selectedCaseForDetail.asStateFlow()

    private val _showAddCaseDialog = MutableStateFlow(false)
    val showAddCaseDialog: StateFlow<Boolean> = _showAddCaseDialog.asStateFlow()

    private val _showBookAppointmentDialog = MutableStateFlow(false)
    val showBookAppointmentDialog: StateFlow<Boolean> = _showBookAppointmentDialog.asStateFlow()

    private val _showUpdateHearingDialog = MutableStateFlow<LegalCase?>(null)
    val showUpdateHearingDialog: StateFlow<LegalCase?> = _showUpdateHearingDialog.asStateFlow()

    val filteredCases: StateFlow<List<LegalCase>> = combine(
        cases,
        searchQuery,
        courtFilter
    ) { caseList, query, filter ->
        caseList.filter { c ->
            val matchesFilter = when (filter) {
                "All" -> true
                "High Court" -> c.court.contains("High Court", ignoreCase = true)
                "Sessions" -> c.court.contains("Sessions", ignoreCase = true)
                "Civil" -> c.court.contains("Civil", ignoreCase = true)
                "Family" -> c.court.contains("Family", ignoreCase = true)
                "Urgent" -> c.priority.name == "URGENT"
                else -> true
            }
            val matchesQuery = if (query.isBlank()) true else {
                c.title.contains(query, ignoreCase = true) ||
                c.caseNumber.contains(query, ignoreCase = true) ||
                c.clientName.contains(query, ignoreCase = true) ||
                c.court.contains(query, ignoreCase = true) ||
                c.stage.contains(query, ignoreCase = true)
            }
            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCourtFilter(filter: String) {
        _courtFilter.value = filter
    }

    fun selectCaseForDetail(case: LegalCase?) {
        _selectedCaseForDetail.value = case
    }

    fun setShowAddCaseDialog(show: Boolean) {
        _showAddCaseDialog.value = show
    }

    fun setShowBookAppointmentDialog(show: Boolean) {
        _showBookAppointmentDialog.value = show
    }

    fun setShowUpdateHearingDialog(case: LegalCase?) {
        _showUpdateHearingDialog.value = case
    }

    fun addNewCase(case: LegalCase) {
        repository.addCase(case)
        _showAddCaseDialog.value = false
    }

    fun updateHearing(caseId: String, newDate: String, newStage: String, outcomeNote: String) {
        repository.updateHearingDate(caseId, newDate, newStage, outcomeNote)
        _showUpdateHearingDialog.value = null
        if (_selectedCaseForDetail.value?.id == caseId) {
            _selectedCaseForDetail.value = cases.value.find { it.id == caseId }
        }
    }

    fun recordFeePayment(caseId: String, amount: Long) {
        repository.updateFeePaid(caseId, amount)
        if (_selectedCaseForDetail.value?.id == caseId) {
            _selectedCaseForDetail.value = cases.value.find { it.id == caseId }
        }
    }

    fun bookAppointment(appointment: Appointment) {
        repository.addAppointment(appointment)
        _showBookAppointmentDialog.value = false
    }

    fun updateAppointmentStatus(id: String, newStatus: String) {
        repository.updateAppointmentStatus(id, newStatus)
    }

    fun addCustomStatus(option: MarketingStatusOption) {
        repository.addCustomStatus(option)
    }

    // Helper functions for user interaction
    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    fun shareText(context: Context, text: String, subject: String = "Legal Update") {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, text)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Share via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open share chooser", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareToWhatsApp(context: Context, text: String, phoneNumber: String? = null) {
        try {
            val uri = if (phoneNumber.isNullOrBlank()) {
                Uri.parse("whatsapp://send?text=" + Uri.encode(text))
            } else {
                val cleanNumber = phoneNumber.replace("+", "").replace("-", "").replace(" ", "")
                Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=" + Uri.encode(text))
            }
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to generic share
            shareText(context, text, "WhatsApp Status / Update")
        }
    }

    fun dialPhone(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to dial phone", Toast.LENGTH_SHORT).show()
        }
    }

    // Court fee calculation according to Court Fees Act 1870 (Punjab / Federal schedule)
    fun calculateCourtFee(suitValuation: Double): String {
        return if (suitValuation <= 25000.0) {
            "No Court Fee (Exempt for valuation up to Rs. 25,000)"
        } else {
            val standardRate = suitValuation * 0.075 // 7.5%
            val cappedFee = standardRate.coerceAtMost(15000.0) // Max statutory court fee cap
            "Rs. %,.0f (7.5%% ad valorem, Maximum Statutory Cap Rs. 15,000)".format(cappedFee)
        }
    }
}
