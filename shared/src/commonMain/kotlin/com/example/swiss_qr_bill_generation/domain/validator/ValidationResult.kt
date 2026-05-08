package com.example.swiss_qr_bill_generation.domain.validator

sealed interface ValidationResult {

    data object Success : ValidationResult

    data class Error(
        val message: String
    ) : ValidationResult
}