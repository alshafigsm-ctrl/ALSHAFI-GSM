package com.example.model

enum class CasePriority {
    URGENT,
    HIGH,
    NORMAL
}

enum class CaseStatus {
    ACTIVE,
    STAY_GRANTED,
    ADJOURNED,
    DECIDED
}

data class HearingRecord(
    val date: String,
    val courtAction: String,
    val judgeRemarks: String = "",
    val nextDate: String = ""
)

data class LegalCase(
    val id: String,
    val title: String,
    val caseNumber: String,
    val court: String, // e.g. "Lahore High Court", "Sessions Court", "Civil Court", "Supreme Court"
    val bench: String, // e.g. "Single Bench III", "Division Bench II"
    val clientName: String,
    val clientPhone: String,
    val clientRole: String, // "Petitioner / Appellant", "Respondent", "Defendant"
    val opposingParty: String,
    val opposingCounsel: String,
    val stage: String, // "Arguments on Stay", "Cross Examination", "Evidence", "Bail Hearing", "Final Order"
    val nextHearingDate: String,
    val hearingTime: String = "09:30 AM",
    val courtroomNumber: String = "Court #4",
    val priority: CasePriority = CasePriority.NORMAL,
    val status: CaseStatus = CaseStatus.ACTIVE,
    val feeAgreed: Long = 100000L,
    val feePaid: Long = 50000L,
    val notes: String = "",
    val hearingHistory: List<HearingRecord> = emptyList()
)

data class Appointment(
    val id: String,
    val clientName: String,
    val clientPhone: String,
    val date: String,
    val time: String,
    val category: String, // "Criminal / Bail", "Civil Dispute", "Family / Khula", "High Court Writ", "Property Matter"
    val consultationMode: String, // "Chambers Meeting", "Urgent Phone Call", "WhatsApp Video"
    val status: String = "Confirmed",
    val notes: String = ""
)

data class MarketingStatusOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String, // "Portfolio Highlight", "Short & Impact", "Urdu Professional", "One-Liner Story"
    val rawContent: String,
    val tags: List<String>,
    val emoji: String
)

data class LegalDraft(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val content: String
)

data class LimitationRule(
    val actionType: String,
    val court: String,
    val duration: String,
    val provision: String,
    val notes: String
)

data class AdvocateProfile(
    val name: String = "Rashid Mukhtiar",
    val designation: String = "Advocate High Court & Legal Consultant",
    val chambersName: String = "Rashid Mukhtiar Law Chambers",
    val barCouncilReg: String = "PBC Reg: P-42190 / LHCBA",
    val experienceYears: Int = 16,
    val officeAddress: String = "Chamber #18, High Court Bar Association, Court Road",
    val phone: String = "+92 300 7861234",
    val whatsapp: String = "+923007861234",
    val email: String = "advocate.rashidmukhtiar@gmail.com",
    val practiceAreas: List<String> = listOf(
        "Civil Litigation",
        "Criminal Law & Bail",
        "High Court Constitutional Writs",
        "Family & Succession",
        "Property & Land Revenue",
        "Corporate & Contracts",
        "Arbitration & Banking"
    ),
    val totalResolvedCases: Int = 480,
    val activeCasesCount: Int = 38,
    val successRate: String = "96%"
)
