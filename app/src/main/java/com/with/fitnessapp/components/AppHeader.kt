package com.with.fitnessApp.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Correct import for back arrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String, 
    onBackClicked: (() -> Unit)? = null, 
    onEditClick: (() -> Unit)? = null, // Kept for simple cases
    actions: @Composable RowScope.() -> Unit = {} // New flexible actions parameter
) {
    // Determine if the default edit action should be shown
    // This is true if onEditClick is provided AND the caller has NOT provided custom actions.
    // We can check if the 'actions' lambda is the default empty one.
    // However, directly comparing lambdas for equality is tricky.
    // A more straightforward approach is to let the caller decide: 
    // if they want just edit, they pass onEditClick. If they want custom actions, they pass 'actions'.
    // For this iteration, if 'actions' is the default AND onEditClick is present, show Edit. Otherwise, show 'actions'.
    // A cleaner way could be to make 'actions' the primary way and remove onEditClick, 
    // forcing callers to explicitly define their action icons.
    // For now, to minimize breaking changes for PlansScreen, let's try to keep onEditClick functional as a default.

    val currentActions: @Composable RowScope.() -> Unit = if (actions == {} && onEditClick != null) {
        // If default actions lambda is used AND onEditClick is available, provide default Edit button
        { IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, contentDescription = "Edit") } }
    } else {
        // Otherwise, use the actions provided by the caller
        actions
    }

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
        actions = currentActions, // Use the determined actions
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
