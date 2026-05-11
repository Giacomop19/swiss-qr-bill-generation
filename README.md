# Swiss QR Bill KMP

Kotlin Multiplatform library for generating and rendering Swiss QR Bills using:

* Shared business logic
* Native QR generation on Android and iOS
* Compose UI on Android
* SwiftUI on iOS
* Shared ViewModels and UI state

The goal of the project is to provide a clean architecture for Swiss QR Bill generation while keeping:

* validation centralized
* payload generation shared
* QR rendering native
* UI rendering platform-specific

---

# Architecture

The project follows a layered KMP architecture.

```text
Shared Business Logic
        ↓
QrPayloadBuilder
        ↓
Shared UiState + ViewElements
        ↓
Platform Renderer
        ↓
Native QR Generator
```

---

# Project Structure

```text
shared/
 └── presentation/
     └── mapper/
     │   └── QrBillViewElementsMapper.kt
     └── screen/   
     │   └── QrBillScreenViewModel.kt
     └── state/     
     │   └── QrBillUiState.kt
     └── viewElement/   
     │   └── QrBillViewElement.kt
     └── viewModel/   
         └── QrBillViewModel.kt

androidApp/
 ├── compose/
 │    ├── QrBillScreen.kt
 │    ├── NativeAndroidQrBill.kt
 │    ├── ComponentQrBill.kt
 │    └── RenderViewElement.kt
 ├── native/
 │    └── GenerateSwissQrCode.kt
 └── utils/
      └── SwissCrossBase64.kt

iosApp/
 └── SwiftUI/
      ├── QrBillScreenView.swift
      ├── QrBillQrCodeView.swift
      └── NativeQrGenerator.swift
```

---

# Features

## Shared Business Logic

* Swiss QR Bill payload generation
* Validation rules
* Shared ViewModels
* Shared UI state
* Shared view elements

## Android

* Native QR generation using ZXing
* Compose UI
* Swiss cross overlay
* Bitmap rendering

## iOS

* Native QR generation using CoreImage
* SwiftUI rendering
* Native image generation

---

# Shared Models

## SwissQrBill

```kotlin
SwissQrBill(
    account = "CH4431999123000889012",
    creditor = Address(
        name = "John Doe",
        street = "Bahnhofstrasse",
        buildingNumber = "1",
        postalCode = "8000",
        city = "Zurich",
        countryCode = "CH"
    ),
    debtor = Address(
        name = "Jane Doe",
        street = "Via Roma",
        buildingNumber = "10",
        postalCode = "6900",
        city = "Lugano",
        countryCode = "CH"
    ),
    amount = PaymentAmount(
        value = BigDecimal("120.00"),
        currency = Currency.CHF
    ),
    reference = PaymentReference.Qrr(
        "210000000003139471430009017"
    ),
    unstructuredMessage = "Invoice 2026-001"
)
```

---

# QrPayloadBuilder

The `QrPayloadBuilder` is responsible for:

* generating the Swiss QR payload string
* validating the data
* enforcing formatting rules
* preparing the final payload for native QR generation

Example:

```kotlin
val payload = QrPayloadBuilder()
    .build(bill)
```

Result:

```text
SPC
0200
1
CH4431999123000889012
...
```

The generated payload is then passed to the native QR generator.

---

# Shared Presentation Layer

## QrBillUiState

Represents the whole screen state.

```kotlin
QrBillUiState(
    title = "Swiss QR Bill",
    subtitle = "Scan to pay",
    elements = listOf(...)
)
```

## QrBillViewElement

Shared UI blocks used by Android and iOS.

```kotlin
sealed interface ViewElement
```

Implemented elements:

* Text
* Spacing
* QrBillViewElement

Example:

```kotlin
QrBillViewElement(
    payload = payload.value,
    showIcon = true
)
```

## QrBillScreenViewModel

Responsible for:

* preparing the screen state
* mapping domain data
* exposing StateFlow

Example:

```kotlin
viewModel.uiState
```

---

# Android Implementation

## NativeAndroidQrBill

Bridge between shared `QrBillViewElement` and Android Compose.

```kotlin
@Composable
fun NativeAndroidQrBill(
    viewElement: QrBillViewElement.QrCode,
    modifier: Modifier = Modifier,
) {
    ComponentQrBill(
        modifier = modifier,
        data = viewElement.payload,
        showIcon = viewElement.showIcon,
    )
}
```

---

# ComponentQrBill

Responsible for:

* rendering the QR code
* rendering the Swiss cross overlay
* handling Compose layout

The component receives:

* payload string
* showIcon flag

The component does not contain:

* business logic
* validation logic
* payload generation

---

# GenerateSwissQrCode

Android QR generation uses ZXing.

Example:

```kotlin
GenerateSwissQrCode.generate(payload)
```

The QR generation is fully native.

Recommended QR configuration:

```kotlin
EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M
```

This is required because the Swiss cross overlays the QR code.

---

# Swiss Cross Overlay

The Swiss cross is stored as Base64:

```kotlin
object SwissCrossBase64
```

The overlay is optional and controlled by:

```kotlin
showIcon = true
```

---

# iOS Implementation

## SwiftUI Renderer

The iOS layer renders the shared state using SwiftUI.

Example:

```swift
QrBillQrCodeView(
    payload: qr.payload,
    showIcon: qr.showIcon
)
```

## Native QR Generation

iOS uses:

* CoreImage
* CIQRCodeGenerator

The payload string generated in shared code is passed directly to the native generator.

---

# Rendering Flow

```text
SwissQrBill
      ↓
QrPayloadBuilder
      ↓
QrPayload
      ↓
QrBillViewElementsMapper
      ↓
QrBillUiState
      ↓
QrBillViewElement
      ↓
Android/iOS Renderer
      ↓
Native QR Generator
```

---

# Why Native QR Generation

The project intentionally keeps QR generation native because:

* better platform integration
* easier image export
* native performance
* native bitmap support
* easier PDF generation
* easier printing integration

The shared layer remains responsible only for:

* data validation
* payload generation
* screen state
* UI structure

---

# Design Principles

## Shared Owns

* business logic
* validation
* payload generation
* screen state
* view elements

## Platform Owns

* rendering
* QR generation
* image generation
* UI framework
* platform styling

---


# Tech Stack

## Shared

* Kotlin Multiplatform
* Kotlin Coroutines
* StateFlow
* Kotlin Serialization

## Android

* Jetpack Compose
* ZXing

## iOS

* SwiftUI
* CoreImage

---

# Goals

The project aims to provide:

* clean KMP architecture
* strict Swiss QR validation
* platform-native rendering
* scalable UI system
* reusable business logic
* future-proof payment infrastructure

---

# License

MIT

## Created By @Giacomo Pumapillo
