package org.crackhash.worker.config

interface Route {
    companion object{
        const val INTERNAL_API = "/internal/api"
        const val UPDATE_TASK = "/manager/hash/crack/request"
        const val CREATE_SUBTASK = "/worker/hash/crack/task"
        const val QUEUE =  "worker-queue"
    }
}