package burrow.carton.wordy.command

import burrow.carton.wordy.Wordy
import burrow.kernel.terminal.BurrowCommand
import burrow.kernel.terminal.Command
import burrow.kernel.terminal.CommandData
import burrow.kernel.terminal.Parameters

@BurrowCommand(
    name = "new",
    header = ["Includes a new word."]
)
class NewCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = ["The word to include."]
    )
    private var word = ""

    @Parameters(
        index = "1",
        description = ["The translation of the word."]
    )
    private var translation = ""

    @Parameters(
        index = "2",
        description = ["An example of the usage of the word."],
        defaultValue = ""
    )
    private var example = ""

    override fun call(): Int {
        val entry = use(Wordy::class).createWordEntry(word, translation)
        if (example.isNotEmpty()) {
            entry.update(Wordy.EntryKey.EXAMPLE, example)
        }

        return dispatch(WordCommand::class, listOf(entry.id))
    }
}