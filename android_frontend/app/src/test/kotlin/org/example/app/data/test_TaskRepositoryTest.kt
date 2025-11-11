package org.example.app

import kotlinx.coroutines.runBlocking
import org.example.app.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests TaskRepository CRUD operations and persistence interactions using a fake LocalDataStore.
 */
class TaskRepositoryTest {

    private class FakeLocalDataStore : LocalDataStoreStub() {
        private val storage = mutableListOf<Task>()
        override suspend fun loadTasks(): List<Task> = storage.toList()
        override suspend fun saveTasks(tasks: List<Task>) { 
            storage.clear()
            storage.addAll(tasks)
        }
    }

    @Test
    fun add_update_toggle_delete_flow() = runBlocking {
        val fake = FakeLocalDataStore()
        val repo = TaskRepository(fake)

        // Initially empty
        assertTrue(repo.getTasks().isEmpty())

        // Add
        val t1 = Task(title = "Buy milk", description = "2L")
        val listAfterAdd = repo.addTask(t1)
        assertEquals(1, listAfterAdd.size)
        assertEquals("Buy milk", listAfterAdd.first().title)

        // Update
        val updated = t1.copy(title = "Buy oat milk")
        val listAfterUpdate = repo.updateTask(updated)
        assertEquals(1, listAfterUpdate.size)
        assertEquals("Buy oat milk", listAfterUpdate.first().title)
        // updatedAt should be updated (greater or equal); allow equality due to fast execution
        assertTrue(listAfterUpdate.first().updatedAt >= updated.updatedAt)

        // Toggle complete
        val listAfterToggle = repo.toggleComplete(t1.id)
        assertTrue(listAfterToggle.first().isCompleted)

        // Delete
        val listAfterDelete = repo.deleteTask(t1.id)
        assertTrue(listAfterDelete.isEmpty())
    }

    @Test
    fun update_nonexistent_task_does_not_crash_and_persists_existing() = runBlocking {
        val fake = FakeLocalDataStore()
        val repo = TaskRepository(fake)
        val t1 = Task(title = "A")
        repo.addTask(t1)

        val list = repo.updateTask(Task(id = "missing", title = "X"))
        assertEquals(1, list.size)
        assertEquals("A", list.first().title)
        assertFalse(list.first().isCompleted)
    }
}

/**
 * LocalDataStoreStub exposes only methods required by repository to facilitate JVM unit tests
 * without bringing Android framework. Real LocalDataStore is Android-specific; this stub allows
 * us to provide a fake in unit tests.
 */
open class LocalDataStoreStub {
    open suspend fun loadTasks(): List<Task> = emptyList()
    open suspend fun saveTasks(tasks: List<Task>) {}
}
