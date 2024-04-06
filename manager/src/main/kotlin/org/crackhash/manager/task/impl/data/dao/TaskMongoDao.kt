package org.crackhash.manager.task.impl.data.dao

import org.crackhash.manager.task.impl.domain.Task
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskMongoDao: MongoRepository<Task, String>