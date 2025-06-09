package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.wordy.Wordy
import picocli.CommandLine

@CommandLine.Command(
    name = "archive",
    header = ["Archives a word."]
)
class ArchiveCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The ID or the word of the word entry to archive."]
    )
    private var idOrWord = ""

    override fun call(): Int {
        super.call()

        use(Wordy::class).getWordRecord(idOrWord).isArchived = true

        return CommandLine.ExitCode.OK
    }
}