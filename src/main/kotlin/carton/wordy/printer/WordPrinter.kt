package burrow.carton.wordy.printer

import burrow.carton.hoard.Entry
import burrow.carton.hoard.HoardTime
import burrow.carton.wordy.Wordy
import burrow.kernel.stream.Printer
import java.io.PrintWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WordPrinter(writer: PrintWriter, context: WordPrinterContext) :
    Printer<WordPrinterContext>(writer, context) {
    override fun print() {
        val wordEntry = context.wordEntry
        val id = wordEntry.id
        val word = wordEntry.get<String>(Wordy.EntryKey.WORD)!!
        val translation = wordEntry.get<String>(Wordy.EntryKey.TRANSLATION)!!
        val example = wordEntry.get<String>(Wordy.EntryKey.EXAMPLE)
        val reviews = wordEntry.get<Long>(Wordy.EntryKey.REVIEWS)!!
        val isArchived = wordEntry.get<Boolean>(Wordy.EntryKey.IS_ARCHIVED)!!

        writer.println("($id) $word  $translation")
        if (example != null) {
            writer.println(example)
        }

        val includedAt = wordEntry.get<Long>(HoardTime.EntryKey.CREATED_AT)!!
        val reviewedAt = wordEntry.get<Long>(HoardTime.EntryKey.CREATED_AT)!!
        val includedAtStr = timestampToString(includedAt)
        val reviewedAtStr = timestampToString(reviewedAt)
        val isArchivedStr = if (isArchived) "yes" else "no"
        val extraInfo = """
            reviews: $reviews | included at: $includedAtStr | reviewed at: $reviewedAtStr | is archived: $isArchivedStr
        """.trimIndent()
        writer.println(extraInfo)
    }

    private fun timestampToString(timestampMs: Long): String {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestampMs),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }
}

data class WordPrinterContext(val wordEntry: Entry)