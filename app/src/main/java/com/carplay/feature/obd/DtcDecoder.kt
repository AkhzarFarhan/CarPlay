package com.carplay.feature.obd

/**
 * Represents a Diagnostic Trouble Code with its description and severity.
 */
data class DtcInfo(
    val code: String,
    val description: String,
    val severity: DtcSeverity
)

enum class DtcSeverity {
    MINOR,    // Level 1: Maintenance only
    MODERATE, // Level 2: Performance affected, repair soon
    SEVERE    // Level 3: Critical fault, stop vehicle
}

object DtcDecoder {
    private val universalCodes = mapOf(
        "P0101" to DtcInfo("P0101", "Mass Air Flow Circuit Range/Performance", DtcSeverity.MODERATE),
        "P0300" to DtcInfo("P0300", "Random or Multiple Cylinder Misfire Detected", DtcSeverity.SEVERE),
        "P0420" to DtcInfo("P0420", "Catalyst System Efficiency Below Threshold (Bank 1)", DtcSeverity.MODERATE),
        "P0442" to DtcInfo("P0442", "Evaporative Emission System Leak Detected (small leak)", DtcSeverity.MINOR),
        "P0115" to DtcInfo("P0115", "Engine Coolant Temperature Circuit Malfunction", DtcSeverity.SEVERE),
        "P0500" to DtcInfo("P0500", "Vehicle Speed Sensor Malfunction", DtcSeverity.MODERATE),
        "P0700" to DtcInfo("P0700", "Transmission Control System Malfunction", DtcSeverity.SEVERE)
    )

    fun decode(code: String): DtcInfo {
        return universalCodes[code.uppercase()] ?: DtcInfo(
            code = code,
            description = "Unknown fault code. Please consult vehicle manual.",
            severity = determineSeverityFromCode(code)
        )
    }

    private fun determineSeverityFromCode(code: String): DtcSeverity {
        return when {
            code.startsWith("P03") || code.startsWith("P02") -> DtcSeverity.SEVERE
            code.startsWith("P04") -> DtcSeverity.MINOR
            code.startsWith("U") -> DtcSeverity.MODERATE
            else -> DtcSeverity.MODERATE
        }
    }
}
