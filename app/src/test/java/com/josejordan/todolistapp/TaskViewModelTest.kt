package com.josejordan.todolistapp
import com.josejordan.todolistapp.data.Task
import com.josejordan.todolistapp.data.TaskRepository
import com.josejordan.todolistapp.presentation.TaskViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TaskViewModelTest {

    @Mock
    private lateinit var mockRepository: TaskRepository

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        viewModel = TaskViewModel(mockRepository)
    }

    @Test
    fun `when insert is called, then repository insert is called`() = runTest {
        val task = Task(title = "Title")

        viewModel.insert(task)

        verify(mockRepository).insert(task)
    }

    @Test
    fun `when update is called, then repository update is called`() = runTest {
        val task = Task(title = "Title")

        viewModel.update(task)

        verify(mockRepository).update(task)
    }

    @Test
    fun `when delete is called, then repository delete is called`() = runTest {
        val task = Task(title = "Title")

        viewModel.delete(task)

        verify(mockRepository).delete(task)
    }
}


