package org.example.app

import org.example.app.model.Task

/**
 * A test-only fake of LocalDataStore that stores tasks in-memory and provides the same API surface.
 * This allows repository and UI tests to run without Android SharedPreferences.
 */
open class LocalDataStoreFakeForTests : LocalDataStoreTestFacade() {
    private val storage = mutableListOf<Task>()

    override suspend fun loadTasks(): List<Task> = storage.toList()

    override suspend fun saveTasks(tasks: List<Task>) {
        storage.clear()
        storage.addAll(tasks)
    }
}

/**
 * Facade exposing only the methods used in production LocalDataStore.
 */
open class LocalDataStoreTestFacade {
    open suspend fun loadTasks(): List<Task> = emptyList()
    open suspend fun saveTasks(tasks: List<Task>) {}
}
