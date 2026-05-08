package com.example.swiss_qr_bill_generation.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.swiss_qr_bill_generation.android.compose.ComponentQrBill
import com.example.swiss_qr_bill_generation.android.ui.viewelement.QrBillViewElement

@Composable
fun NativeAndroidQrBill(
    viewElement: QrBillViewElement,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val state = viewElement

    ComponentQrBill(
        modifier = modifier,
        data = state.data,
        showIcon = state.showIcon,
    )
}