package com.example.swiss_qr_bill_generation.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.swiss_qr_bill_generation.presentation.viewElement.QrBillViewElement

@Composable
fun NativeAndroidQrBill(
    viewElement: QrBillViewElement,
    modifier: Modifier = Modifier
) {

    ComponentQrBill(
        modifier = modifier,
        data = viewElement.payload,
        showIcon = viewElement.showIcon
    )
}