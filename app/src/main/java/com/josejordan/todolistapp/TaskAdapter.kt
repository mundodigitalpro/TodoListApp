package com.josejordan.todolistapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: MutableList<Task>, private val listener: OnTaskSelectedListener): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Interfaz para el evento de selección de tarea
    interface OnTaskSelectedListener {
        fun onTaskSelected(task: Task)
        fun onTaskChecked(task: Task)
    }

    // Clase ViewHolder que representa cada elemento de la lista
    class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.title_text_view)
        private val descriptionTextView: TextView = view.findViewById(R.id.description_text_view)
        private val completedCheckBox: CheckBox = view.findViewById(R.id.task_completed_check_box)

        // Método para vincular una tarea a las vistas del ViewHolder
/*        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            completedCheckBox.isChecked = task.isCompleted

            // Manejar el evento de cambio de estado del CheckBox
            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                if (isChecked) {
                    titleTextView.setTextColor(Color.GRAY)
                    descriptionTextView.setTextColor(Color.GRAY)
                } else {
                    titleTextView.setTextColor(Color.BLACK)
                    descriptionTextView.setTextColor(Color.BLACK)
                }
            }
        }*/
        fun bind(task: Task, listener: OnTaskSelectedListener) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            completedCheckBox.isChecked = task.isCompleted

            // Actualizar el listener luego de establecer el estado checked inicial
            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                if (isChecked) {
                    listener.onTaskChecked(task)
                }
            }
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
        holder.bind(task, listener)
        holder.itemView.setOnLongClickListener {
            listener. onTaskSelected(task)
            true
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
