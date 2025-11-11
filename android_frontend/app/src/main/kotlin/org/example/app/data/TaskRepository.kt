package org.example.app

import org.example.app.model.Task

/**
 * PUBLIC_INTERFACE
 * TaskRepository exposes CRUD operations backed by a TaskDataSource.
 */
open class TaskRepository(private val local: TaskDataSource) {

    // PUBLIC_INTERFACE
    open suspend fun getTasks(): List<Task> = local.loadTasks()

    // PUBLIC_INTERFACE
    open suspend fun addTask(task: Task): List<Task> {
        val curr = local.loadTasks().toMutableList()
        curr.add(task)
        local.saveTasks(curr)
        return curr
    }

    // PUBLIC_INTERFACE
    open suspend fun updateTask(task: Task): List<Task> {
        val curr = local.loadTasks().toMutableList()
        val idx = curr.indexOfFirst { it.id == task.id }
        if (idx >= 0) {
            curr[idx] = task.copy(updatedAt = System.currentTimeMillis())
        }
        local.saveTasks(curr)
        return curr
    }

    // PUBLIC_INTERFACE
    open suspend fun deleteTask(id: String): List<Task> {
        val curr = local.loadTasks().filter { it.id != id }
        local.saveTasks(curr)
        return curr
    }

    // PUBLIC_INTERFACE
    open suspend fun toggleComplete(id: String): List<Task> {
        val curr = local.loadTasks().map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted, updatedAt = System.currentTimeMillis())
            else it
        }
        local.saveTasks(curr)
        return curr
    }
}
