package com.github.turansky.yfiles.correction

import com.github.turansky.yfiles.GeneratorContext

internal fun generateFlagsUtils(context: GeneratorContext) {
    // language=kotlin
    context["yfiles.lang.Flags"] =
        """
            |@file:Suppress("NOTHING_TO_INLINE")
            |
            |package yfiles.lang
            |
            |external interface Flags<T>
            |        where T : Flags<T>,
            |              T : YEnum<T>
            |
            |inline infix fun <T> T.or(other: T): T
            |        where T : Flags<T>,
            |              T : YEnum<T> {
            |    return unsafeCast<Int>()
            |        .or(other.unsafeCast<Int>())
            |        .unsafeCast<T>()
            |}
            |
            |operator fun <T> T.contains(other: T): Boolean
            |        where T : Flags<T>,
            |              T : YEnum<T> {
            |    val t = unsafeCast<Int>()
            |    val o = other.unsafeCast<Int>()
            |    return (t and o) == o
            |}
        """.trimMargin()
}