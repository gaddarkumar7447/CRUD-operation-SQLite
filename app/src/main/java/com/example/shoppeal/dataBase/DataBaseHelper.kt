package com.example.shoppeal.dataBase

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.number.IntegerWidth
import androidx.core.content.contentValuesOf
import com.example.shoppeal.model.TaskListModel

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT);"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)

    }
    companion object{
        private val DB_NAME = "task"
        private val DB_VERSION = 1
        private val TABLE_NAME = "taskLite"
        private val ID = "id"
        private val TASK_NAME = "taskName"
        private val TASK_DETAILS = "taskDetails"
    }

    @SuppressLint("Range")
    fun getAllTask() : List<TaskListModel>{
        val taskList = ArrayList<TaskListModel>()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val tasks = TaskListModel()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
                    tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
                    taskList.add(tasks)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    fun addTask(task : TaskListModel) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, task.name)
        values.put(TASK_DETAILS, task.details)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }
    // select the data for particular id
    @SuppressLint("Range")
    fun getTask(id : Int) : TaskListModel{
        val task = TaskListModel()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(selectQuery,  null)
        cursor?.moveToFirst()
        task.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        task.name = cursor.getString(cursor.getColumnIndex(TABLE_NAME))
        task.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
        cursor.close()
        return task
    }

    fun deleteTask(id : Int):Boolean{
        val db = this.writableDatabase
        val success : Long = db.delete(TABLE_NAME, ID + "=?", arrayOf(id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun updateTask(tasks : TaskListModel) : Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TASK_NAME, tasks.name)
        contentValues.put(TASK_DETAILS, tasks.details)
        val success = db.update(TABLE_NAME, contentValues, ID + "=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

}

