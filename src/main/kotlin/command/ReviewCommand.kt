package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.hoard.Hoard
import burrow.carton.hoard.HoardPair
import burrow.carton.wordy.Wordy
import burrow.carton.wordy.record.WordRecord
import picocli.CommandLine

@CommandLine.Command(
    name = "review",
    header = ["Review words with a weighted random selection."],
    description = [
        "This command select words randomly from the database, prioritizing words with fewer reviews. Ensures no duplicate words are displayed during a session. Each displayed word has its review count incremented after being shown."
    ]
)
class ReviewCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = [
            "The number of words to review. If not specified, the '" +
                    Wordy.ConfigKey.NEXT_LENGTH + "' config value will be used."
        ],
        defaultValue = "-1"
    )
    private var count = -1

    override fun call(): Int {
        super.call()

        val realCount = when (count) {
            -1 -> use(Wordy::class).spec.nextCount
            else -> count
        }

        use(Hoard::class).getAllRecords<WordRecord>()
            .let { use(Wordy::class).getRandomWordEntries(it, realCount) }
            .forEachIndexed { index, record ->
                record.reviews += 1
                if (index > 0) stdout.println()
                dispatch(WordCommand::class, record.id, "--hide-extra")
            }

        return CommandLine.ExitCode.OK
    }
}