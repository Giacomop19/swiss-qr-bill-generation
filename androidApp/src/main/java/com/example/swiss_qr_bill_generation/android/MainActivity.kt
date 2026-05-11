package com.example.swiss_qr_bill_generation.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.swiss_qr_bill_generation.Greeting
import com.example.swiss_qr_bill_generation.android.compose.ComponentQrBill
import com.example.swiss_qr_bill_generation.android.compose.QrBillScreen
import com.example.swiss_qr_bill_generation.domain.model.QrBillData
import com.example.swiss_qr_bill_generation.domain.model.QrPayload
import com.example.swiss_qr_bill_generation.presentation.mapper.QrBillViewElementsMapper
import com.example.swiss_qr_bill_generation.presentation.screen.QrBillScreenViewModel
import com.example.swiss_qr_bill_generation.presentation.state.QrBillUiState

class MainActivity : ComponentActivity() {

    private val viewModel = QrBillScreenViewModel(
        mapper = QrBillViewElementsMapper()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.uiState.collectAsState()

                    LaunchedEffect(Unit) {
                        val sampleData = QrBillData(
                            qrPayload = QrPayload("test"),
                            amountText = "123.45",
                            accountText = "CH12345678901234567890"
                        )
                        viewModel.load(sampleData)
                    }
                    // testing the functionality on android side
                    QrBillScreen(
                        state = state,
                        renderQrCode = { qrElement, modifier ->
                            // This is the Android-specific implementation of the QR code
                            ComponentQrBill(
                                data = qrElement.payload,
                                showIcon = qrElement.showIcon,
                                modifier = modifier
                            )
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    val viewModel = QrBillScreenViewModel(QrBillViewElementsMapper())
    MyApplicationTheme {
        val state by viewModel.uiState.collectAsState()
        QrBillScreen(
            state = state,
            renderQrCode = { qrElement, modifier ->
                // This is the Android-specific implementation of the QR code
                ComponentQrBill(
                    data = qrElement.payload,
                    showIcon = qrElement.showIcon,
                    modifier = modifier
                )
            }
        )
    }
}
