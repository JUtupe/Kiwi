package pl.jutupe.theme.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.theme.ThemeDataStore

class ThemePickerViewModel(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    val currentTheme: Flow<KiwiTheme> = themeDataStore.currentThemeFlow

    fun getAllThemes(): List<KiwiTheme> = listOf(
        KiwiTheme.Dark,
        KiwiTheme.Light,
    )

    suspend fun onThemeClicked(theme: KiwiTheme) {
        themeDataStore.saveCurrentTheme(theme)
    }
}