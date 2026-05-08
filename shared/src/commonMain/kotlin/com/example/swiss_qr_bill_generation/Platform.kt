package com.example.swiss_qr_bill_generation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform