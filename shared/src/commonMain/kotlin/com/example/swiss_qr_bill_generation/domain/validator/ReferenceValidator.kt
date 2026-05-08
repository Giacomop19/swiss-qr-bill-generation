package com.example.swiss_qr_bill_generation.domain.validator

import com.example.swiss_qr_bill_generation.domain.model.Reference

class ReferenceValidator {

    fun validate(reference: Reference): ValidationResult {

        return when (reference) {

            is Reference.QRR -> {
                if (reference.reference.length != 27) {
                    ValidationResult.Error("QRR reference must be 27 digits")
                } else {
                    ValidationResult.Success
                }
            }

            is Reference.SCOR -> {
                ValidationResult.Success
            }

            Reference.None -> {
                ValidationResult.Success
            }
        }
    }
}