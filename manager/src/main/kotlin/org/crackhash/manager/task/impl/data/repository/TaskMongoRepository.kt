package org.crackhash.manager.task.impl.data.repository

import org.crackhash.manager.task.api.exception.TaskNotFoundException
import org.crackhash.manager.task.impl.data.TaskRepository
import org.crackhash.manager.task.impl.data.dao.TaskMongoDao
import org.crackhash.manager.task.impl.domain.Task
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class TaskMongoRepository(private val dao: TaskMongoDao): TaskRepository {

    override fun add(task: Task): Task = dao.save(task)

    override fun remove(task: Task): Unit = dao.deleteById(task.id)

    override fun find(id: String): Task = dao.findByIdOrNull(id) ?: throw TaskNotFoundException(id)
}