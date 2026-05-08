package com.example.swiss_qr_bill_generation.viewModel

import com.example.swiss_qr_bill_generation.domain.builder.SwissQrPayloadBuilder
import com.example.swiss_qr_bill_generation.domain.model.SwissQrBill

class SwissQrBillViewModel(
    private val payloadBuilder: SwissQrPayloadBuilder
) {

    fun generatePayload(
        bill: SwissQrBill
    ): String {
        return payloadBuilder.build(bill)
    }
}