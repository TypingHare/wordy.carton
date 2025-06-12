package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.wordy.Wordy
import burrow.carton.wordy.printer.WordPrinter
import burrow.carton.wordy.printer.WordPrinterContext
import picocli.CommandLine

@CommandLine.Command(
    name = "word",
    header = ["Display a word."]
)
class WordCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The ID of the word to display."]
    )
    private var idOrWord = ""

    @CommandLine.Option(
        names = ["--hide-extra", "-h"],
        description = ["Whether to hide the extra information."],
    )
    private var shouldHideExtraInfo = false

    override fun call(): Int {
        super.call()

        val wordEntry = use(Wordy::class).getWordRecord(idOrWord)
        val context = WordPrinterContext(wordEntry, getTerminalWidth()).apply {
            shouldDisplayExtraInformation = !shouldHideExtraInfo
        }
        WordPrinter(stdout, context).print()

        return CommandLine.ExitCode.OK
    }
}