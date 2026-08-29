package `in`.sartaj.fb2site

import `in`.sartaj.fb2site.cli.convertCommand
import `in`.sartaj.fb2site.cli.groupsCommand
import `in`.sartaj.fb2site.cli.inspectCommand
import `in`.sartaj.fb2site.cli.printHelp
import `in`.sartaj.fb2site.cli.userCommand
import `in`.sartaj.fb2site.cli.user2Command
import `in`.sartaj.fb2site.cli.versionCommand

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printHelp()
        return
    }

    when (args[0]) {
        "help", "--help", "-h" -> printHelp()
        "version", "--version", "-v" -> versionCommand()
        "convert" -> convertCommand(args)
        "inspect" -> inspectCommand(args)
        "groups" -> groupsCommand(args)
        "user" -> userCommand(args)
	"user2" -> user2Command(args)
        else -> {
            println("Unknown command: ${args[0]}")
            printHelp()
        }
    }
}
