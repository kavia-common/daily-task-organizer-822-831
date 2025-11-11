package org.example.app.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.example.app.TaskListScreen
import org.example.app.TaskViewModel
import org.example.app.model.Task
import org.example.app.ui.theme.AppTheme
import org.example.app.LocalDataStoreAndroidFake
import org.example.app.TaskRepositoryAndroidTestAdapter
import org.junit.Rule
import org.junit.Test

class TaskListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun empty_state_then_add_edit_delete_flow() {
        val store = LocalDataStoreAndroidFake()
        val repoAdapter = TaskRepositoryAndroidTestAdapter(store)

        composeRule.setContent {
            AppTheme {
                // Bridge adapter into the ViewModel by wrapping through a minimal inline repository interface
                // For UI flows here, we'll simply simulate initial empty state and rely on repository calls inside VM
                val vm = object : TaskViewModel(
                    repository = object : org.example.app.TaskRepository(
                        // Dummy LocalDataStore that delegates to our fake via adapter
                        object : org.example.app.LocalDataStore(this@TaskListScreenTest.composeRule.activity) {
                            // Not used; we'll override VM methods to delegate through adapter as needed
                        }
                    ) {}
                ) {}
                // However, the above inheritance is heavy; simpler: use production VM and intercept calls via a Proxy repo is complex in this source set.
                // Instead, render UI and interact; state will be empty by default and our interactions will close dialogs; we assert basic UI visibility.

                TaskListScreen(viewModel = TaskViewModel(object : org.example.app.TaskRepository(
                    object : org.example.app.LocalDataStore(this@TaskListScreenTest.composeRule.activity) {}
                ) {
                    // Override to delegate to adapter
                    override suspend fun getTasks(): List<Task> = repoAdapter.getTasks()
                    override suspend fun addTask(task: Task): List<Task> = repoAdapter.addTask(task)
                    override suspend fun updateTask(task: Task): List<Task> = repoAdapter.updateTask(task)
                    override suspend fun deleteTask(id: String): List<Task> = repoAdapter.deleteTask(id)
                    override suspend fun toggleComplete(id: String): List<Task> = repoAdapter.toggleComplete(id)
                }))
            }
        }

        // Empty state visible
        composeRule.onNodeWithText("No tasks yet").assertIsDisplayed()
        composeRule.onNodeWithText("Tap + to add your first task").assertIsDisplayed()

        // Click FAB to add
        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Add Task").assertIsDisplayed()

        // Enter title and save
        composeRule.onAllNodes(hasText("Title")).assertCountEquals(1)
        composeRule.onNodeWithText("Title").performTextInput("Read book")
        composeRule.onNodeWithText("Save").assertIsEnabled().performClick()

        // New item appears
        composeRule.onNodeWithText("Read book").assertIsDisplayed()

        // Edit via icon
        composeRule.onNodeWithContentDescription("Edit").performClick()
        composeRule.onNodeWithText("Edit Task").assertIsDisplayed()
        composeRule.onNodeWithText("Title").performTextClearance()
        composeRule.onNodeWithText("Title").performTextInput("Read Kotlin book")
        composeRule.onNodeWithText("Save").performClick()
        composeRule.onNodeWithText("Read Kotlin book").assertIsDisplayed()

        // Delete
        composeRule.onNodeWithContentDescription("Delete").performClick()
        // Item removed (empty again)
        composeRule.onNodeWithText("No tasks yet").assertIsDisplayed()
    }
}
