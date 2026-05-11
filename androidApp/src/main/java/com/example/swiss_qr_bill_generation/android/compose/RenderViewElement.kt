package com.example.swiss_qr_bill_generation.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.swiss_qr_bill_generation.presentation.viewElement.QrBillViewElement
import com.example.swiss_qr_bill_generation.presentation.viewElement.Spacing
import com.example.swiss_qr_bill_generation.presentation.viewElement.Text
import com.example.swiss_qr_bill_generation.presentation.viewElement.ViewElement

@Composable
fun RenderViewElement(
    element: ViewElement,
    modifier: Modifier = Modifier
) {

    when (element) {

        is QrBillViewElement -> {

            NativeAndroidQrBill(
                viewElement = element,
                modifier = modifier
            )
        }

        is Spacing -> TODO()
        is Text -> TODO()
    }
}