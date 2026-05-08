package com.example.swiss_qr_bill_generation.domain.model

import kotlin.jvm.JvmInline

data class SwissQrBill(
    val account: QrIban,
    val creditor: Party,
    val debtor: Party?,
    val paymentAmount: PaymentAmount?,
    val reference: Reference?,
    val additionalInformation: String? = null
)

@JvmInline
value class QrIban(
    val value: String
)

data class Party(
    val name: String,
    val street: String,
    val houseNumber: String,
    val postalCode: String,
    val city: String,
    val countryCode: String
)

data class PaymentAmount(
    val amount: Double,
    val currency: Currency
)

enum class Currency {
    CHF,
    EUR
}

sealed interface Reference {

    data class QRR(
        val reference: String
    ) : Reference

    data class SCOR(
        val reference: String
    ) : Reference

    data object None : Reference
}