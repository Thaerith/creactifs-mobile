package be.creactifs.http

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform