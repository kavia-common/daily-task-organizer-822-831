package org.example.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.app.model.Task

/**
 * PUBLIC_INTERFACE
 * ViewModel managing the list of tasks and interactions with repository.
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            _tasks.value = repository.getTasks()
        }
    }

    // PUBLIC_INTERFACE
    fun addTask(title: String, description: String?) {
        if (title.isBlank()) {
            _error.value = "Title cannot be empty"
            return
        }
        viewModelScope.launch {
            val newTask = Task(title = title.trim(), description = description?.trim().takeUnless { it.isNullOrBlank() })
            _tasks.value = repository.addTask(newTask)
            _error.value = null
        }
    }

    // PUBLIC_INTERFACE
    fun updateTask(id: String, title: String, description: String?) {
        if (title.isBlank()) {
            _error.value = "Title cannot be empty"
            return
        }
        viewModelScope.launch {
            val existing = _tasks.value.firstOrNull { it.id == id } ?: return@launch
            val updated = existing.copy(
                title = title.trim(),
                description = description?.trim().takeUnless { it.isNullOrBlank() }
            )
            _tasks.value = repository.updateTask(updated)
            _error.value = null
        }
    }

    // PUBLIC_INTERFACE
    fun toggleComplete(id: String) {
        viewModelScope.launch {
            _tasks.value = repository.toggleComplete(id)
        }
    }

    // PUBLIC_INTERFACE
    fun deleteTask(id: String) {
        viewModelScope.launch {
            _tasks.value = repository.deleteTask(id)
        }
    }

    // PUBLIC_INTERFACE
    fun dismissError() {
        _error.value = null
    }
}
