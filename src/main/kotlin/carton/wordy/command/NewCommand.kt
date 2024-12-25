package burrow.carton.wordy.command

import burrow.carton.wordy.printer.WordPrinter
import burrow.kernel.command.Command
import burrow.kernel.command.CommandData
import carton.wordy.Wordy
import picocli.CommandLine
import picocli.CommandLine.ExitCode
import picocli.CommandLine.Parameters

@CommandLine.Command(
    name = "new",
    description = ["Creates a new translation entry."]
)
class NewCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = [
            "The word in the entry."
        ]
    )
    private var word = ""

    @Parameters(
        index = "1",
        description = [
            "The translation of the word."
        ]
    )
    private var translation = ""

    @Parameters(
        index = "2",
        description = [
            "An example of the usage of the word."
        ],
        defaultValue = ""
    )
    private var example = ""

    override fun call(): Int {
        val entry = use(Wordy::class).createWordEntry(word, translation)
        if (example.isNotEmpty()) {
            entry.set(Wordy.EntryKey.EXAMPLE, example)
        }

        WordPrinter(stdout, entry).print()

        return ExitCode.OK
    }
}