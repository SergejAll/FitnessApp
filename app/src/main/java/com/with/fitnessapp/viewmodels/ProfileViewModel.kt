package com.with.fitnessApp.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.with.fitnessApp.data.ProfileData
import com.with.fitnessApp.data.ProfileDataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = ProfileDataStoreManager(application.applicationContext)

    // Private MutableStateFlows for UI state
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _birthday = MutableStateFlow("")
    val birthday: StateFlow<String> = _birthday.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>("") // Store as String
    val imageUri: StateFlow<String?> = _imageUri.asStateFlow()

    // To notify UI about save completion or errors, optional
    private val _saveStatus = MutableStateFlow<String?>(null)
    val saveStatus: StateFlow<String?> = _saveStatus.asStateFlow()

    init {
        loadInitialProfileData()
    }

    private fun loadInitialProfileData() {
        viewModelScope.launch {
            val profileData = dataStoreManager.profileDataFlow.first() // Get the first emitted value
            _name.value = profileData.name
            _username.value = profileData.username
            _email.value = profileData.email
            _birthday.value = profileData.birthday
            _imageUri.value = profileData.imageUri.ifEmpty { null } 
        }
    }

    // Functions to update the state from UI
    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updateBirthday(newBirthday: String) {
        _birthday.value = newBirthday
    }

    fun updateImageUri(newImageUri: Uri?) {
        _imageUri.value = newImageUri?.toString() ?: ""
    }
    
    fun clearSaveStatus(){
        _saveStatus.value = null
    }

    fun saveProfile() {
        viewModelScope.launch {
            val currentProfileData = ProfileData(
                name = _name.value,
                username = _username.value,
                email = _email.value,
                birthday = _birthday.value,
                imageUri = _imageUri.value ?: ""
            )
            dataStoreManager.saveProfileData(currentProfileData)
            _saveStatus.value = "Profile saved successfully!"
            // Optionally, you could re-load or confirm data is saved if needed,
            // but DataStore edit should be transactional.
        }
    }
}
