package org.crackhash.manager.task.api.exception

class TaskNotFoundException(id: String): RuntimeException("Task with id=$id not found")