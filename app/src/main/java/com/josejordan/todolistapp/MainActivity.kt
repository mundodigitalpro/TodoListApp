package com.josejordan.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskSelectedListener {

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private var selectedTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a las vistas del layout
        val titleEditText: EditText = findViewById(R.id.title_edit_text)
        val descriptionEditText: EditText = findViewById(R.id.description_edit_text)
        val addButton: Button = findViewById(R.id.add_button)
        val updateButton: Button = findViewById(R.id.update_button)
        val deleteButton: Button = findViewById(R.id.delete_button)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        // Configurar el LinearLayoutManager y el adaptador del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(tasks, this)
        recyclerView.adapter = taskAdapter

        // Código para ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // No nos importa el movimiento
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = tasks[position]
                onTaskChecked(task)

                // Crear y mostrar un Snackbar
                val snackbar = Snackbar.make(findViewById(R.id.recycler_view), "Tarea terminada", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
        // Fin del código para ItemTouchHelper


        // Agregar tareas de ejemplo al RecyclerView
        val exampleTasks = listOf(
            Task("Buy groceries", "Milk, eggs, bread"),
            Task("Study for test", "Chapters 4-6"),
            Task("Call mom", "Pick up the phone"),
            Task("Clean the house", "Living room, kitchen, bathroom")
        )
        tasks.addAll(exampleTasks)
        taskAdapter.notifyDataSetChanged()

        addButton.setOnClickListener {
            // Obtener el título y la descripción de la tarea ingresados por el usuario
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            // Crear una nueva tarea con el título y la descripción
            val task = Task(title, description)
            // Agregar la tarea al adaptador y actualizar la lista
            taskAdapter.addTask(task)
        }

        updateButton.setOnClickListener {
            // Obtener el título y la descripción de la tarea ingresados por el usuario
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            selectedTask?.let { oldTask ->
                // Mostrar un cuadro de diálogo de confirmación para actualizar la tarea
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Crear una nueva tarea con el título y la descripción actualizados
                        val newTask = Task(title, description)
                        // Obtener la posición de la tarea antigua en la lista de tareas
                        val position = tasks.indexOf(oldTask)
                        if (position != -1) {
                            // Actualizar la tarea en el adaptador y seleccionar la nueva tarea
                            taskAdapter.updateTask(position, newTask)
                            selectedTask = newTask
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        deleteButton.setOnClickListener {
            selectedTask?.let { task ->
                // Mostrar un cuadro de diálogo de confirmación para eliminar la tarea
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Obtener la posición de la tarea a eliminar en la lista de tareas
                        val position = tasks.indexOf(task)
                        if (position != -1) {
                            // Eliminar la tarea del adaptador y deseleccionar la tarea seleccionada
                            taskAdapter.deleteTask(position)
                            selectedTask = null
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    // Método de devolución de llamada cuando se selecciona una tarea en el adaptador
    override fun onTaskSelected(task: Task) {
        selectedTask = task
        val titleEditText: EditText = findViewById(R.id.title_edit_text)
        val descriptionEditText: EditText = findViewById(R.id.description_edit_text)
        // Establecer el título y la descripción de la tarea seleccionada en los campos de texto
        titleEditText.setText(task.title)
        descriptionEditText.setText(task.description)
        // Ordenar las tareas en el adaptador
        taskAdapter.sortTasks()
    }
    override fun onTaskChecked(task: Task) {
        task.isCompleted = true
        val position = tasks.indexOf(task)
        if (position != -1) {
            taskAdapter.deleteTask(position)
            selectedTask = null
            // Crear y mostrar un Snackbar
            val snackbar = Snackbar.make(findViewById(R.id.recycler_view), "Tarea terminada", Snackbar.LENGTH_SHORT)
            snackbar.show()

        }
    }

}
