package com.example.swiss_qr_bill_generation.domain.validator

import com.example.swiss_qr_bill_generation.domain.model.QrIban

class QrIbanValidator {

    fun validate(iban: QrIban): ValidationResult {

        if (iban.value.isBlank()) {
            return ValidationResult.Error("IBAN cannot be empty")
        }

        if (!iban.value.startsWith("CH")) {
            return ValidationResult.Error("Swiss IBAN required")
        }

        return ValidationResult.Success
    }
}