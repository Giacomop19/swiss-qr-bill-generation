package com.example.swiss_qr_bill_generation.domain.builder

import com.example.swiss_qr_bill_generation.domain.model.Reference
import com.example.swiss_qr_bill_generation.domain.model.SwissQrBill
import com.example.swiss_qr_bill_generation.domain.validator.QrIbanValidator
import com.example.swiss_qr_bill_generation.domain.validator.ReferenceValidator
import com.example.swiss_qr_bill_generation.domain.validator.ValidationResult

class SwissQrPayloadBuilder(
    private val ibanValidator: QrIbanValidator,
    private val referenceValidator: ReferenceValidator
) {

    fun build(bill: SwissQrBill): String {

        validate(bill)

        return buildString {

            appendLine("SPC")
            appendLine("0200")
            appendLine("1")

            // Account
            appendLine(bill.account.value)

            // Creditor
            appendLine(bill.creditor.name)
            appendLine(bill.creditor.street)
            appendLine(bill.creditor.houseNumber)
            appendLine(bill.creditor.postalCode)
            appendLine(bill.creditor.city)
            appendLine(bill.creditor.countryCode)

            // Amount
            appendLine(
                bill.paymentAmount?.amount?.toString() ?: ""
            )

            appendLine(
                bill.paymentAmount?.currency?.name ?: ""
            )

            // Debtor
            appendLine(bill.debtor?.name ?: "")
            appendLine(bill.debtor?.street ?: "")
            appendLine(bill.debtor?.houseNumber ?: "")
            appendLine(bill.debtor?.postalCode ?: "")
            appendLine(bill.debtor?.city ?: "")
            appendLine(bill.debtor?.countryCode ?: "")

            // Reference
            when (val ref = bill.reference) {

                is Reference.QRR -> {
                    appendLine("QRR")
                    appendLine(ref.reference)
                }

                is Reference.SCOR -> {
                    appendLine("SCOR")
                    appendLine(ref.reference)
                }

                Reference.None,
                null -> {
                    appendLine("NON")
                    appendLine("")
                }
            }

            // Additional Info
            appendLine(bill.additionalInformation ?: "")
        }
    }

    private fun validate(bill: SwissQrBill) {

        val ibanValidation =
            ibanValidator.validate(bill.account)

        if (ibanValidation is ValidationResult.Error) {
            error(ibanValidation.message)
        }

        bill.reference?.let {

            val referenceValidation =
                referenceValidator.validate(it)

            if (referenceValidation is ValidationResult.Error) {
                error(referenceValidation.message)
            }
        }
    }
}