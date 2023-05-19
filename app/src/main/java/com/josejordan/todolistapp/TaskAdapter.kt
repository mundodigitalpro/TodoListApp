package com.josejordan.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    var tasks: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
    }

    // Clase ViewHolder que representa cada elemento de la lista
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleEditText: EditText = view.findViewById(R.id.title_edit_text)

        fun bind(task: Task) {
            titleEditText.setText(task.title)
        }
    }

    // Crear una instancia del ViewHolder inflando el diseño del elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TaskViewHolder(view)
    }

    // Obtener el número de elementos en la lista
    override fun getItemCount(): Int {
        return tasks.size
    }

    // Vincular datos de una tarea específica al ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

        holder.itemView.setOnClickListener {
            if (!holder.titleEditText.isEnabled) {
                holder.titleEditText.isEnabled = true
                holder.titleEditText.requestFocus()
            } else {
                holder.titleEditText.isEnabled = false
                task.title = holder.titleEditText.text.toString()
                onTaskClickListener.onTaskClick(task)
            }
        }
    }

    fun sortTasks() {
        tasks.sortWith(compareBy { it.title })
        notifyDataSetChanged()
    }
}
