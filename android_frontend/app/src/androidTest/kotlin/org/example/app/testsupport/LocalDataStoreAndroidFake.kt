package org.example.app

import org.example.app.model.Task

/**
 * AndroidTest fake implementation of LocalDataStore API that keeps data in memory.
 * This avoids SharedPreferences usage in instrumented UI tests.
 */
class LocalDataStoreAndroidFake {
    private val storage = mutableListOf<Task>()

    // PUBLIC_INTERFACE
    suspend fun loadTasks(): List<Task> = storage.toList()

    // PUBLIC_INTERFACE
    suspend fun saveTasks(tasks: List<Task>) {
        storage.clear()
        storage.addAll(tasks)
    }
}
