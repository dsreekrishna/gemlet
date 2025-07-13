
package com.mobiletracker.app.ui.invitations

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobiletracker.app.data.models.Invitation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitationsActivity : ComponentActivity() {
    private val viewModel: InvitationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvitationsScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationsScreen(viewModel: InvitationsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadInvitations()
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invitations") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Received") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Sent") }
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTab) {
                    0 -> InvitationsList(
                        invitations = uiState.receivedInvitations,
                        showActions = true,
                        onAccept = { viewModel.acceptInvitation(it) },
                        onReject = { viewModel.rejectInvitation(it) }
                    )
                    1 -> InvitationsList(
                        invitations = uiState.sentInvitations,
                        showActions = false
                    )
                }
            }
        }
    }
}

@Composable
fun InvitationsList(
    invitations: List<Invitation>,
    showActions: Boolean,
    onAccept: (Int) -> Unit = {},
    onReject: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(invitations) { invitation ->
            InvitationItem(
                invitation = invitation,
                showActions = showActions,
                onAccept = { onAccept(invitation.id) },
                onReject = { onReject(invitation.id) }
            )
        }
    }
}

@Composable
fun InvitationItem(
    invitation: Invitation,
    showActions: Boolean,
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = invitation.group_name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "From: ${invitation.receiver_name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${invitation.status}",
                style = MaterialTheme.typography.bodySmall
            )
            
            if (showActions && invitation.status == "PENDING") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Accept")
                    }
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reject")
                    }
                }
            }
        }
    }
}
