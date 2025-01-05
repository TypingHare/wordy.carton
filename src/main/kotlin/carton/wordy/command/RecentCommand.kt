package burrow.carton.wordy.command

import burrow.carton.hoard.Hoard
import burrow.kernel.terminal.BurrowCommand
import burrow.kernel.terminal.Command
import burrow.kernel.terminal.CommandData
import burrow.kernel.terminal.Parameters

@BurrowCommand(
    name = "recent",
    header = ["Displays recent words."]
)
class RecentCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = ["The ID of the word to display."],
        defaultValue = "8"
    )
    private var length = 8

    override fun call(): Int {
        val maxId = use(Hoard::class).maxId
        return dispatch(
            ListCommand::class,
            listOf(maxId, length, "--reverse=true")
        )
    }
}