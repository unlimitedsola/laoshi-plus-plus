package love.sola.laoshipp.hsk

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

/**
 * This is an ad-hoc application for converting old dictionary to JSON format.
 */
private fun main() {
    Files.list(Path.of("./dict"))
        .filter { it.toString().endsWith(".txt") }
        .forEach { path ->
            val level = path.fileName.toString()[4].toString()
            val content = Files.readString(path)
            val words = parse(content)
            Files.writeString(Path.of("src/main/resources/levels/$level.json"), Json.encodeToString(words))
        }
}

private fun parse(content: String): List<Word> {
    return content.lineSequence().filter(String::isNotBlank).map { line ->
        val cols = line.split('\t')
        Word(
            cols[0].polish(),
            cols[1].polish(),
            listOf(cols[2].polish().lowercase(), cols[3].polish().lowercase()).filter(String::isNotBlank),
            cols[4].split(';').map(String::trim).filter(String::isNotBlank)
        )
    }.toList()
}

private val ILLEGAL_REGEX = Regex("""[\s'﻿]""")

fun String.polish() = this.replace(ILLEGAL_REGEX, "").trim()
