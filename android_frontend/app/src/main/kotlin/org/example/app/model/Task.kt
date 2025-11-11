package org.example.app.model

import java.util.UUID

/**
 * PUBLIC_INTERFACE
 * Represents a to-do Task item.
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
