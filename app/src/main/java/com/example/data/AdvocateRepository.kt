package com.example.data

import com.example.model.AdvocateProfile
import com.example.model.Appointment
import com.example.model.CasePriority
import com.example.model.CaseStatus
import com.example.model.HearingRecord
import com.example.model.LegalCase
import com.example.model.LegalDraft
import com.example.model.LimitationRule
import com.example.model.MarketingStatusOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class AdvocateRepository {

    val profile = AdvocateProfile()

    private val _cases = MutableStateFlow<List<LegalCase>>(initialCases)
    val cases: StateFlow<List<LegalCase>> = _cases.asStateFlow()

    private val _appointments = MutableStateFlow<List<Appointment>>(initialAppointments)
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _statusOptions = MutableStateFlow<List<MarketingStatusOption>>(initialMarketingStatuses)
    val statusOptions: StateFlow<List<MarketingStatusOption>> = _statusOptions.asStateFlow()

    fun addCase(case: LegalCase) {
        _cases.update { listOf(case) + it }
    }

    fun updateHearingDate(caseId: String, newDate: String, newStage: String, outcomeNote: String) {
        _cases.update { list ->
            list.map { c ->
                if (c.id == caseId) {
                    val record = HearingRecord(
                        date = c.nextHearingDate,
                        courtAction = c.stage,
                        judgeRemarks = outcomeNote,
                        nextDate = newDate
                    )
                    c.copy(
                        nextHearingDate = newDate,
                        stage = if (newStage.isNotBlank()) newStage else c.stage,
                        hearingHistory = listOf(record) + c.hearingHistory,
                        notes = if (outcomeNote.isNotBlank()) "${c.notes}\n[${c.nextHearingDate}]: $outcomeNote".trim() else c.notes
                    )
                } else c
            }
        }
    }

    fun updateFeePaid(caseId: String, amountAdded: Long) {
        _cases.update { list ->
            list.map { c ->
                if (c.id == caseId) {
                    c.copy(feePaid = (c.feePaid + amountAdded).coerceAtMost(c.feeAgreed))
                } else c
            }
        }
    }

    fun addAppointment(appointment: Appointment) {
        _appointments.update { listOf(appointment) + it }
    }

    fun updateAppointmentStatus(id: String, newStatus: String) {
        _appointments.update { list ->
            list.map { if (it.id == id) it.copy(status = newStatus) else it }
        }
    }

    fun addCustomStatus(option: MarketingStatusOption) {
        _statusOptions.update { listOf(option) + it }
    }

    val legalDrafts: List<LegalDraft> = listOf(
        LegalDraft(
            id = "d1",
            title = "Urgent Stay Application (Order 39 Rules 1 & 2 CPC)",
            category = "Civil Law",
            description = "Application for ad-interim injunction against illegal demolition / dispossession",
            content = """
IN THE COURT OF LEARNED SENIOR CIVIL JUDGE, LAHORE
Suit No. ________ / 2026

[Plaintiff Name] S/o [Father Name]
R/o [Address]
                                                  ... PLAINTIFF / APPLICANT
                               VERSUS
[Defendant Name] & Others
                                                  ... DEFENDANTS / RESPONDENTS

APPLICATION UNDER ORDER XXXIX RULES 1 & 2 READ WITH SECTION 151 C.P.C FOR GRANT OF TEMPORARY INJUNCTION

Respectfully Sheweth:
1. That the applicant has filed the accompanying suit for declaration and permanent injunction, contents of which may kindly be read as integral part of this application.
2. That the applicant has a strong prima facie case in his favor.
3. That balance of convenience also lies completely in favor of the applicant.
4. That if the respondents are not restrained from interfering in the lawful peaceful possession of the applicant, the applicant shall suffer irreparable loss and injury which cannot be compensated in terms of money.

PRAYER:
It is therefore respectfully prayed that an ad-interim temporary injunction may graciously be granted in favor of the applicant, restraining the respondents, their agents and servants from dispossessing the applicant from the suit property till final decision of the suit.

APPLICANT
Through
RASHID MUKHTIAR ADVOCATE
Advocate High Court
Chambers #18, High Court Bar Association
            """.trimIndent()
        ),
        LegalDraft(
            id = "d2",
            title = "Pre-Arrest Bail Petition (Section 498 Cr.P.C)",
            category = "Criminal Law",
            description = "Bail petition before Court of Sessions / High Court against FIR registered with mala fide",
            content = """
IN THE COURT OF LEARNED SESSIONS JUDGE, LAHORE
Crl. Misc. No. ________ / B / 2026

[Accused Name] S/o [Father Name]
                                                  ... PETITIONER / ACCUSED
                               VERSUS
The State & [Complainant Name]
                                                  ... RESPONDENTS

PETITION UNDER SECTION 498 Cr.P.C FOR GRANT OF PRE-ARREST BAIL IN CASE FIR NO. [FIR Number] DATED [Date] OFFENCE UNDER SECTIONS 420/468/471 PPC POLICE STATION [Police Station]

Respectfully Sheweth:
1. That the petitioner has been falsely and maliciously implicated in the above-mentioned FIR due to ulterior motives and business rivalry.
2. That there is an unexplained delay of 18 days in lodging the FIR, which reflects deliberation and consultation.
3. That the allegations are totally omnibus and no specific overt act has been attributed to the petitioner.
4. That the petitioner is a respectable citizen with no previous criminal record and is ready to join the investigation.
5. That if the petitioner is arrested, he shall face irreparable humiliation and loss of reputation at the hands of the police.

PRAYER:
It is therefore respectfully prayed that the petitioner may graciously be admitted to pre-arrest bail ad-interim, and after notice, the same may kindly be confirmed, in the interest of justice.

PETITIONER
Through
RASHID MUKHTIAR ADVOCATE
Advocate High Court
            """.trimIndent()
        ),
        LegalDraft(
            id = "d3",
            title = "General Power of Attorney (Mukhtarnama Aam)",
            category = "General Legal",
            description = "Standard comprehensive power of attorney for property management and legal representation",
            content = """
GENERAL POWER OF ATTORNEY (MUKHTAR-NAMA AAM)

KNOW ALL MEN BY THESE PRESENTS that I, [Principal Name], holding CNIC No. [CNIC], resident of [Address], do hereby appoint, nominate and constitute [Attorney Name], holding CNIC No. [CNIC], resident of [Address], as my true and lawful General Attorney to act for me and on my behalf:

1. To manage, superintend, lease, and represent my property situated at [Property Details].
2. To appear, institute, defend, sign and verify plaints, written statements, appeals, revisions, and execution petitions before all Courts of Civil, Criminal, Revenue and High Court.
3. To engage legal counsels, Advocates (including Rashid Mukhtiar Advocate), sign Vakalatnama and give statements.
4. To deposit and withdraw court fees, obtain certified copies of orders and judgments.

All acts, deeds and things done by the said Attorney shall be construed as acts done by me personally.

EXECUTANT / PRINCIPAL:
Signature: __________________
Thumb Impression: ___________
Witness 1: __________________
Witness 2: __________________
Drafted by: Rashid Mukhtiar Advocate, High Court
            """.trimIndent()
        ),
        LegalDraft(
            id = "d4",
            title = "Legal Notice for Recovery of Dues / Contract Breach",
            category = "Commercial & Notice",
            description = "Statutory legal notice prior to civil recovery suit or Section 489-F PPC proceedings",
            content = """
REGISTERED A.D. & U.P.C.
LEGAL NOTICE

To:
[Recipient Name / Company]
[Full Address]

Under instructions from and on behalf of my client, [Client Name], resident of [Address], I hereby serve upon you this Legal Notice as under:

1. That my client entered into an agreement dated [Date] with you for [Subject Matter].
2. That my client performed all his contractual obligations diligently, however, an outstanding amount of Rs. [Amount] remains unpaid by you despite multiple requests.
3. That your deliberate failure to pay constitutes breach of trust and actionable wrong under the law.

NOW THEREFORE, through this legal notice, you are hereby called upon to pay the outstanding balance of Rs. [Amount] within 14 days of receipt of this notice, failing which my client has given me peremptory instructions to initiate civil recovery proceedings as well as criminal action in the competent court of law at your sole risk, cost and consequence.

RASHID MUKHTIAR ADVOCATE
Advocate High Court
Office: Chamber #18, High Court Bar Association
            """.trimIndent()
        ),
        LegalDraft(
            id = "d5",
            title = "Bayan-e-Halafi / Affidavit Format",
            category = "Affidavit",
            description = "Standard verification affidavit for petitions, written statements & bail matters",
            content = """
AFFIDAVIT / BAYAN-E-HALAFI

I, [Deponent Name] S/o [Father Name], Muslim, adult, holding CNIC No. [CNIC], resident of [Address], do hereby solemnly affirm and declare on oath as under:

1. That the deponent is the petitioner/plaintiff in the above-titled matter and is fully conversant with the facts of the case.
2. That the contents of the accompanying petition/application have been read over and explained to the deponent in Urdu, which are true and correct to the best of my knowledge and belief.
3. That nothing has been concealed or misstated therein.

DEPONENT
Verified on oath at Lahore on this [Day] day of [Month], 2026, that the contents of this affidavit are true and correct.
DEPONENT: ________________
Identified by: Rashid Mukhtiar Advocate
            """.trimIndent()
        )
    )

    val limitationRules: List<LimitationRule> = listOf(
        LimitationRule(
            actionType = "First Appeal against Civil Decree",
            court = "District Court / High Court",
            duration = "30 Days (District) / 90 Days (High Court)",
            provision = "Article 152 & 156, Limitation Act 1908",
            notes = "Time requisite for obtaining certified copy is excluded under Section 12."
        ),
        LimitationRule(
            actionType = "Criminal Appeal against Conviction",
            court = "High Court / Sessions Court",
            duration = "30 Days (High Court) / 30 Days (Sessions)",
            provision = "Article 154 & 155, Limitation Act",
            notes = "Death sentence appeal / confirmation within 7 days under Section 374 CrPC."
        ),
        LimitationRule(
            actionType = "Civil Revision Petition (Section 115 CPC)",
            court = "High Court",
            duration = "90 Days from the date of impugned order",
            provision = "Section 115(1) CPC as amended",
            notes = "Accompanied by copies of pleadings, evidence and impugned judgments."
        ),
        LimitationRule(
            actionType = "Review of Judgment / Decree",
            court = "Same Court (Order 47 CPC)",
            duration = "20 Days (High Court) / 30 Days (Subordinate)",
            provision = "Article 162 & 173, Limitation Act",
            notes = "Only on discovery of new important matter or error apparent on face of record."
        ),
        LimitationRule(
            actionType = "Execution of Decree",
            court = "Executing Court",
            duration = "3 Years from date of final decree",
            provision = "Article 182, Limitation Act",
            notes = "Subsequent execution application within 3 years of previous step in aid."
        ),
        LimitationRule(
            actionType = "Constitutional Writ Petition (Article 199)",
            court = "High Court",
            duration = "No rigid bar, but must be filed without laches (promptly)",
            provision = "Constitution of Pakistan 1973",
            notes = "Unexplained delay/laches can cause dismissal on preliminary threshold."
        )
    )

    companion object {
        private val initialCases = listOf(
            LegalCase(
                id = "c1",
                title = "Malik Muhammad Jahangir vs. Province of Punjab & LDA",
                caseNumber = "W.P. No. 18492/2025",
                court = "Lahore High Court",
                bench = "Single Bench - Justice Tariq Saleem",
                clientName = "Malik Jahangir",
                clientPhone = "+92 301 7654321",
                clientRole = "Petitioner",
                opposingParty = "LDA & Director Town Planning",
                opposingCounsel = "Advocate General Office & Rao Naveed",
                stage = "Arguments on Injunction / Stay Confirmation",
                nextHearingDate = "Tomorrow, 09:30 AM",
                hearingTime = "09:30 AM",
                courtroomNumber = "Court #4, Item #12",
                priority = CasePriority.URGENT,
                status = CaseStatus.STAY_GRANTED,
                feeAgreed = 200000L,
                feePaid = 150000L,
                notes = "Ad-interim stay granted against illegal demolition notice. File certified copies of revenue record before next date.",
                hearingHistory = listOf(
                    HearingRecord("Oct 14, 2026", "Notice issued to LDA. Ad-interim stay granted till next date.", "Justice remarked notice prima facie illegal."),
                    HearingRecord("Sep 28, 2026", "Fresh filing admitted for preliminary hearing.", "Office objection removed by counsel.")
                )
            ),
            LegalCase(
                id = "c2",
                title = "State vs. Tariq Mehmood & Anr (Bail Matter)",
                caseNumber = "Crl. Misc. No. 6420/B/2026",
                court = "Sessions Court, Lahore",
                bench = "Addl. Sessions Judge Court #9",
                clientName = "Tariq Mehmood",
                clientPhone = "+92 321 4455667",
                clientRole = "Accused / Petitioner",
                opposingParty = "State / Complainant Bilal Aslam",
                opposingCounsel = "Deputy District Public Prosecutor",
                stage = "Post-Arrest Bail Arguments (Sec 497 CrPC)",
                nextHearingDate = "Today, 11:00 AM",
                hearingTime = "11:00 AM",
                courtroomNumber = "Court #9, Item #07",
                priority = CasePriority.URGENT,
                status = CaseStatus.ACTIVE,
                feeAgreed = 120000L,
                feePaid = 80000L,
                notes = "FIR registered under 489-F PPC. Cheque issued as guarantee; civil dispute converted into criminal litigation.",
                hearingHistory = listOf(
                    HearingRecord("Oct 20, 2026", "Police file requisitioned. Investigating Officer directed to appear.", "IO absent, notice repeated.")
                )
            ),
            LegalCase(
                id = "c3",
                title = "Haji Abdul Rasheed vs. Mst. Nasreen Akhtar & Ors",
                caseNumber = "Civil Suit No. 341/2024",
                court = "Civil Court, Lahore",
                bench = "Senior Civil Judge (East)",
                clientName = "Haji Abdul Rasheed",
                clientPhone = "+92 333 9988776",
                clientRole = "Plaintiff",
                opposingParty = "Mst. Nasreen & 4 Others",
                opposingCounsel = "Ch. Zafar Iqbal Adv.",
                stage = "Cross Examination of Plaintiff Witnesses",
                nextHearingDate = "Nov 03, 2026",
                hearingTime = "10:15 AM",
                courtroomNumber = "Court #2, Item #19",
                priority = CasePriority.NORMAL,
                status = CaseStatus.ACTIVE,
                feeAgreed = 180000L,
                feePaid = 120000L,
                notes = "Suit for Specific Performance regarding 2 Kanal commercial plot in Johar Town. PW-2 affidavit submitted.",
                hearingHistory = listOf(
                    HearingRecord("Oct 08, 2026", "PW-1 cross-examined partially. Adjourned on request of defense counsel.", "Costs of Rs. 2,000 imposed on defense.")
                )
            ),
            LegalCase(
                id = "c4",
                title = "Mst. Fatima Bibi vs. Kamran Shahzad (Custody)",
                caseNumber = "Family Suit No. 89/2025",
                court = "Family Court, Lahore",
                bench = "Judge Family Court IV",
                clientName = "Mst. Fatima Bibi",
                clientPhone = "+92 304 5544332",
                clientRole = "Petitioner / Mother",
                opposingParty = "Kamran Shahzad",
                opposingCounsel = "Syed Mansoor Ali Adv.",
                stage = "Reconciliation & Interim Maintenance Order",
                nextHearingDate = "Nov 06, 2026",
                hearingTime = "11:30 AM",
                courtroomNumber = "Family Court #4",
                priority = CasePriority.HIGH,
                status = CaseStatus.ACTIVE,
                feeAgreed = 90000L,
                feePaid = 90000L,
                notes = "Interim maintenance fixed at Rs. 25,000 per minor child. Father failed to deposit last month installment.",
                hearingHistory = listOf(
                    HearingRecord("Oct 12, 2026", "Reconciliation proceedings failed. Written statement filed.", "Issues framed.")
                )
            ),
            LegalCase(
                id = "c5",
                title = "Al-Karam Cotton Mills vs. Commissioner Inland Revenue",
                caseNumber = "Sales Tax Appeal No. 219/2025",
                court = "Lahore High Court",
                bench = "Division Bench - Justice Shams & Justice Waqas",
                clientName = "Sardar Karam Elahi (CEO)",
                clientPhone = "+92 300 8472910",
                clientRole = "Appellant",
                opposingParty = "FBR / Commissioner Inland Revenue",
                opposingCounsel = "Standing Counsel for Federal Government",
                stage = "Final Arguments on Tax Assessment Notice",
                nextHearingDate = "Nov 12, 2026",
                hearingTime = "10:00 AM",
                courtroomNumber = "Court #1, Item #04",
                priority = CasePriority.HIGH,
                status = CaseStatus.ACTIVE,
                feeAgreed = 350000L,
                feePaid = 250000L,
                notes = "Challenge to arbitrary show cause notice issued under Section 11(2) of Sales Tax Act 1990 without audit report.",
                hearingHistory = listOf(
                    HearingRecord("Sep 15, 2026", "Interim relief extended. Department granted 3 weeks to file reply.", "Bench directed original file.")
                )
            )
        )

        private val initialAppointments = listOf(
            Appointment(
                id = "a1",
                clientName = "Sheikh Noman Rafiq",
                clientPhone = "+92 300 9876543",
                date = "Today",
                time = "04:30 PM",
                category = "Commercial Contract Breach",
                consultationMode = "Chambers Meeting",
                status = "Confirmed",
                notes = "Wants to file recovery suit regarding delayed equipment shipment worth 4.2 Million."
            ),
            Appointment(
                id = "a2",
                clientName = "Dr. Asma Qureshi",
                clientPhone = "+92 321 8877665",
                date = "Tomorrow",
                time = "05:15 PM",
                category = "High Court Service Writ",
                consultationMode = "WhatsApp Video",
                status = "Confirmed",
                notes = "Senior doctor challenging wrongful promotion supersession in health department."
            ),
            Appointment(
                id = "a3",
                clientName = "Chaudhry Arshad Gujjar",
                clientPhone = "+92 345 1122334",
                date = "Oct 31, 2026",
                time = "06:00 PM",
                category = "Ancestral Land Partition",
                consultationMode = "Chambers Meeting",
                status = "Pending",
                notes = "Revenue partition dispute among legal heirs; mutation challenged."
            )
        )

        private val initialMarketingStatuses = listOf(
            MarketingStatusOption(
                id = "opt1",
                title = "Option 1 (Portfolio Highlight — Modern & Impressive)",
                subtitle = "Best for professional status, story posts & client showcase",
                category = "Portfolio Highlight",
                emoji = "📱",
                tags = listOf("Modern", "Impressive", "High Engagement"),
                rawContent = """
📱 Project Completed!
Proud to announce the Android Application developed for Rashid Mukhtiar Advocate! ⚖️
Kya aap bhi apne business ya profession ke liye ek powerful Android App banwana chahte hain?
📲 Modern UI, smooth performance aur best design ke sath app banwane ke liye aaj hi rabta karein! 🚀
                """.trimIndent()
            ),
            MarketingStatusOption(
                id = "opt2",
                title = "Option 2 (Short & High Impact)",
                subtitle = "Punchy, direct, ideal for quick WhatsApp status updates",
                category = "Short & Impact",
                emoji = "💡",
                tags = listOf("Short", "High Impact", "Direct CTA"),
                rawContent = """
Idea Aap Ka, App Hamari! 💡📲
Successfully developed Android App for Rashid Mukhtiar Advocate. ⚖️
Professional & custom Android App development for lawyers, businesses & entrepreneurs.
💬 DM me to get your app built today!
                """.trimIndent()
            ),
            MarketingStatusOption(
                id = "opt3",
                title = "Option 3 (Urdu Professional Styling)",
                subtitle = "Culturally resonant, warm, highly relatable Urdu/Roman copy",
                category = "Urdu Professional",
                emoji = "💻",
                tags = listOf("Urdu", "Digital Business", "Trust & Quality"),
                rawContent = """
apne business ko digital banayein! 💻📱
Rashid Mukhtiar Advocate ke liye tayyar ki gayi shandar Android Application. ⚖️
Agar aap bhi apne business, shop ya services ke liye Android App banwana chahte hain toh abhi rabta karein. Behtareen quality aur mukammal satisfaction! ⚡
                """.trimIndent()
            ),
            MarketingStatusOption(
                id = "opt4",
                title = "Option 4 (One-Liner / Story Status)",
                subtitle = "Snappy single-card story format for Instagram, FB & WhatsApp",
                category = "One-Liner Story",
                emoji = "🚀",
                tags = listOf("Story", "Quick Read", "Instant Buzz"),
                rawContent = """
🚀 Latest Project Live: Android App for Rashid Mukhtiar Advocate! ⚖️
Need a professional app for your business or service? Let's turn your idea into reality. DM now! 📲
                """.trimIndent()
            )
        )
    }
}
