package org.example.app

import org.example.app.model.Task

/**
 * PUBLIC_INTERFACE
 * TaskDataSource provides a minimal abstraction for loading and saving tasks.
 * This allows production LocalDataStore and test fakes to share the same contract.
 */
interface TaskDataSource {
    /**
     * Load all tasks from the data source.
     */
    suspend fun loadTasks(): List<Task>

    /**
     * Persist the provided list of tasks to the data source.
     */
    suspend fun saveTasks(tasks: List<Task>)
}
