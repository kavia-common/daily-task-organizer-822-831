package org.example.app

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.app.model.Task
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repo: TaskRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_loads_tasks() = runTest(dispatcher) {
        val initial = listOf(Task(title = "T1"))
        coEvery { repo.getTasks() } returns initial

        val vm = TaskViewModel(repo)
        // Advance init block
        testScheduler.advanceUntilIdle()

        assertEquals(initial, vm.tasks.first())
        coVerify(exactly = 1) { repo.getTasks() }
    }

    @Test
    fun addTask_with_blank_title_sets_error_and_does_not_call_repo() = runTest(dispatcher) {
        coEvery { repo.getTasks() } returns emptyList()
        val vm = TaskViewModel(repo)
        testScheduler.advanceUntilIdle()

        vm.addTask("   ", "desc")
        // No repo call
        coVerify(exactly = 0) { repo.addTask(any()) }

        // error emitted then cleared by UI; ViewModel only sets error, not auto-clear
        assertEquals("Title cannot be empty", vm.error.first())
    }

    @Test
    fun add_update_toggle_delete_calls_repository_and_updates_state() = runTest(dispatcher) {
        // Arrange
        val existing = emptyList<Task>()
        coEvery { repo.getTasks() } returns existing

        val vm = TaskViewModel(repo)
        testScheduler.advanceUntilIdle()

        val t1 = Task(title = "Task A", description = null)
        val afterAdd = listOf(t1)
        coEvery { repo.addTask(any()) } returns afterAdd

        vm.addTask("Task A", null)
        testScheduler.advanceUntilIdle()
        assertEquals(afterAdd, vm.tasks.first())
        coVerify { repo.addTask(match { it.title == "Task A" && it.description == null }) }
        assertNull(vm.error.first())

        val t1Updated = t1.copy(title = "Task A+", updatedAt = System.currentTimeMillis() + 1000)
        val afterUpdate = listOf(t1Updated)
        coEvery { repo.updateTask(any()) } returns afterUpdate

        vm.updateTask(t1.id, "Task A+", "")
        testScheduler.advanceUntilIdle()
        assertEquals(afterUpdate, vm.tasks.first())
        coVerify { repo.updateTask(match { it.id == t1.id && it.title == "Task A+" && it.description == null }) }

        val toggled = afterUpdate.map { it.copy(isCompleted = !it.isCompleted) }
        coEvery { repo.toggleComplete(t1.id) } returns toggled

        vm.toggleComplete(t1.id)
        testScheduler.advanceUntilIdle()
        assertEquals(toggled, vm.tasks.first())
        coVerify { repo.toggleComplete(t1.id) }

        val afterDelete = emptyList<Task>()
        coEvery { repo.deleteTask(t1.id) } returns afterDelete

        vm.deleteTask(t1.id)
        testScheduler.advanceUntilIdle()
        assertEquals(afterDelete, vm.tasks.first())
        coVerify { repo.deleteTask(t1.id) }
    }

    @Test
    fun update_with_blank_title_sets_error() = runTest(dispatcher) {
        coEvery { repo.getTasks() } returns emptyList()
        val vm = TaskViewModel(repo)
        testScheduler.advanceUntilIdle()

        vm.updateTask("id", "   ", null)
        coVerify(exactly = 0) { repo.updateTask(any()) }
        assertEquals("Title cannot be empty", vm.error.first())
    }
}
