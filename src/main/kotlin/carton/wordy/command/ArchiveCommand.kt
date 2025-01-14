package burrow.carton.wordy.command

import burrow.carton.wordy.Wordy
import burrow.kernel.terminal.*

@BurrowCommand(
    name = "archive",
    header = ["Archives a word."]
)
class ArchiveCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = ["The ID or the word of the word entry to archive."]
    )
    private var idOrWord = ""

    override fun call(): Int {
        use(Wordy::class).getWordEntry(idOrWord).apply {
            update(Wordy.EntryKey.IS_ARCHIVED, true)
        }

        return ExitCode.OK
    }
}