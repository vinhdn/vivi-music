/*
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.music.vivi.lyrics

import com.atilika.kuromoji.ipadic.Tokenizer

/**
 * Full edition: tokenizes Japanese text with Kuromoji and returns the katakana
 * reading for each token (falling back to the surface form when no reading is
 * available), ready to be converted to romaji by [LyricsUtils.katakanaToRomaji].
 */
internal object JapaneseTokenizer {

    private val tokenizer: Tokenizer by lazy { Tokenizer() }

    fun tokenizeReadings(text: String): List<String> =
        tokenizer.tokenize(text).map { token ->
            token.reading?.takeIf { it.isNotEmpty() && it != "*" } ?: token.surface
        }
}
