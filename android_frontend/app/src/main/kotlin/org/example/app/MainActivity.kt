package org.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.example.app.ui.theme.AppTheme

// PUBLIC_INTERFACE
class MainActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dataStore = LocalDataStore(this@MainActivity)
                val repo = TaskRepository(dataStore)
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repo) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
    }
}
