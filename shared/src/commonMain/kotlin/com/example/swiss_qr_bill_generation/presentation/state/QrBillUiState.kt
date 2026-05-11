package com.example.swiss_qr_bill_generation.presentation.state

import com.example.swiss_qr_bill_generation.presentation.viewElement.ViewElement

data class QrBillUiState(
    val title: String = "Swiss QR Bill",
    val subtitle: String = "",
    val elements: List<ViewElement> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)