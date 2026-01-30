package com.app.commons

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform