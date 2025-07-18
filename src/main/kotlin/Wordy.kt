package burrow.carton.wordy

import burrow.carton.core.Core
import burrow.carton.core.command.palette.Highlight
import burrow.carton.core.command.palette.Style
import burrow.carton.found.Found
import burrow.carton.found.StillNotFoundException
import burrow.carton.hoard.Hoard
import burrow.carton.hoard.HoardPair
import burrow.carton.shell.Shell
import burrow.carton.wordy.command.*
import burrow.carton.wordy.record.WordRecord
import burrow.kernel.Blueprint
import burrow.kernel.chamber.Furnishing
import burrow.kernel.chamber.Furniture
import burrow.kernel.chamber.RawSpec
import burrow.kernel.chamber.Renovator
import kotlin.random.Random

@Furniture(
    version = "0.0.0",
    description = "Learn and review vocabulary using Wordy!",
    type = Furniture.Type.MAIN
)
class Wordy(
    renovator: Renovator,
    blueprint: Blueprint
) : Furnishing<WordySpec>(renovator, blueprint) {
    override fun initSpec(rawSpec: RawSpec) = WordySpec().apply {
        setSpec<Double>("nextCount") { nextCount = it.toInt() }
    }

    override fun assemble() {
        use(Core::class).extendSubcommands(
            ListCommand::class,
            AddCommand::class,
            WordCommand::class,
            ReviewCommand::class,
            ArchiveCommand::class,
            RecentCommand::class,
        )

        useSpec(Hoard::class).recordClassName = WordRecord::class.java.name

        useSpec(HoardPair::class).let {
            it.keyName = "word"
            it.valueName = "translation"
            it.allowDuplicateKeys = true
        }
    }

    override fun launch() {
        use(Found::class).register { command, args ->
            if (args.size != 1) {
                throw StillNotFoundException()
            }

           command.dispatch(WordCommand::class, args[0])
        }

        use(Shell::class).createShellFileIfNotExist()
    }

    fun addWordRecord(word: String, translation: String): WordRecord =
        use(HoardPair::class).addRecord(word, translation)

    private fun getWordRecords(word: String): List<WordRecord> {
        val idSet = use(HoardPair::class).getIdSetByKey(word)
        if (idSet.isEmpty()) {
            throw WordNotFoundException(word)
        }

        return idSet.map { use(Hoard::class).getRecord<WordRecord>(it) }
            .toList()
    }

    fun getWordRecord(idOrWord: String): WordRecord =
        when (val id = idOrWord.toIntOrNull()) {
            null -> use(Wordy::class).getWordRecords(idOrWord)[0]
            else -> use(Hoard::class).getRecord(id)
        }

    fun getRandomWordEntries(
        entries: List<WordRecord>,
        count: Int
    ): List<WordRecord> {
        if (entries.size <= count) {
            return entries
        }

        val reviewMap = mutableMapOf<WordRecord, Int>().apply {
            entries.forEach { record -> this[record] = record.reviews }
        }

        val weights = reviewMap.mapValues { 1.0 / (it.value + 1) }
        val cumulativeWeights = weights.entries
            .runningFold(0.0) { acc, entry -> acc + entry.value }
            .drop(1)

        val entryList = reviewMap.keys.toList()
        val lastWeight = cumulativeWeights.last()
        val selectedEntry = mutableSetOf<WordRecord>()

        return mutableListOf<WordRecord>().apply {
            while (selectedEntry.size < count) {
                val randomValue = Random.nextDouble(lastWeight)
                val index = cumulativeWeights.indexOfFirst { it >= randomValue }
                if (index < 0) continue

                val entry = entryList[index]
                if (selectedEntry.add(entry)) {
                    add(entry)
                }
            }
        }
    }

    object ConfigKey {
        const val NEXT_LENGTH = "wordy.next_length"
    }

    object Highlights {
        val WORD = Highlight(48, style = Style.BOLD)
        val TRANSLATION = Highlight(75, style = Style.BOLD)
        val EXAMPLE = Highlight(222)
        val EXTRA_INFO = Highlight(247)
    }
}

class WordNotFoundException(word: String) :
    RuntimeException("Word not found: $word")