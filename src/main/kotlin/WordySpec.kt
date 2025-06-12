package burrow.carton.wordy

import burrow.carton.core.Core
import burrow.carton.found.Found
import burrow.carton.hoard.HoardPair
import burrow.carton.hoard.HoardTime
import burrow.carton.shell.Shell
import burrow.kernel.chamber.Dependency
import burrow.kernel.chamber.Spec

const val BURROW_VERSION = "0.0.0"

data class WordySpec(
    override var requires: Collection<Dependency> = listOf(
        Dependency(Core::class, BURROW_VERSION),
        Dependency(HoardPair::class, BURROW_VERSION),
        Dependency(HoardTime::class, BURROW_VERSION),
        Dependency(Shell::class, BURROW_VERSION),
        Dependency(Found::class, BURROW_VERSION)
    ),
    var nextCount: Int = 5
) : Spec(requires)