package com.example.swiss_qr_bill_generation.presentation.screen

import com.example.swiss_qr_bill_generation.domain.model.QrBillData
import com.example.swiss_qr_bill_generation.presentation.mapper.QrBillViewElementsMapper
import com.example.swiss_qr_bill_generation.presentation.state.QrBillUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QrBillScreenViewModel(
    private val mapper: QrBillViewElementsMapper,
) {
    private val _uiState = MutableStateFlow(QrBillUiState(isLoading = true))
    val uiState: StateFlow<QrBillUiState> = _uiState.asStateFlow()

    fun load(data: QrBillData) {
        _uiState.value = mapper.map(data)
    }
}