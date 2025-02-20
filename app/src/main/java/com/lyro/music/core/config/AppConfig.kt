package com.lyro.music.core.config

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor() {
    var useMockData: Boolean = false  // Set default value
} 