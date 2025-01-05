package burrow.carton.wordy

import burrow.carton.hoard.Entry
import burrow.carton.hoard.Hoard
import burrow.carton.hoard.HoardPair
import burrow.carton.hoard.HoardTime
import burrow.carton.wordy.command.*
import burrow.kernel.config.Config
import burrow.kernel.converter.StringConverterPairs
import burrow.kernel.furniture.Furnishing
import burrow.kernel.furniture.Renovator
import burrow.kernel.furniture.annotation.Dependency
import burrow.kernel.furniture.annotation.Furniture
import burrow.kernel.furniture.annotation.RequiredDependencies

const val VERSION = "0.0.0"
const val REQUIRED_BURROW_VERSION = "0.0.0"

@Furniture(
    version = VERSION,
    description = "Learn and review vocabulary using Wordy!",
    type = Furniture.Type.MAIN
)
@RequiredDependencies(
    Dependency(HoardPair::class, REQUIRED_BURROW_VERSION),
    Dependency(HoardTime::class, REQUIRED_BURROW_VERSION)
)
class Wordy(renovator: Renovator) : Furnishing(renovator) {
    override fun modifyConfig(config: Config) {
        config[HoardPair.ConfigKey.KEY_NAME] = EntryKey.WORD
        config[HoardPair.ConfigKey.VALUE_NAME] = EntryKey.TRANSLATION
    }

    override fun assemble() {
        registerCommand(NewCommand::class)
        registerCommand(WordCommand::class)
        registerCommand(ListCommand::class)
        registerCommand(ArchiveCommand::class)
        registerCommand(RecentCommand::class)

        use(Hoard::class).converterPairsContainer.apply {
            add(EntryKey.IS_ARCHIVED, StringConverterPairs.BOOLEAN)
            add(EntryKey.REVIEWS, StringConverterPairs.INT)
        }
    }

    fun createWordEntry(word: String, translation: String): Entry {
        val entry = use(HoardPair::class).createEntry(word, translation)
        entry[EntryKey.EXAMPLE] = Default.EXAMPLE
        entry[EntryKey.IS_ARCHIVED] = Default.IS_ARCHIVED
        entry[EntryKey.REVIEWS] = Default.REVIEWS

        return entry
    }

    private fun getWordEntryByWord(word: String): Entry =
        use(HoardPair::class).getEntries(word).firstOrNull()
            ?: throw WordNotFoundException(word)

    fun getWordEntry(idOrWord: String): Entry =
        when (val id = idOrWord.toIntOrNull()) {
            null -> use(Wordy::class).getWordEntryByWord(idOrWord)
            else -> use(Hoard::class)[id]
        }

    object EntryKey {
        const val WORD = "word"
        const val TRANSLATION = "translation"
        const val EXAMPLE = "example"
        const val IS_ARCHIVED = "is_archived"
        const val REVIEWS = "reviews"
    }

    object Default {
        const val EXAMPLE = ""
        const val IS_ARCHIVED = false
        const val REVIEWS = 0
    }
}

class WordNotFoundException(word: String) :
    RuntimeException("Word not found: $word")