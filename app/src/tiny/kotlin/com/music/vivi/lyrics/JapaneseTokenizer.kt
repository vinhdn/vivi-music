/*
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.music.vivi.lyrics

/**
 * Tiny edition: Kuromoji and its ~33 MB dictionary are not bundled. Japanese
 * romanization is unavailable, so this returns no readings and callers fall back
 * to the original Japanese text.
 */
internal object JapaneseTokenizer {

    fun tokenizeReadings(text: String): List<String> = emptyList()
}
