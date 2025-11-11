package org.example.app

import org.example.app.model.Task

/**
 * PUBLIC_INTERFACE
 * TaskRepository exposes CRUD operations backed by LocalDataStore.
 */
class TaskRepository(private val local: LocalDataStore) {

    // PUBLIC_INTERFACE
    suspend fun getTasks(): List<Task> = local.loadTasks()

    // PUBLIC_INTERFACE
    suspend fun addTask(task: Task): List<Task> {
        val curr = local.loadTasks().toMutableList()
        curr.add(task)
        local.saveTasks(curr)
        return curr
    }

    // PUBLIC_INTERFACE
    suspend fun updateTask(task: Task): List<Task> {
        val curr = local.loadTasks().toMutableList()
        val idx = curr.indexOfFirst { it.id == task.id }
        if (idx >= 0) {
            curr[idx] = task.copy(updatedAt = System.currentTimeMillis())
        }
        local.saveTasks(curr)
        return curr
    }

    // PUBLIC_INTERFACE
    suspend fun deleteTask(id: String): List<Task> {
        val curr = local.loadTasks().filter { it.id != id }
        local.saveTasks(curr)
        return curr
    }

    // PUBLIC_INTERFACE
    suspend fun toggleComplete(id: String): List<Task> {
        val curr = local.loadTasks().map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted, updatedAt = System.currentTimeMillis())
            else it
        }
        local.saveTasks(curr)
        return curr
    }
}
