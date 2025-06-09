package burrow.carton.wordy.command

import burrow.carton.core.command.CoreCommand
import burrow.carton.hoard.Hoard
import burrow.carton.web.WebRecord
import burrow.carton.wordy.record.WordRecord
import picocli.CommandLine

@CommandLine.Command(
    name = "list",
    header = ["Display a list of words."],
)
class ListCommand() : CoreCommand() {
    @CommandLine.Parameters(
        index = "0",
        description = ["The start ID of words to display."],
        defaultValue = "1"
    )
    private var startId = 1

    @CommandLine.Parameters(
        index = "1",
        description = ["The number of words to display."],
        defaultValue = "-1"
    )
    private var length = -1

    @CommandLine.Option(
        names = ["--reverse", "-r"],
        description = ["Reverse."]
    )
    private var shouldReverse = false

    override fun call(): Int {
        super.call()

        use(Hoard::class).getAllRecords<WordRecord>().forEach { wordRecord ->
            val (word, translation, _, _, _, _, _) =  wordRecord
            stdout.println(
                "${wordRecord.id.toString().padEnd(4)} $word $translation"
            )
        }
//        return dispatch(
//            TableCommand::class,
//            listOf(
//                startId,
//                length,
//                "--keys",
//                "word; translation; reviews",
//                "--reverse=$shouldReverse",
//                "--spacing=4"
//            )
//        )

        return CommandLine.ExitCode.OK
    }
}