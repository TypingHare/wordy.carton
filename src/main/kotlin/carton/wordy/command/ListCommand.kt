package burrow.carton.wordy.command

import burrow.carton.hoard.command.TableCommand
import burrow.kernel.terminal.*

@BurrowCommand(
    name = "list",
    header = ["Display a list of words."],
)
class ListCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = ["The start ID of words to display."],
        defaultValue = "1"
    )
    private var startId: Int = 1

    @Parameters(
        index = "1",
        description = ["The number of words to display."],
        defaultValue = "-1"
    )
    private var length = -1

    @Option(
        names = ["--reverse", "-r"],
        description = ["Reverse."]
    )
    private var shouldReverse = false

    override fun call(): Int {
        return dispatch(
            TableCommand::class,
            listOf(
                startId,
                length,
                "--keys",
                "word; translation; reviews",
                "--reverse=$shouldReverse",
            )
        )
    }
}