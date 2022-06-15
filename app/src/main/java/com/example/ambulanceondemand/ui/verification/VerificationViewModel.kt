package com.example.ambulanceondemand.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ambulanceondemand.ui.landing.model.ModelPreference
import com.example.ambulanceondemand.util.UserPreference
import kotlinx.coroutines.launch

class VerificationViewModel(private val pref: UserPreference): ViewModel() {

    fun setVerification(user: ModelPreference) {
        viewModelScope.launch {
            pref.setVerification(user)
        }
    }

}