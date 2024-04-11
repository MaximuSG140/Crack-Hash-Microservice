package org.crackhash.manager.task.impl.data

import org.crackhash.manager.task.impl.Task
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TaskMongoDao: ReactiveMongoRepository<Task, String>