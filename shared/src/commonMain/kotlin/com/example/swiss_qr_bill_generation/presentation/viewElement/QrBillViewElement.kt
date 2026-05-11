package com.example.swiss_qr_bill_generation.presentation.viewElement

sealed interface ViewElement

data class QrBillViewElement(
    val payload: String,
    val showIcon: Boolean
) : ViewElement

data class Text(
    val value: String?,
) : ViewElement

data class Spacing(
    val heightDp: Int?,
) : ViewElement

