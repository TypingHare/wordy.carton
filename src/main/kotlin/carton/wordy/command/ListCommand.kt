package burrow.carton.wordy.command

import burrow.kernel.command.Command
import burrow.kernel.command.CommandData
import picocli.CommandLine
import picocli.CommandLine.*

@CommandLine.Command(
    name = "list",
    description = ["List all words."],
)
class ListCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = [
            "The number of words to display."
        ],
        defaultValue = "0"
    )
    private var count = 0

    @Option(
        names = ["--reverse", "-r"],
        description = [
            "Reverse."
        ]
    )
    private var shouldReverse = false

    override fun call(): Int {


        return ExitCode.OK
    }
}