package burrow.carton.wordy.command

import burrow.carton.hoard.Hoard
import burrow.carton.wordy.Wordy
import burrow.kernel.terminal.*

@BurrowCommand(
    name = "review",
    header = ["Review words with a weighted random selection."],
    description = [
        "Selects words randomly from the database, prioritizing words with fewer reviews. " +
                "Ensures no duplicate words are displayed during a session. Each displayed word " +
                "has its review count incremented after being shown."
    ]
)
class ReviewCommand(data: CommandData) : Command(data) {
    @Parameters(
        index = "0",
        description = [
            "The number of words to review. If not specified, the '" +
                    Wordy.ConfigKey.NEXT_LENGTH + "' config value will be used."
        ],
        defaultValue = "-1"
    )
    private var count = -1

    override fun call(): Int {
        val realCount = when (count) {
            -1 -> config.getNotNull(Wordy.ConfigKey.NEXT_LENGTH)
            else -> count
        }

        use(Hoard::class).storage
            .getAllEntries()
            .let { use(Wordy::class).getRandomWordEntries(it, realCount) }
            .forEachIndexed { index, entry ->
                val reviews = entry.get<Int>(Wordy.EntryKey.REVIEWS)!!
                entry.update(Wordy.EntryKey.REVIEWS, reviews + 1)

                if (index > 0) stdout.println()
                dispatch(WordCommand::class, listOf(entry.id, "--hide-extra"))
            }

        return ExitCode.OK
    }
}