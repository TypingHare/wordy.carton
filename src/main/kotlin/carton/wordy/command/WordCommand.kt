package burrow.carton.wordy.command

import burrow.carton.wordy.Wordy
import burrow.carton.wordy.printer.WordPrinter
import burrow.carton.wordy.printer.WordPrinterContext
import burrow.kernel.terminal.*

@BurrowCommand(
    name = "word",
    header = ["Displays a word."]
)
class WordCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = ["The ID of the word to display."]
    )
    private var idOrWord = ""

    @Option(
        names = ["--hide-extra", "-h"],
        description = ["Whether to hide the extra information."],
    )
    private var shouldHideExtraInfo = false

    override fun call(): Int {
        val wordEntry = use(Wordy::class).getWordEntry(idOrWord)
        val context = WordPrinterContext(wordEntry, getTerminalWidth()).apply {
            shouldDisplayExtraInformation = !shouldHideExtraInfo
        }
        WordPrinter(stdout, context).print()

        return ExitCode.OK
    }
}