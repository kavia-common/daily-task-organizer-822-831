package org.example.app

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.example.app.model.Task

/**
 * LocalDataStore persists tasks as a JSON array within SharedPreferences.
 * Uses Dispatchers.IO for blocking operations.
 */
class LocalDataStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // PUBLIC_INTERFACE
    suspend fun loadTasks(): List<Task> = withContext(Dispatchers.IO) {
        val json = prefs.getString(KEY_TASKS, "[]") ?: "[]"
        val array = JSONArray(json)
        val list = mutableListOf<Task>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(obj.toTask())
        }
        list
    }

    // PUBLIC_INTERFACE
    suspend fun saveTasks(tasks: List<Task>) = withContext(Dispatchers.IO) {
        val arr = JSONArray()
        tasks.forEach { arr.put(it.toJson()) }
        prefs.edit(commit = true) {
            putString(KEY_TASKS, arr.toString())
        }
    }

    private fun Task.toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("description", description)
            put("isCompleted", isCompleted)
            put("createdAt", createdAt)
            put("updatedAt", updatedAt)
        }
    }

    private fun JSONObject.toTask(): Task {
        return Task(
            id = optString("id"),
            title = optString("title"),
            description = if (has("description") && !isNull("description")) optString("description") else null,
            isCompleted = optBoolean("isCompleted", false),
            createdAt = optLong("createdAt", System.currentTimeMillis()),
            updatedAt = optLong("updatedAt", System.currentTimeMillis())
        )
    }

    companion object {
        private const val PREFS_NAME = "task_prefs"
        private const val KEY_TASKS = "tasks"
    }
}
