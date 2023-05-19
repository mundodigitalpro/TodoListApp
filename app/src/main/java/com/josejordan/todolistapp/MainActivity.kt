package com.josejordan.todolistapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.josejordan.todolistapp.data.Task
import com.josejordan.todolistapp.presentation.TaskAdapter
import com.josejordan.todolistapp.presentation.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener{

    private lateinit var taskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleEditText: EditText = findViewById(R.id.title_edit_text)
        val addButton: Button = findViewById(R.id.add_button)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(emptyList<Task>().toMutableList(), this)
        recyclerView.adapter = taskAdapter

        taskViewModel.allTasks.observe(this) { tasks ->
            // Actualizar la lista de tareas en el adaptador cuando los datos cambian
            tasks?.let {
                taskAdapter.tasks = it.toMutableList()
                taskAdapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val task = taskAdapter.tasks[position]
                    taskViewModel.delete(task)
                    snackBarMessage("Tarea terminada")
                }
            })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val task = Task(title=title)
            taskViewModel.insert(task)
            titleEditText.text.clear()
            hideKeyboard(this@MainActivity)
            snackBarMessage("Tarea Añadida")
        }
    }

    private fun snackBarMessage(text: String) {
        Snackbar.make(findViewById(R.id.recycler_view),text, Snackbar.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = activity.currentFocus
        if (view != null) {
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onTaskClick(task: Task) {
        taskViewModel.update(task)
    }

}
