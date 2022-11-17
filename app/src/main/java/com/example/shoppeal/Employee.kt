package com.example.shoppeal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppeal.adapter.TaskListAdapter
import com.example.shoppeal.dataBase.DataBaseHelper
import com.example.shoppeal.model.TaskListModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Employee : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var btn_add1 : FloatingActionButton
    var taskListAdapter : TaskListAdapter ?= null
    var dbHandler : DataBaseHelper ?= null
    var taskList : List<TaskListModel> = ArrayList<TaskListModel>()
    var linearLayoutManager : LinearLayoutManager ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "All employee details"
        setContentView(R.layout.activity_employee)
        recyclerView = findViewById(R.id.rv_list)
        btn_add1 = findViewById(R.id.ft_add_item)

        dbHandler = DataBaseHelper(this)
        btn_add1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, AddTask::class.java))
        })
        fetchList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchList(){
        taskList = dbHandler!!.getAllTask()
        taskListAdapter = TaskListAdapter(taskList, applicationContext)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = taskListAdapter
        taskListAdapter?.notifyDataSetChanged()
    }
}