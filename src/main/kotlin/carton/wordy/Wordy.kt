package burrow.carton.wordy

import burrow.carton.hoard.Entry
import burrow.carton.hoard.Hoard
import burrow.carton.hoard.HoardPair
import burrow.carton.hoard.HoardTime
import burrow.carton.inverse.Inverse
import burrow.carton.inverse.annotation.ConfigItem
import burrow.carton.inverse.annotation.ConverterPairType
import burrow.carton.inverse.annotation.InverseRegisterCommands
import burrow.carton.inverse.annotation.InverseSetConfig
import burrow.carton.wordy.Wordy.EntryKey
import burrow.common.converter.StringConverterPairs
import burrow.common.palette.Highlight
import burrow.common.palette.Style
import burrow.kernel.furniture.Furnishing
import burrow.kernel.furniture.Renovator
import burrow.kernel.furniture.annotation.Dependency
import burrow.kernel.furniture.annotation.Furniture
import burrow.kernel.furniture.annotation.RequiredDependencies
import kotlin.random.Random

const val VERSION = "0.0.0"
const val REQUIRED_BURROW_VERSION = "0.0.0"

@Furniture(
    version = VERSION,
    description = "Learn and review vocabulary using Wordy!",
    type = Furniture.Type.MAIN
)
@RequiredDependencies(
    Dependency(Inverse::class, REQUIRED_BURROW_VERSION),
    Dependency(HoardPair::class, REQUIRED_BURROW_VERSION),
    Dependency(HoardTime::class, REQUIRED_BURROW_VERSION)
)
@InverseSetConfig(
    ConfigItem(
        Wordy.ConfigKey.NEXT_LENGTH,
        defaultValue = Wordy.DEFAULT.NEXT_LENGTH.toString(),
        type = ConverterPairType.INT
    ),
    ConfigItem(HoardPair.ConfigKey.KEY_NAME, value = EntryKey.WORD),
    ConfigItem(HoardPair.ConfigKey.VALUE_NAME, value = EntryKey.TRANSLATION)
)
@InverseRegisterCommands
class Wordy(renovator: Renovator) : Furnishing(renovator) {
    override fun assemble() {
        use(Hoard::class).converterPairsContainer.apply {
            add(EntryKey.IS_ARCHIVED, StringConverterPairs.BOOLEAN)
            add(EntryKey.REVIEWS, StringConverterPairs.INT)
        }
    }

    fun createWordEntry(word: String, translation: String): Entry {
        val entry = use(HoardPair::class).createEntry(word, translation)
        entry[EntryKey.EXAMPLE] = EntryDefault.EXAMPLE
        entry[EntryKey.IS_ARCHIVED] = EntryDefault.IS_ARCHIVED
        entry[EntryKey.REVIEWS] = EntryDefault.REVIEWS

        return entry
    }

    fun getWordEntry(idOrWord: String): Entry =
        when (val id = idOrWord.toIntOrNull()) {
            null -> use(Wordy::class).getWordEntryByWord(idOrWord)
            else -> use(Hoard::class)[id]
        }

    fun getRandomWordEntries(
        entries: List<Entry>,
        count: Int
    ): List<Entry> {
        if (entries.size <= count) {
            return entries
        }

        val reviewMap = mutableMapOf<Entry, Int>().apply {
            entries.forEach { entry ->
                this[entry] = entry.get<Int>(EntryKey.REVIEWS)!!
            }
        }

        val weights = reviewMap.mapValues { 1.0 / (it.value + 1) }
        val cumulativeWeights = weights.entries
            .runningFold(0.0) { acc, entry -> acc + entry.value }
            .drop(1)

        val entryList = reviewMap.keys.toList()
        val lastWeight = cumulativeWeights.last()
        val selectedEntry = mutableSetOf<Entry>()

        return mutableListOf<Entry>().apply {
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

    private fun getWordEntryByWord(word: String): Entry =
        use(HoardPair::class).getEntries(word).firstOrNull()
            ?: throw WordNotFoundException(word)

    object ConfigKey {
        const val NEXT_LENGTH = "wordy.next_length"
    }

    object DEFAULT {
        const val NEXT_LENGTH = 5
    }

    object EntryKey {
        const val WORD = "word"
        const val TRANSLATION = "translation"
        const val EXAMPLE = "example"
        const val IS_ARCHIVED = "is_archived"
        const val REVIEWS = "reviews"

    }

    object EntryDefault {
        const val EXAMPLE = ""
        const val IS_ARCHIVED = false
        const val REVIEWS = 0
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