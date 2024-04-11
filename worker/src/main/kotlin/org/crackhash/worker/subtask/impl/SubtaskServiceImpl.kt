package org.crackhash.worker.subtask.impl

import org.apache.commons.codec.digest.DigestUtils
import org.crackhash.worker.subtask.api.SubtaskService
import org.crackhash.worker.subtask.api.event.CompletedSubtaskEvent
import org.crackhash.worker.subtask.api.event.CreatedTaskEvent
import org.crackhash.worker.util.LogBefore
import org.crackhash.worker.util.Sender
import org.paukov.combinatorics3.Generator
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class SubtaskServiceImpl(private val sender: Sender) : SubtaskService {

    @LogBefore
    override fun runAsync(event: CreatedTaskEvent): Unit = run(event)

    @LogBefore
    override fun run(event: CreatedTaskEvent): Unit =
        sender(listOf(CompletedSubtaskEvent(event.id, event.partNumber, findWords(event))))

    private fun findWords(event: CreatedTaskEvent): Set<String> =
        Generator.permutation(event.alphabet.split(""))
            .withRepetitions(event.maxLength)
            .stream()
            .skip(getSkipElementsNumber(event))
            .map { it.joinToString("") }
            .filter { DigestUtils.md5Hex(it) == event.hash }.toList().toSet()

    private fun getSkipElementsNumber(event: CreatedTaskEvent): Long =
        event.partNumber * (event.alphabet.chars().count().toDouble().pow(event.maxLength)).toLong()

}