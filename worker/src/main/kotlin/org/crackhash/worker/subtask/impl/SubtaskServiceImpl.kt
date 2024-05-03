package org.crackhash.worker.subtask.impl

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.apache.commons.codec.digest.DigestUtils
import org.crackhash.worker.subtask.api.SubtaskService
import org.crackhash.worker.subtask.api.event.CompletedSubtaskEvent
import org.crackhash.worker.subtask.api.event.CreatedTaskEvent
import org.crackhash.worker.subtask.api.Sender
import org.paukov.combinatorics3.Generator
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class SubtaskServiceImpl(private val sender: Sender) : SubtaskService {
    override fun runAsync(event: CreatedTaskEvent): Unit = run(event)

    override fun run(event: CreatedTaskEvent): Unit =
        sender(
            Json.encodeToJsonElement(
                CompletedSubtaskEvent(event.id, event.partNumber, findWords(event))
            )
        )

    private fun findWords(event: CreatedTaskEvent): Set<String> =
        Generator.permutation(event.alphabet.split(""))
            .withRepetitions(event.maxLength)
            .stream()
            .skip(getSkipElementsNumber(event))
            .limit(getCombinationsCount(event))
            .map { it.joinToString("") }
            .filter { DigestUtils.md5Hex(it) == event.hash }.toList().toSet()

    private fun getElementsNumber(event: CreatedTaskEvent) =
        (event.alphabet.chars().count().toDouble().pow(event.maxLength)).toLong()

    private fun getSkipElementsNumber(event: CreatedTaskEvent): Long =
        event.partNumber * getElementsNumber(event)

    private fun isLastPart(event: CreatedTaskEvent) = event.partCount == event.partNumber - 1

    private fun getBasicCombinationsCount(event: CreatedTaskEvent) =
        getElementsNumber(event) / event.partCount

    private fun getCombinationsCount(event: CreatedTaskEvent) : Long {
        if (!isLastPart(event))
            return getBasicCombinationsCount(event);

        return getBasicCombinationsCount(event) + getElementsNumber(event) % event.partCount
    }
}