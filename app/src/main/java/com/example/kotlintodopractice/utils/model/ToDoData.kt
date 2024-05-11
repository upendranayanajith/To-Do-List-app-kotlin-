package com.example.kotlintodopractice.utils.model

import java.time.LocalDate
import java.time.LocalTime

data class ToDoData(
    var taskId: String,
    var task: String,
    var date: LocalDate,
    var time: LocalTime
)