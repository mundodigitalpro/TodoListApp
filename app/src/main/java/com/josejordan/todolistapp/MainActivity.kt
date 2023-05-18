package com.josejordan.todolistapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskSelectedListener {

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleEditText: EditText = findViewById(R.id.title_edit_text)
        val addButton: Button = findViewById(R.id.add_button)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

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
                    val task = tasks[position]
                    onTaskSwiped(task)
                    val snackbar = Snackbar.make(
                        findViewById(R.id.recycler_view),
                        "Tarea terminada",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val task = Task(title)
            taskAdapter.addTask(task)
            titleEditText.text.clear()
            hideKeyboard(this@MainActivity)
            val snackbar = Snackbar.make(
                findViewById(R.id.recycler_view),
                "Tarea Añadida",
                Snackbar.LENGTH_SHORT
            )
            snackbar.show()
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = activity.currentFocus
        if (view != null) {
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onTaskSwiped(task: Task) {
        val position = tasks.indexOf(task)
        if (position != -1) {
            taskAdapter.deleteTask(position)
            val snackbar = Snackbar.make(
                findViewById(R.id.recycler_view),
                "Tarea terminada",
                Snackbar.LENGTH_SHORT
            )
            snackbar.show()

        }
    }

}
