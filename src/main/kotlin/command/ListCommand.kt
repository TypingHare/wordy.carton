package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import picocli.CommandLine

@CommandLine.Command(
    name = "list",
    header = ["Display a list of words."],
)
class ListCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The start ID of words to display."],
        defaultValue = "1"
    )
    private var startId = 1

    @CommandLine.Parameters(
        index = "1",
        description = ["The number of words to display."],
        defaultValue = "-1"
    )
    private var length = -1

    @CommandLine.Option(
        names = ["--reverse", "-r"],
        description = ["Reverse."]
    )
    private var shouldReverse = false

    override fun call(): Int {
        super.call()

//        return dispatch(
//            TableCommand::class,
//            listOf(
//                startId,
//                length,
//                "--keys",
//                "word; translation; reviews",
//                "--reverse=$shouldReverse",
//                "--spacing=4"
//            )
//        )

        return CommandLine.ExitCode.OK
    }
}