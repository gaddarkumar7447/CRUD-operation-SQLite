package com.example.shoppeal.dataBase

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.number.IntegerWidth
import androidx.core.content.contentValuesOf
import com.example.shoppeal.model.TaskListModel

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT, $TASK_PHONE TEXT, $TASK_COUNTRY TEXT, $TASK_CITY TEXT, $TASK_STATE TEXT, $TASK_PINCODE TEXT, $TASK_EMAIL TEXT, $TASK_BIRTHDAY TEXT, $TASK_GENDER TEXT);"
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
        private val TASK_PHONE = "taskPhone"
        private val TASK_COUNTRY = "taskCountry"
        private val TASK_CITY = "taskCity"
        private val TASK_STATE = "taskState"
        private val TASK_PINCODE = "taskPinCode"
        private val TASK_EMAIL = "taskEmail"
        private val TASK_BIRTHDAY = "taskBirthDay"
        private val TASK_GENDER = "taskGender"
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
                    tasks.phone = cursor.getString(cursor.getColumnIndex(TASK_PHONE))
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
        values.put(TASK_PHONE, task.phone)
        values.put(TASK_COUNTRY, task.country)
        values.put(TASK_CITY, task.city)
        values.put(TASK_STATE, task.state)
        values.put(TASK_PINCODE, task.pinCode)
        values.put(TASK_EMAIL, task.email)
        values.put(TASK_BIRTHDAY, task.birthday)
        values.put(TASK_GENDER, task.gender)
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
        task.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
        task.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
        task.phone = cursor.getString(cursor.getColumnIndex(TASK_PHONE))
        task.country = cursor.getString(cursor.getColumnIndex(TASK_COUNTRY))
        task.city = cursor.getString(cursor.getColumnIndex(TASK_CITY))
        task.state = cursor.getString(cursor.getColumnIndex(TASK_STATE))
        task.pinCode = cursor.getString(cursor.getColumnIndex(TASK_PINCODE))
        task.email = cursor.getString(cursor.getColumnIndex(TASK_EMAIL))
        task.birthday = cursor.getString(cursor.getColumnIndex(TASK_BIRTHDAY))
        task.gender = cursor.getString(cursor.getColumnIndex(TASK_GENDER))
        cursor.close()
        return task
    }

    fun deleteTask(id : Int):Boolean{
        val db = this.writableDatabase
        val success : Long = db.delete(TABLE_NAME, "$ID=?", arrayOf(id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun updateTask(tasks : TaskListModel) : Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TASK_NAME, tasks.name)
        contentValues.put(TASK_DETAILS, tasks.details)
        contentValues.put(TASK_PHONE, tasks.phone)
        contentValues.put(TASK_COUNTRY, tasks.country)
        contentValues.put(TASK_CITY, tasks.city)
        contentValues.put(TASK_STATE, tasks.state)
        contentValues.put(TASK_PINCODE, tasks.pinCode)
        contentValues.put(TASK_EMAIL, tasks.email)
        contentValues.put(TASK_BIRTHDAY, tasks.birthday)
        contentValues.put(TASK_GENDER, tasks.gender)
        val success = db.update(TABLE_NAME, contentValues, "$ID=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }
}

