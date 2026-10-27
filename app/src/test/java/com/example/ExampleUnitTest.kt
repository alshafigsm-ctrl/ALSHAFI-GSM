package com.example

import com.example.data.AdvocateRepository
import com.example.model.CasePriority
import com.example.model.LegalCase
import com.example.ui.AdvocateViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testRepositoryInitialData() {
        val repo = AdvocateRepository()
        assertTrue("Cases list should not be empty", repo.cases.value.isNotEmpty())
        assertEquals("Advocate name should be Rashid Mukhtiar", "Rashid Mukhtiar", repo.profile.name)
        assertEquals("There should be at least 4 marketing status options", 4, repo.statusOptions.value.size)
    }

    @Test
    fun testMarketingStatusesContainUserPromptOptions() {
        val repo = AdvocateRepository()
        val statuses = repo.statusOptions.value
        val titles = statuses.map { it.title }

        assertTrue(titles.any { it.contains("Option 1", ignoreCase = true) })
        assertTrue(titles.any { it.contains("Option 2", ignoreCase = true) })
        assertTrue(titles.any { it.contains("Option 3", ignoreCase = true) })
        assertTrue(titles.any { it.contains("Option 4", ignoreCase = true) })
    }

    @Test
    fun testCourtFeeCalculation() {
        val viewModel = AdvocateViewModel()

        // Up to 25,000 is exempt
        val exemptResult = viewModel.calculateCourtFee(20000.0)
        assertTrue(exemptResult.contains("Exempt", ignoreCase = true))

        // Large valuation capped at Rs. 15,000
        val cappedResult = viewModel.calculateCourtFee(1000000.0)
        assertTrue(cappedResult.contains("15,000"))
    }

    @Test
    fun testAddNewCaseAndAdjourn() {
        val repo = AdvocateRepository()
        val initialCount = repo.cases.value.size

        val testCase = LegalCase(
            id = "test-case-1",
            title = "Ahmed vs. State",
            caseNumber = "Crl. Misc. 100/2026",
            court = "Lahore High Court",
            bench = "Single Bench",
            clientName = "Ahmed",
            clientPhone = "03001234567",
            clientRole = "Petitioner",
            opposingParty = "State",
            opposingCounsel = "DDPP",
            stage = "Bail Hearing",
            nextHearingDate = "Tomorrow",
            priority = CasePriority.URGENT
        )

        repo.addCase(testCase)
        assertEquals(initialCount + 1, repo.cases.value.size)

        repo.updateHearingDate("test-case-1", "Next Week", "Arguments Concluded", "Bail granted")
        val updated = repo.cases.value.find { it.id == "test-case-1" }
        assertNotNull(updated)
        assertEquals("Next Week", updated?.nextHearingDate)
        assertEquals("Arguments Concluded", updated?.stage)
        assertEquals(1, updated?.hearingHistory?.size)
    }
}
