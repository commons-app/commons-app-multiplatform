package app.multiplatform.commons

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform