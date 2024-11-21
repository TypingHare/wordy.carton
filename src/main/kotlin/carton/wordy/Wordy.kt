package carton.wordy

import burrow.carton.hoard.Entry
import burrow.carton.hoard.HoardPair
import burrow.carton.hoard.HoardTime
import burrow.kernel.chamber.Chamber
import burrow.kernel.config.Config
import burrow.kernel.furnishing.Furnishing
import burrow.kernel.furnishing.annotation.DependsOn
import burrow.kernel.furnishing.annotation.Furniture

@Furniture(
    version = "0.0.0",
    description = "Learn and review vocabulary using Wordy!",
    type = Furniture.Type.MAIN
)
@DependsOn([HoardPair::class, HoardTime::class])
class Wordy(chamber: Chamber) : Furnishing(chamber) {
    override fun modifyConfig(config: Config) {
        config.set(HoardPair.ConfigKey.KEY_NAME, EntryKey.WORD)
        config.set(HoardPair.ConfigKey.VALUE_NAME, EntryKey.TRANSLATION)
    }

    override fun assemble() {
    }

    fun createWordEntry(word: String, translation: String): Entry {
        val entry = use(HoardPair::class).createEntry(word, translation)
        entry.set(EntryKey.EXAMPLE, Default.EXAMPLE)
        entry.set(EntryKey.IS_ARCHIVED, Default.IS_ARCHIVED)
        entry.set(EntryKey.REVIEWS, Default.REVIEWS)

        return entry
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