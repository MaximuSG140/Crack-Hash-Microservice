package org.crackhash.manager.config

interface Route {
    companion object{
        const val API = "/api"
        const val INTERNAL_API = "/internal/api"
        const val CREATE_TASK = "/hash/crack"
        const val GET_TASK = "/hash/status/requestId={id}"
        const val UPDATE_TASK = "/manager/hash/crack/request"
        const val CREATE_SUBTASK = "/worker/hash/crack/task"
        const val QUEUE = "manager-queue"
    }
}