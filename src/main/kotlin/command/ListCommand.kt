package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.core.printer.TablePrinter
import burrow.carton.core.printer.TablePrinterContext
import burrow.carton.hoard.Hoard
import burrow.carton.wordy.record.WordRecord
import picocli.CommandLine

@CommandLine.Command(
    name = "list",
    header = ["Display a list of words."],
)
class ListCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The number of words to display."],
        defaultValue = "-1"
    )
    private var length = -1

    @CommandLine.Option(
        names = ["--start", "-s"],
        description = ["The start ID of words to display."],
        defaultValue = "-1"
    )
    private var startId = -1

    @CommandLine.Option(
        names = ["--reverse", "-r"],
        description = ["Reverse."]
    )
    private var shouldReverse = false

    override fun call(): Int {
        super.call()

        val hoard = use(Hoard::class)
        val allWords = hoard.getAllRecords<WordRecord>()
        val wordList =
            if (shouldReverse) getReverseWords(allWords) else getWords(allWords)

        val table = buildList {
            add(listOf("ID", "word", "translation"))
            addAll(wordList.map {
                listOf(
                    it.id.toString(),
                    it.word,
                    it.translation
                )
            })
        }

        TablePrinter(
            stdout,
            TablePrinterContext(table, getTerminalWidth())
        ).print()

        return CommandLine.ExitCode.OK
    }

    fun getWords(allWords: List<WordRecord>): List<WordRecord> {
        val startId = if (this.startId == -1) 1 else this.startId
        val filteredWords = allWords.filter { it.id >= startId }
        return if (length == -1) filteredWords else filteredWords.take(length)
    }

    fun getReverseWords(allWords: List<WordRecord>): List<WordRecord> {
        val startId =
            if (this.startId == -1) allWords.last().id else this.startId
        val filteredWords = allWords.filter { it.id <= startId }.reversed()
        return if (length == -1) filteredWords else filteredWords.take(length)
    }
}
