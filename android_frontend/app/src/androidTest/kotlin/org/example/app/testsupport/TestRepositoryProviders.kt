package org.example.app

import org.example.app.model.Task

/**
 * Adapter repository that uses LocalDataStoreAndroidFake to satisfy TaskRepository contract in androidTest.
 */
class TaskRepositoryAndroidTestAdapter(
    private val store: LocalDataStoreAndroidFake
) {
    suspend fun getTasks(): List<Task> = store.loadTasks()

    suspend fun addTask(task: Task): List<Task> {
        val curr = store.loadTasks().toMutableList()
        curr.add(task)
        store.saveTasks(curr)
        return curr
    }

    suspend fun updateTask(task: Task): List<Task> {
        val curr = store.loadTasks().toMutableList()
        val idx = curr.indexOfFirst { it.id == task.id }
        if (idx >= 0) curr[idx] = task.copy(updatedAt = System.currentTimeMillis())
        store.saveTasks(curr)
        return curr
    }

    suspend fun deleteTask(id: String): List<Task> {
        val curr = store.loadTasks().filter { it.id != id }
        store.saveTasks(curr)
        return curr
    }

    suspend fun toggleComplete(id: String): List<Task> {
        val curr = store.loadTasks().map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted, updatedAt = System.currentTimeMillis()) else it
        }
        store.saveTasks(curr)
        return curr
    }
}
