package com.HLPTLab7.ExplorerApp.handlers

import com.HLPTLab7.ExplorerApp.interfaces.*
import com.HLPTLab7.ExplorerApp.models.*
import com.HLPTLab7.ExplorerApp.enumerators.*
import kotlinx.cli.*
import org.springframework.stereotype.Service
@Service
class CommandParser {
    sealed class ParseResult {
        data class Ok(val command: Command) : ParseResult()
        data class Error(val exitCode: Int) : ParseResult()
    }

    fun parse(arguments: Array<String>): ParseResult {
        val parser = ArgParser("app")

        val login by parser.option(ArgType.String, fullName = "login", description = "User login").required()
        val password by parser.option(ArgType.String, fullName = "password", description = "User password").required()
        val action by parser.option(ArgType.String, fullName = "action", description = "Type of action with file").required()
        val resource by parser.option(ArgType.String, fullName = "resource", description = "Path to resource").required()
        val volume by parser.option(ArgType.String, fullName = "volume", description = "Volume of file").required()

        try {
            parser.parse(arguments)
        } catch (e: Exception) {
            return ParseResult.Error(ExitCode.HELP.code)
        }

        val actionEnum = when ((action ?: "").lowercase()) {
            "read" -> Action.READ
            "write" -> Action.WRITE
            "execute" -> Action.EXECUTE
            else -> return ParseResult.Error(ExitCode.ERROR_INVALID_ACTION.code)
        }

        val volumeInt = volume?.toIntOrNull()
            ?: return ParseResult.Error(ExitCode.ERROR_INVALID_VOLUME_FORMAT.code)

        return ParseResult.Ok(
            Command(
                login = login!!,
                password = password!!,
                action = actionEnum,
                resourcePath = resource!!,
                volume = volumeInt
            )
        )
    }
}
