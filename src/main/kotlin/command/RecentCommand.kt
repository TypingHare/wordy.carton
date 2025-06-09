package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.hoard.Hoard
import burrow.carton.wordy.record.WordRecord
import picocli.CommandLine

@CommandLine.Command(
    name = "recent",
    header = ["Display recent included words."]
)
class RecentCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The ID of the word to display."],
        defaultValue = "8"
    )
    private var length = 8

    override fun call(): Int {
        super.call()

        val maxId = use(Hoard::class).getStorage<WordRecord>().maxId

        return dispatch(ListCommand::class, maxId, length, "--reverse=true")
    }
}