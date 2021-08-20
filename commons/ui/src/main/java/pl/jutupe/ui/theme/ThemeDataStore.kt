package pl.jutupe.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

class ThemeDataStore(context: Context) {

    private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(
        name = THEME_SHARED_PREFERENCES
    )

    private val themeDataStore = context.themeDataStore

    suspend fun saveCurrentTheme(theme: KiwiTheme) {
        Timber.d("saveCurrentTheme(theme=$theme)")

        themeDataStore.edit { preferences ->
            preferences[CURRENT_THEME_ID_KEY] = theme.id
        }
    }

    val currentThemeFlow: Flow<KiwiTheme> = themeDataStore.data
        .map { preferences ->
            when (preferences[CURRENT_THEME_ID_KEY]) {
                KiwiTheme.Light.id -> KiwiTheme.Light
                KiwiTheme.Dark.id -> KiwiTheme.Dark
                else -> throw IllegalArgumentException("Unsupported theme")
            }
        }
        .catch { KiwiTheme.Dark }

    companion object {
        private const val THEME_SHARED_PREFERENCES = "pl.jutupe.kiwi.theme_shared_preferences"
        private val CURRENT_THEME_ID_KEY = stringPreferencesKey("theme.current")
    }
}