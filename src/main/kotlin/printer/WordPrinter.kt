package burrow.carton.wordy.printer

import burrow.carton.core.command.palette.PicocliPalette
import burrow.carton.core.printer.Printer
import burrow.carton.wordy.Wordy.Highlights
import burrow.carton.wordy.record.WordRecord
import java.io.PrintWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WordPrinter(writer: PrintWriter, context: WordPrinterContext) :
    Printer<WordPrinterContext>(writer, context) {
    override fun print() {
        val wordRecord = context.wordRecord
        val (word, translation, example, isArchived, reviews) = wordRecord
        val id = context.wordRecord.id

        val palette = PicocliPalette()
        val coloredWord = palette.color(word, Highlights.WORD)
        val coloredTranslation =
            palette.color(translation, Highlights.TRANSLATION)
        writer.println("($id) $coloredWord   $coloredTranslation")
        if (example.isNotBlank()) {
            writer.println(
                palette.color(
                    getExampleLinesString(
                        example,
                        context.maxColumns
                    ), Highlights.EXAMPLE
                )
            )
        }

        if (context.shouldDisplayExtraInformation) {
            val includedAt = wordRecord.createdAt!!
            val reviewedAt = wordRecord.updatedAt!!
            val includedAtStr = timestampToString(includedAt)
            val reviewedAtStr = timestampToString(reviewedAt)
            val isArchivedStr = if (isArchived) "yes" else "no"
            val extraInfo =
                """
                reviews: $reviews | reviewed at: $reviewedAtStr | included at: $includedAtStr | archived: $isArchivedStr
                """.trimIndent()
            val coloredExtraInfo =
                palette.color(extraInfo, Highlights.EXTRA_INFO)
            writer.println(coloredExtraInfo)
        }
    }

    private fun timestampToString(timestampMs: Long): String {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestampMs),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }

    private fun getExampleLinesString(
        example: String,
        maxColumns: Int
    ): String {
        val tokens = example.split(" ")
        val lines = mutableListOf<String>()

        var i = 0
        var lineNumber = 0
        while (i < tokens.size) {
            if (lineNumber == lines.size) lines.add("")
            val line = lines[lineNumber]
            val token = tokens[i]
            if (line.isEmpty()) {
                lines[lineNumber] = token
                ++i
            } else if (line.length + token.length + 1 > maxColumns) {
                ++lineNumber
            } else {
                lines[lineNumber] = "$line $token"
                ++i
            }
        }

        return lines.joinToString("\n")
    }
}

data class WordPrinterContext(val wordRecord: WordRecord, val maxColumns: Int) {
    var shouldDisplayExtraInformation = false
}