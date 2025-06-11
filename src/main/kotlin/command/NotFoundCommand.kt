package burrow.carton.wordy.command

import burrow.carton.core.Core
import picocli.CommandLine
import burrow.carton.core.command.NotFoundCommand as OriginalNotFoundCommand

@CommandLine.Command(
    name = Core.NOT_FOUND_COMMAND_NAME
)
class NotFoundCommand : OriginalNotFoundCommand(){
    override fun call(): Int {
        return if (commandList.size == 1) {
            dispatch(WordCommand::class, commandList.first())
        } else {
            super.call()
        }
    }
}