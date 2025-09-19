package com.with.fitnessApp.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Define a stable, top-level default for the actions lambda
private val DefaultEmptyActions: @Composable RowScope.() -> Unit = {}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    onBackClicked: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = DefaultEmptyActions // Use the stable default
) {
    val currentActions: @Composable RowScope.() -> Unit = if (actions === DefaultEmptyActions && onEditClick != null) {
        // If the passed actions is the default empty one AND onEditClick is available, provide default Edit button
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
