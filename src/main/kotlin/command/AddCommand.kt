package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.wordy.Wordy
import picocli.CommandLine

@CommandLine.Command(
    name = "new",
    header = ["Include a new word."]
)
class AddCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The word to include."]
    )
    private var word = ""

    @CommandLine.Parameters(
        index = "1",
        description = ["The translation of the word."]
    )
    private var translation = ""

    @CommandLine.Parameters(
        index = "2",
        description = ["An example of the usage of the word."],
        defaultValue = ""
    )
    private var example = ""

    override fun call(): Int {
        super.call()

        val record = use(Wordy::class).addWordRecord(word, translation)
        if (example.isNotEmpty()) {
            record.example = example
        }

        return dispatch(WordCommand::class, record.id)
    }
}