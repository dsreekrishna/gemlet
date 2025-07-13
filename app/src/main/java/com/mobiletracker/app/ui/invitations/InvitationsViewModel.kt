
package com.mobiletracker.app.ui.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiletracker.app.data.api.ApiService
import com.mobiletracker.app.data.models.Invitation
import com.mobiletracker.app.data.models.InvitationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InvitationsUiState(
    val receivedInvitations: List<Invitation> = emptyList(),
    val sentInvitations: List<Invitation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationsUiState())
    val uiState: StateFlow<InvitationsUiState> = _uiState.asStateFlow()

    fun loadInvitations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val receivedResponse = apiService.getReceivedInvitations()
                val sentResponse = apiService.getSentInvitations()
                
                if (receivedResponse.isSuccessful && sentResponse.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        receivedInvitations = receivedResponse.body() ?: emptyList(),
                        sentInvitations = sentResponse.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to load invitations",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error occurred",
                    isLoading = false
                )
            }
        }
    }

    fun acceptInvitation(invitationId: Int) {
        handleInvitationAction(invitationId, "ACCEPTED")
    }

    fun rejectInvitation(invitationId: Int) {
        handleInvitationAction(invitationId, "REJECTED")
    }

    private fun handleInvitationAction(invitationId: Int, status: String) {
        viewModelScope.launch {
            try {
                val response = apiService.handleInvitationAction(
                    InvitationAction(invitationId, status)
                )
                if (response.isSuccessful) {
                    loadInvitations() // Reload invitations after action
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to update invitation"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to update invitation"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
