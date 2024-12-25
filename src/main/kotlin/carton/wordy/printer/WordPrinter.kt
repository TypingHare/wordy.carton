package burrow.carton.wordy.printer

import burrow.carton.hoard.Entry
import burrow.kernel.stream.Printer
import carton.wordy.Wordy
import java.io.PrintWriter

class WordPrinter(writer: PrintWriter, word: Entry) :
    Printer<Entry>(writer, word) {
    override fun print() {
        val entry = context
        val id = entry.id
        val word = entry.get<String>(Wordy.EntryKey.WORD)
        val translation = entry.get<String>(Wordy.EntryKey.TRANSLATION)
        val example = entry.get<String>(Wordy.EntryKey.EXAMPLE)
        val reviews = entry.get<String>(Wordy.EntryKey.REVIEWS)
    }
}