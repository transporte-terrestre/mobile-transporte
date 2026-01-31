package org.rol.transportation.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.crossfade
import coil3.util.DebugLogger
import org.koin.dsl.module

val imageModule = module {
    single {
        ImageLoader.Builder(get<PlatformContext>())
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }
}