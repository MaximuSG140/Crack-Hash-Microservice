package org.crackhash.manager.task.impl.data

interface DomainRepository<V, ID> {

    fun add(domainModel: V): V

    fun remove(domainModel: V): V

    fun findById(id: ID): V?

    fun findAll(): List<V>
}