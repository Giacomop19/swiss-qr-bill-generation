package com.example.swiss_qr_bill_generation.android.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swiss_qr_bill_generation.presentation.state.QrBillUiState
import com.example.swiss_qr_bill_generation.presentation.viewElement.QrBillViewElement
import com.example.swiss_qr_bill_generation.presentation.viewElement.Spacing
import com.example.swiss_qr_bill_generation.presentation.viewElement.Text

@Composable
fun QrBillScreen(
    state: QrBillUiState,
    renderQrCode: @Composable (QrBillViewElement, Modifier) -> Unit,
) {
    // Handle loading state
    if (state.isLoading) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        state.elements.forEach { element ->
            when (element) {
                // Handle Spacing elements
                is Spacing -> {
                    Spacer(modifier = Modifier.height((element.heightDp ?: 0).dp))
                }

                // Handle the QR Code element (using the passed lambda)
                is QrBillViewElement -> {
                    renderQrCode(element, Modifier)
                }

                is Text -> {
                    Text(
                        text = element.value ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}