package net.oddware.gamepadmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform