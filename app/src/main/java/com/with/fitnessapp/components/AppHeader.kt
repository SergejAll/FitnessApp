package com.with.fitnessApp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Correct import for back arrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// Removed padding imports as they are not directly used on TopAppBar itself in this version.
// Padding if needed would be applied by the caller or to specific elements.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String, 
    onBackClicked: (() -> Unit)? = null, // Optional back click handler
    onEditClick: (() -> Unit)? = null // Optional edit click handler
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onBackClicked != null) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (onEditClick != null) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
