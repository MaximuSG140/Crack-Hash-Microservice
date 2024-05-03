package org.crackhash.manager.task.impl.data

import org.crackhash.manager.task.impl.Task
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class InMemoryTaskRepository : DomainRepository<Task, String> {
    override fun add(domainModel: Task): Task {
        storage[domainModel.id] = domainModel
        return domainModel
    }

    override fun remove(domainModel: Task): Task {
        storage.remove(domainModel.id)
        return domainModel
    }

    override fun findById(id: String): Task? {
        return storage[id]
    }

    override fun findAll(): List<Task> {
        return storage.values.toList()
    }

    private val storage = Collections.synchronizedMap(mutableMapOf<String, Task>())
}