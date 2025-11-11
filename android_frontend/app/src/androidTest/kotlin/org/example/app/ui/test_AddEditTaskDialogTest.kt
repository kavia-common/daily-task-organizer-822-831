package org.example.app.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import org.example.app.AddEditTaskDialog
import org.example.app.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class AddEditTaskDialogTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun save_button_enabled_only_when_title_not_blank() {
        composeRule.setContent {
            AppTheme {
                val open = remember { mutableStateOf(true) }
                if (open.value) {
                    AddEditTaskDialog(
                        task = null,
                        onDismiss = { open.value = false },
                        onConfirm = { _, _ -> open.value = false }
                    )
                }
            }
        }

        // Initially disabled
        composeRule.onNodeWithText("Save").assertIsNotEnabled()

        // Enter title -> enabled
        composeRule.onNodeWithText("Title").performTextInput("New Task")
        composeRule.onNodeWithText("Save").assertIsEnabled().assertIsDisplayed()
    }
}
