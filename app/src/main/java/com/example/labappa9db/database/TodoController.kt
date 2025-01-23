package com.example.labappa9db.database

import android.content.ContentValues
import android.content.Context
import android.util.Log

/**
 * TodoController
 * provides the connection between the database and the todo data class
 * with functions to update, delete and add individual todos
 * as well as fetch todos with status unfinished (0) and finished (1)
 * @param context the Context from which the TodoController is created
 * @return null
 * */

class TodoController(context: Context) {
    private val dbHelper = DbHelper(context)

    fun addTodo(todo: todo): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("description", todo.description)
                put("deadline", todo.deadline)
                put("status", todo.status)
            }
            val result = db.insert("todos", null, values)
            result != -1L
        } catch (e: Exception) {
            Log.e("TodoController", "Insert failed", e)
            false
        } finally {
            db.close()
        }
    }

    fun updateTodo(todo: todo): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("description", todo.description)
                put("deadline", todo.deadline)
                put("status", todo.status)
            }
            val result = db.update("todos", values, "id = ?", arrayOf(todo.id.toString()))
            Log.d("TodoController", "Update result: $result, todo ID: ${todo.id}")
            result > 0
        } catch (e: Exception) {
            Log.e("TodoController", "Update failed", e)
            false
        } finally {
            db.close()
        }
    }

    fun deleteTodo(todoID: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val result = db.delete("todos", "id = ?", arrayOf(todoID.toString()))
            result > 0
        } catch (e: Exception) {
            Log.e("TodoController", "Delete failed", e)
            false
        } finally {
            db.close()
        }
    }

    fun getUnfinishedTodos(): List<todo> {
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<todo>()
        val cursor = db.rawQuery("SELECT * FROM todos WHERE status=0", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val todo = todo(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority")),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    )
                    todos.add(todo)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("TodoController", "Fetching todos failed", e)
        } finally {
            cursor.close()
            db.close()
        }
        return todos
    }

    fun getFinishedTodos(): List<todo>{
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<todo>()
        val cursor = db.rawQuery("SELECT * FROM todos WHERE status=1", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val todo = todo(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority")),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    )
                    todos.add(todo)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("TodoController", "Fetching todos failed", e)
        } finally {
            cursor.close()
            db.close()
        }
        return todos

    }
}