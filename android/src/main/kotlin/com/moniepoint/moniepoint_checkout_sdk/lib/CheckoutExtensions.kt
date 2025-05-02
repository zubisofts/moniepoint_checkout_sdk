package com.moniepoint.moniepoint_checkout_sdk.lib

import CkoDesignToken
import CkoFont
import com.checkout.components.interfaces.uicustomisation.BorderRadius
import com.checkout.components.interfaces.uicustomisation.designtoken.ColorTokens
import com.checkout.components.interfaces.uicustomisation.designtoken.DesignTokens
import com.checkout.components.interfaces.uicustomisation.font.Font
import com.checkout.components.interfaces.uicustomisation.font.FontFamily
import com.checkout.components.interfaces.uicustomisation.font.FontName
import com.checkout.components.interfaces.uicustomisation.font.FontStyle
import com.checkout.components.interfaces.uicustomisation.font.FontWeight

fun CkoDesignToken.toDesignTokens(): DesignTokens {
    val colorTokens = ColorTokens(
        colorAction = colorTokens?.colorAction ?: ColorTokens().colorAction,
        colorBackground = colorTokens?.colorBackground ?: ColorTokens().colorBackground,
        colorBorder = colorTokens?.colorBorder ?: ColorTokens().colorBorder,
        colorDisabled = colorTokens?.colorDisabled ?: ColorTokens().colorDisabled,
        colorPrimary = colorTokens?.colorPrimary ?: ColorTokens().colorPrimary,
        colorFormBackground = colorTokens?.colorFormBackground ?: ColorTokens().colorFormBackground,
        colorFormBorder = colorTokens?.colorFormBorder ?: ColorTokens().colorFormBorder,
        colorInverse = colorTokens?.colorInverse ?: ColorTokens().colorInverse,
        colorOutline = colorTokens?.colorOutline ?: ColorTokens().colorOutline,
        colorSecondary = colorTokens?.colorSecondary ?: ColorTokens().colorSecondary,
        colorSuccess = colorTokens?.colorSuccess ?: ColorTokens().colorSuccess,
        colorError = colorTokens?.colorError ?: ColorTokens().colorError,
        colorScrolledContainer = colorTokens?.colorScrolledContainer
            ?: ColorTokens().colorScrolledContainer
    )

    val fontMap = fonts?.mapNotNull { (key, font) ->
        val fontName = when (key) {
            "Subheading" -> FontName.Subheading
            "Input" -> FontName.Input
            "Button" -> FontName.Button
            "Label" -> FontName.Label
            else -> null
        } ?: return@mapNotNull null

        fontName to font.toFont()
    }?.toMap() ?: emptyMap()

    return DesignTokens(
        colorTokens = colorTokens,
        borderButtonRadius = BorderRadius(all = borderRadius?.all?.toInt() ?: 0),
        borderFormRadius = BorderRadius(all = borderFormRadius?.all?.toInt() ?: 0),
        fonts = fontMap,
    )
}

fun CkoFont.toFont(): Font {
    val fontStyle = when (fontStyle?.lowercase()) {
        "italic" -> FontStyle.Italic
        "normal" -> FontStyle.Normal
        else -> FontStyle.Normal
    }

    val fontWeight = when (fontWeight?.lowercase()) {
        "light" -> FontWeight.Light
        "normal" -> FontWeight.Normal
        "bold" -> FontWeight.Bold
        "extrabold" -> FontWeight.ExtraBold
        else -> FontWeight.Normal
    }

    val fontFamily = when (fontFamily?.lowercase()) {
        "monospace" -> FontFamily.Monospace
        "sansserif" -> FontFamily.SansSerif
        "serif" -> FontFamily.Serif
        "cursive" -> FontFamily.Cursive
        else -> FontFamily.Default
    }

    return Font(
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing?.toInt(),
        lineHeight = lineHeight?.toInt()
    )
}
