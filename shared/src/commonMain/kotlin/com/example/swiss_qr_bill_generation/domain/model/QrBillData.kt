package com.example.swiss_qr_bill_generation.domain.model

data class QrBillData(
    val qrPayload: QrPayload,
    val amountText: String,
    val accountText: String
)

data class QrPayload(
    val value: String
)