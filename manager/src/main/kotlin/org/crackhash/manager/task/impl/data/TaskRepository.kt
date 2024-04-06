package org.crackhash.manager.task.impl.data

import org.crackhash.manager.task.impl.domain.Task

interface TaskRepository {

    fun add(task: Task): Task

    fun remove(task: Task)

    fun find(id: String): Task
}