package com.example.shoppeal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoppeal.AddTask
import com.example.shoppeal.R
import com.example.shoppeal.model.TaskListModel

class TaskListAdapter(taskList : List<TaskListModel>, internal val context: Context) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var taskList : List<TaskListModel> = ArrayList()
    init {
        this.taskList = taskList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_tasklist, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = taskList[position]
        holder.name.text = tasks.name
        holder.details.text = tasks.details
        holder.employeeId.text = tasks.id.toString()
        holder.phone.text = tasks.phone
        holder.cardView.setOnClickListener(View.OnClickListener {
            val i = Intent(context, AddTask::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", tasks.id)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        })
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.txt_name)
        val details : TextView = view.findViewById(R.id.txt_details)
        val cardView : CardView = view.findViewById(R.id.cardView)
        val employeeId : TextView = view.findViewById(R.id.employeeId)
        val phone : TextView = view.findViewById(R.id.phone)
    }
}