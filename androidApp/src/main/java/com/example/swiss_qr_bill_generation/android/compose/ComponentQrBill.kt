package com.example.swiss_qr_bill_generation.android.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiss_qr_bill_generation.android.utils.compose.GenerateSwissQRCode


/**
 * A Jetpack Compose composable function responsible for asynchronously generating and displaying
 * a Swiss QR code image.
 *
 * It takes QR code data as input and uses [GenerateSwissQRCode.generateQrCodeBitmap]
 * to create an [ImageBitmap]. While the QR code is being generated, a [CircularProgressIndicator]
 * is shown. If the generation is successful, the QR code [Image] is displayed. If the input data
 * is null or an error occurs during generation, appropriate text messages are shown.
 *
 * The generation process is handled within a [LaunchedEffect] keyed on the `data` parameter,
 * ensuring that the QR code is regenerated if the input data changes.
 *
 * The `@SuppressLint("CoroutineCreationDuringComposition")` is present, suggesting that
 * the `LaunchedEffect` is considered acceptable in this context for side effects tied to
 * composable parameters.
 *
 * @param modifier Optional [Modifier] to be applied to the root [Column] of this component.
 *                 Defaults to [Modifier].
 * @param data The string data to be encoded into the QR code. If `null`, a "data not provided"
 *             message is displayed. This is the key input for QR code generation.
 * @param showIcon An optional boolean indicating whether an icon should be embedded within the
 *                 generated QR code (passed to [GenerateSwissQRCode.generateQrCodeBitmap]).
 *                 The exact behavior depends on the implementation of the QR generation utility.
 *                 Defaults to `null`, which might imply a default behavior in the generator.
 * @param accessibilityLabel An optional string to be used as the content description for the
 *                           generated QR code image, enhancing accessibility. If not provided,
 *                           a generic description might be used by the [Image] composable.
 *                           (Note: This parameter is declared but not explicitly used in the [Image]'s
 *                           `contentDescription` in the provided code, which uses a fixed string.)
 * @param accessibilityValue An optional string that could provide additional accessibility information
 *                           about the value or state represented by the QR code.
 *                           (Note: This parameter is declared but not used in the provided code.)
 */

@Composable
fun ComponentQrBill(
    modifier: Modifier = Modifier,
    data: String? = null,
    showIcon: Boolean? = null,
) {
    var bitmapState by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    /**
     * A [LaunchedEffect] that triggers QR code generation whenever the `data` parameter changes.
     * This is the correct way to perform side effects (like image generation) in response to
     * state or parameter changes in Compose.
     */
    LaunchedEffect(key1 = data) {
        if (data != null) {
            isLoading = true
            bitmapState = null
            try {
                val generatedBitmap = GenerateSwissQRCode.generateQrCodeBitmap(data, showIcon)
                bitmapState = generatedBitmap
            } catch (e: IllegalStateException) {
                Log.e("ComponentQrIban", "Error generating QR code", e)
                bitmapState = null
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
            bitmapState = null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (data == null) {
                Text("QR Code data not provided ")
            } else {
                bitmapState?.let { imageBitmap ->
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Generated QR Code Bitmap",
                        modifier = Modifier.size(300.dp),
                    )
                } ?: run {
                    Text("Could not generate QR Code.")
                }
            }
        }
    }
}

@Suppress("FunctionNaming", "UnusedPrivateMember")
@Preview
@Composable
private fun PreviewComponentQrIban() {
    ComponentQrBill(
        data = "test",
    )
}