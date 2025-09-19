package com.with.fitnessApp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define a data class to hold all profile information
data class ProfileData(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val birthday: String = "",
    val imageUri: String = "" // Store URI as String
)

class ProfileDataStoreManager(private val context: Context) {

    // Create the DataStore instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("profile_prefs")
        
        // Define Keys for each preference
        val NAME_KEY = stringPreferencesKey("profile_name")
        val USERNAME_KEY = stringPreferencesKey("profile_username")
        val EMAIL_KEY = stringPreferencesKey("profile_email")
        val BIRTHDAY_KEY = stringPreferencesKey("profile_birthday")
        val IMAGE_URI_KEY = stringPreferencesKey("profile_image_uri")
    }

    // Flow to read all profile data
    val profileDataFlow: Flow<ProfileData> = context.dataStore.data
        .map {
            preferences ->
            ProfileData(
                name = preferences[NAME_KEY] ?: "",
                username = preferences[USERNAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                birthday = preferences[BIRTHDAY_KEY] ?: "",
                imageUri = preferences[IMAGE_URI_KEY] ?: ""
            )
        }

    // Function to save a specific string preference
    private suspend fun saveStringPreference(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit {
            settings ->
            settings[key] = value
        }
    }

    // Public functions to save each profile field
    suspend fun saveName(name: String) {
        saveStringPreference(NAME_KEY, name)
    }

    suspend fun saveUsername(username: String) {
        saveStringPreference(USERNAME_KEY, username)
    }

    suspend fun saveEmail(email: String) {
        saveStringPreference(EMAIL_KEY, email)
    }

    suspend fun saveBirthday(birthday: String) {
        saveStringPreference(BIRTHDAY_KEY, birthday)
    }

    suspend fun saveImageUri(imageUri: String) {
        saveStringPreference(IMAGE_URI_KEY, imageUri)
    }

    // Function to save all profile data at once (useful for a "Save" button)
    suspend fun saveProfileData(profileData: ProfileData) {
        context.dataStore.edit {
            settings ->
            settings[NAME_KEY] = profileData.name
            settings[USERNAME_KEY] = profileData.username
            settings[EMAIL_KEY] = profileData.email
            settings[BIRTHDAY_KEY] = profileData.birthday
            settings[IMAGE_URI_KEY] = profileData.imageUri
        }
    }
}
