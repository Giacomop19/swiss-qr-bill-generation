package com.example.swiss_qr_bill_generation.presentation.mapper

import com.example.swiss_qr_bill_generation.domain.model.QrBillData
import com.example.swiss_qr_bill_generation.presentation.state.QrBillUiState
import com.example.swiss_qr_bill_generation.presentation.viewElement.QrBillViewElement
import com.example.swiss_qr_bill_generation.presentation.viewElement.Spacing
import com.example.swiss_qr_bill_generation.presentation.viewElement.Text

class QrBillViewElementsMapper {
    fun map(data: QrBillData): QrBillUiState {
        val payload = data.qrPayload.value

        return QrBillUiState(
            title = "Swiss QR Bill",
            subtitle = "Scan to pay",
            elements = listOf(
                Text("Amount: ${data.amountText}"),
                Text("Account: ${data.accountText}"),
                Spacing(16),
                QrBillViewElement(
                    payload = payload,
                    showIcon = true
                )
            )
        )
    }
}