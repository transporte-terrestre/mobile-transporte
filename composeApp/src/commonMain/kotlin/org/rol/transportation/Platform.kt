package org.rol.transportation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform