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
    private var idOrWord: String = ""

    override fun call(): Int {
        use(Wordy::class).getWordEntry(idOrWord).apply {
            WordPrinter(stdout, WordPrinterContext(this)).print()
        }

        return ExitCode.OK
    }
}