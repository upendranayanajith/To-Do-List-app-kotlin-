package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.databinding.FragmentToDoDialogBinding
import com.example.kotlintodopractice.utils.model.ToDoData
import com.google.android.material.textfield.TextInputEditText
import android.view.View
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.widget.Toast
import java.time.format.DateTimeParseException

class ToDoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentToDoDialogBinding
    private var listener: OnDialogNextBtnClickListener? = null
    private var toDoData: ToDoData? = null

    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogFragment"
        @JvmStatic
        fun newInstance(taskId: String, task: String, date: String, time: String) =
            ToDoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                    putString("date", date)
                    putString("time", time)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



                if (arguments != null) {
                    val formatterDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                    val formatterTime = DateTimeFormatter.ofPattern("HH:mm")

                    val parsedDate = LocalDate.parse(arguments?.getString("date"), formatterDate)
                    val parsedTime = LocalTime.parse(arguments?.getString("time"), formatterTime)

                    toDoData = ToDoData(
                        arguments?.getString("taskId").toString(),
                        arguments?.getString("task").toString(),
                        parsedDate,
                        parsedTime
                    )
                    binding.todoEt.setText(toDoData?.task)
                    binding.editTextDate2.setText(arguments?.getString("date"))
                    binding.editTextTime2.setText(arguments?.getString("time"))
                }

        binding.todoClose.setOnClickListener {
            dismiss()
        }

        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoEt.text.toString()
            val date = binding.editTextDate2.text.toString()
            val time = binding.editTextTime2.text.toString()


            if (!validateDateTime(date, time)) {
                Toast.makeText(context, "Invalid date or time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (todoTask.isNotEmpty()) {
                if (toDoData == null) {
                    listener?.saveTask(todoTask, date, time, binding.todoEt)
                } else {
                    toDoData!!.task = todoTask
                    listener?.updateTask(toDoData!!, date, time, binding.todoEt)
                }
            }
        }
    }

    interface OnDialogNextBtnClickListener {
        fun saveTask(todoTask: String, date: String, time: String, todoEdit: TextInputEditText)
        fun updateTask(toDoData: ToDoData, date: String, time: String, todoEdit: TextInputEditText)
    }


    fun validateDateTime(date: String, time: String): Boolean {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        return try {
            val parsedDate = LocalDate.parse(date, dateFormatter)
            val parsedTime = LocalTime.parse(time, timeFormatter)

            // Check if the date is in the past or future
            if (parsedDate.isBefore(LocalDate.now())) {
                return false
            }

            // Check if the time is within the 24 hours and 60 minutes range
            if (parsedTime.hour > 23 || parsedTime.minute > 59) {
                return false
            }

            true
        } catch (e: DateTimeParseException) {
            false
        }
    }


}