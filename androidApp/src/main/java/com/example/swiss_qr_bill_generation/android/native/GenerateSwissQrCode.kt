package com.example.swiss_qr_bill_generation.android.native

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.swiss_qr_bill_generation.android.utils.SWISS_CROSS_BASE64
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Base64 encoded logo string (ensure this is accessible if you move the object to a library)
// For a real library, consider passing this as a parameter or loading it from resources.
const val LOGO_IMAGE_BASE64 = SWISS_CROSS_BASE64

/**
 * Object responsible for generating QR code bitmaps.
 * This can be used to generate standard QR codes with an optional overlay logo.
 */
object GenerateSwissQRCode {
    // Define the constant for the logo background padding percentage
    private const val LOGO_BACKGROUND_PADDING_PERCENTAGE = 0.02f

    /**
     * Generates a QR code bitmap from the given data string, with an optional logo overlaid in the center.
     *
     * @param data The string data to encode in the QR code.
     * @param showIcon Flag to insert or not the logo inside the QR code.
     * @param width The desired width of the QR code bitmap in pixels.
     * @param height The desired height of the QR code bitmap in pixels.
     * @param logoBase64 Optional Base64 encoded string of a PNG image to overlay in the center of the QR code.
     *                     Ensure this image is not too large to maintain QR code scannability.
     *                     Error correction level 'H' is used for the QR code to improve scannability when a logo is present.
     * @param logoScalePercentage Percentage (0.0 to 1.0) of the QR code's width the logo should occupy. Default is 0.20 (20%).
     * @return An [ImageBitmap] containing the generated QR code, or null if generation failed.
     *         This function performs operations on the [Dispatchers.IO] context.
     */
    suspend fun generateQrCodeBitmap(
        data: String?,
        showIcon: Boolean?,
        width: Int = 300, // Default width
        height: Int = 300, // Default height
        logoBase64: String? = LOGO_IMAGE_BASE64, // Default logo, can be overridden or set to null
        logoScalePercentage: Float = 0.10f,
    ): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val writer = MultiFormatWriter()
                val bitMatrix: BitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height)

                // Create the base QR code bitmap
                val qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        qrCodeBitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }

                if (showIcon == true && logoBase64 != null) {
                    val imageBytes = try {
                        Base64.decode(logoBase64.substringAfter("base64,"), Base64.DEFAULT)
                    } catch (e: IllegalArgumentException) {
                        Log.e("GenerateQRCode", "Invalid Base64 string", e)
                        null
                    }

                    imageBytes?.let { bytes ->
                        var logoBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                        if (logoBitmap != null) {
                            val canvas = Canvas(qrCodeBitmap)

                            val targetLogoWidth = (width * logoScalePercentage).toInt()
                            val targetLogoHeight = (height * logoScalePercentage).toInt()

                            val aspectRatio = logoBitmap.width.toFloat() / logoBitmap.height.toFloat()
                            val finalLogoWidth: Int
                            val finalLogoHeight: Int

                            if (targetLogoWidth / aspectRatio <= targetLogoHeight) {
                                finalLogoWidth = targetLogoWidth
                                finalLogoHeight = (finalLogoWidth / aspectRatio).toInt()
                            } else {
                                finalLogoHeight = targetLogoHeight
                                finalLogoWidth = (finalLogoHeight * aspectRatio).toInt()
                            }

                            if (finalLogoWidth > 0 && finalLogoHeight > 0) {
                                logoBitmap = Bitmap.createScaledBitmap(logoBitmap, finalLogoWidth, finalLogoHeight, true)

                                val logoX = (width - logoBitmap.width) / 2f
                                val logoY = (height - logoBitmap.height) / 2f

                                val paint = Paint().apply {
                                    color = Color.WHITE
                                    style = Paint.Style.FILL
                                }
                                val backgroundPadding = logoBitmap.width * LOGO_BACKGROUND_PADDING_PERCENTAGE
                                val logoBackgroundRect = RectF(
                                    logoX - backgroundPadding,
                                    logoY - backgroundPadding,
                                    logoX + logoBitmap.width + backgroundPadding,
                                    logoY + logoBitmap.height + backgroundPadding,
                                )
                                canvas.drawRect(logoBackgroundRect, paint)
                                canvas.drawBitmap(logoBitmap, logoX, logoY, null)
                            } else {
                                Log.e("GenerateQRCode", "Invalid logo dimensions")
                            }
                        } else {
                            Log.e("GenerateQRCode", "Failed to decode logo image")
                        }
                    }
                }
                qrCodeBitmap.asImageBitmap()
            } catch (e: WriterException) {
                Log.e("GenerateQRCode", "Failed to generate QR code", e)
                null
            } catch (e: WriterException) {
                Log.e("GenerateQRCode", "An unexpected error occurred", e)
                null
            }
        }
    }
}