package com.josejordan.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val listener: OnTaskSelectedListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Interfaz para el evento de selección de tarea
    interface OnTaskSelectedListener {
        fun onTaskSelected(task: Task)
        fun onTaskSwiped(task: Task)

    }

    // Clase ViewHolder que representa cada elemento de la lista
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleEditText: EditText = view.findViewById(R.id.title_edit_text)
        val descriptionEditText: EditText = view.findViewById(R.id.description_edit_text)

        fun bind(task: Task) {
            titleEditText.setText(task.title)
            descriptionEditText.setText(task.description)
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
            if(!holder.titleEditText.isEnabled){
                holder.titleEditText.isEnabled = true
                holder.descriptionEditText.isEnabled = true
                holder.titleEditText.requestFocus()
            } else {
                holder.titleEditText.isEnabled = false
                holder.descriptionEditText.isEnabled = false
                task.title = holder.titleEditText.text.toString()
                task.description = holder.descriptionEditText.text.toString()
                updateTask(position, task)
            }
        }
    }

    // Método para agregar una nueva tarea al adaptador
    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
        sortTasks()
    }

    // Método para actualizar una tarea en una posición específica del adaptador
    fun updateTask(position: Int, task: Task) {
        tasks[position] = task
        notifyItemChanged(position)
        sortTasks()
    }

    // Método para eliminar una tarea en una posición específica del adaptador
    fun deleteTask(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
        sortTasks()
    }

    // Método para ordenar las tareas en el adaptador
    fun sortTasks() {
        tasks.sortWith(compareBy({ it.isCompleted }, { it.title }))
        notifyDataSetChanged()
    }
}
