package com.example.shoppeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shoppeal.dataBase.DataBaseHelper
import com.example.shoppeal.model.TaskListModel

class AddTask : AppCompatActivity() {
    lateinit var name : EditText
    lateinit var details : EditText
    lateinit var save : Button
    lateinit var delete : Button
    var dbHandler : DataBaseHelper ?= null
    var isEditMode : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        name = findViewById(R.id.et_name)
        details = findViewById(R.id.et_details)
        save = findViewById(R.id.btn_save)
        delete = findViewById(R.id.btn_del)
        supportActionBar?.hide()

        dbHandler = DataBaseHelper(this)
        if (intent != null && intent.getStringExtra("Mode") == "E"){
            //update the data
            isEditMode = true
            save.text = "Update date"
            delete.visibility = View.VISIBLE
            val tasks : TaskListModel = dbHandler!!.getTask(intent.getIntExtra("Id", 0))
            name.setText(tasks.name)
            details.setText(tasks.details)

        }else{
            //add the data
            isEditMode = false
            save.text = "Save Data"
            delete.visibility = View.GONE
        }

        save.setOnClickListener(View.OnClickListener {
            var success : Boolean = false
            val taskListModel = TaskListModel()
            if (isEditMode){
                //Update
                taskListModel.id = intent.getIntExtra("Id", 0)
                taskListModel.name = name.text.toString()
                taskListModel.details = details.text.toString()
                success = dbHandler!!.updateTask(taskListModel) as Boolean
            }else{
                //insert
                taskListModel.name = name.text.toString()
                taskListModel.details = details.text.toString()
                success = dbHandler!!.addTask(taskListModel) as Boolean
            }
            if (success){
                startActivity(Intent(applicationContext, Employee::class.java))
                finish()
            }else{
                Toast.makeText(applicationContext, "Employee data is not inserted", Toast.LENGTH_SHORT).show()
            }
        })

        delete.setOnClickListener(View.OnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click Yes, if you want to delete the employee details").setPositiveButton("yes") { dialog, task ->
                val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                if (success)
                    finish()
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, task ->
                dialog.dismiss()
            }
            dialog.show()
        })
    }

    override fun onBackPressed() {
        super.finish()
    }
}