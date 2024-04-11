package com.mfitrahrmd.githubuser.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun GithubUserTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        lightColorScheme(
//            primary = colorResource(id = R.color.primary_light),
//            onPrimary = colorResource(id = R.color.md_theme_light_onPrimary),
//            primaryContainer = colorResource(id = R.color.md_theme_light_primaryContainer),
//            onPrimaryContainer = colorResource(id = R.color.md_theme_light_onPrimaryContainer),
//            secondary = colorResource(id = R.color.md_theme_light_secondary),
//            onSecondary = colorResource(id = R.color.md_theme_light_onSecondary),
//            secondaryContainer = colorResource(id = R.color.md_theme_light_secondaryContainer),
//            onSecondaryContainer = colorResource(id = R.color.md_theme_light_onSecondaryContainer),
//            tertiary = colorResource(id = R.color.md_theme_light_tertiary),
//            onTertiary = colorResource(id = R.color.md_theme_light_onTertiary),
//            tertiaryContainer = colorResource(id = R.color.md_theme_light_tertiaryContainer),
//            onTertiaryContainer = colorResource(id = R.color.md_theme_light_onTertiaryContainer),
//            error = colorResource(id = R.color.md_theme_light_error),
//            onError = colorResource(id = R.color.md_theme_light_onError),
//            errorContainer = colorResource(id = R.color.md_theme_light_errorContainer),
//            onErrorContainer = colorResource(id = R.color.md_theme_light_onErrorContainer),
//            outline = colorResource(id = R.color.md_theme_light_outline),
//            background = colorResource(id = R.color.md_theme_light_background),
//            onBackground = colorResource(id = R.color.md_theme_light_onBackground),
//            surface = colorResource(id = R.color.md_theme_light_surface),
//            onSurface = colorResource(id = R.color.md_theme_light_onSurface),
//            surfaceVariant = colorResource(id = R.color.md_theme_light_surfaceVariant),
//            onSurfaceVariant = colorResource(id = R.color.md_theme_light_onSurfaceVariant),
//            inverseSurface = colorResource(id = R.color.md_theme_light_inverseSurface),
//            inverseOnSurface = colorResource(id = R.color.md_theme_light_inverseOnSurface),
//            inversePrimary = colorResource(id = R.color.md_theme_light_inversePrimary),
//            surfaceTint = colorResource(id = R.color.md_theme_light_surfaceTint),
//            outlineVariant = colorResource(id = R.color.md_theme_light_outlineVariant),
//            scrim = colorResource(id = R.color.md_theme_light_scrim),
        )
    } else {
        darkColorScheme(
//            primary = colorResource(id = R.color.primary_dark),
//            onPrimary = colorResource(id = R.color.md_theme_dark_onPrimary),
//            primaryContainer = colorResource(id = R.color.md_theme_dark_primaryContainer),
//            onPrimaryContainer = colorResource(id = R.color.md_theme_dark_onPrimaryContainer),
//            secondary = colorResource(id = R.color.md_theme_dark_secondary),
//            onSecondary = colorResource(id = R.color.md_theme_dark_onSecondary),
//            secondaryContainer = colorResource(id = R.color.md_theme_dark_secondaryContainer),
//            onSecondaryContainer = colorResource(id = R.color.md_theme_dark_onSecondaryContainer),
//            tertiary = colorResource(id = R.color.md_theme_dark_tertiary),
//            onTertiary = colorResource(id = R.color.md_theme_dark_onTertiary),
//            tertiaryContainer = colorResource(id = R.color.md_theme_dark_tertiaryContainer),
//            onTertiaryContainer = colorResource(id = R.color.md_theme_dark_onTertiaryContainer),
//            error = colorResource(id = R.color.md_theme_dark_error),
//            onError = colorResource(id = R.color.md_theme_dark_onError),
//            errorContainer = colorResource(id = R.color.md_theme_dark_errorContainer),
//            onErrorContainer = colorResource(id = R.color.md_theme_dark_onErrorContainer),
//            outline = colorResource(id = R.color.md_theme_dark_outline),
//            background = colorResource(id = R.color.md_theme_dark_background),
//            onBackground = colorResource(id = R.color.md_theme_dark_onBackground),
//            surface = colorResource(id = R.color.md_theme_dark_surface),
//            onSurface = colorResource(id = R.color.md_theme_dark_onSurface),
//            surfaceVariant = colorResource(id = R.color.md_theme_dark_surfaceVariant),
//            onSurfaceVariant = colorResource(id = R.color.md_theme_dark_onSurfaceVariant),
//            inverseSurface = colorResource(id = R.color.md_theme_dark_inverseSurface),
//            inverseOnSurface = colorResource(id = R.color.md_theme_dark_inverseOnSurface),
//            inversePrimary = colorResource(id = R.color.md_theme_dark_inversePrimary),
//            surfaceTint = colorResource(id = R.color.md_theme_dark_surfaceTint),
//            outlineVariant = colorResource(id = R.color.md_theme_dark_outlineVariant),
//            scrim = colorResource(id = R.color.md_theme_dark_scrim),
        )
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}