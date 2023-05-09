package org.d3if3016.assesment1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "preference"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

class SettingDataStore(prefDatastore: DataStore<Preferences>) {
    private val IS_GRID_LAYOUT = booleanPreferencesKey("is_grid_layout")

    val preferenceFlow: Flow<Boolean> = prefDatastore.data
        .catch { emit(emptyPreferences()) }
        .map { it[IS_GRID_LAYOUT] ?: true }

    suspend fun saveLayout(context: Context, isGridLayout: Boolean) {
        context.dataStore.edit { it[IS_GRID_LAYOUT] = isGridLayout }
    }
}