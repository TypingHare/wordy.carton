package burrow.carton.wordy.record

import burrow.carton.hoard.Record
import burrow.carton.hoard.record.Timed

data class WordRecord(
    var word: String = "",
    var translation: String = "",
    var example: String = "",
    var isArchived: Boolean = false,
    var reviews: Int = 0,
    override var createdAt: Long? = null,
    override var updatedAt: Long? = null,
) : Record(), Timed