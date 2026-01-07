package com.HLPTLab7.ExplorerApp.handlers

import com.HLPTLab7.ExplorerApp.application.ResourceActionUseCase
import com.HLPTLab7.ExplorerApp.interfaces.*
import com.HLPTLab7.ExplorerApp.models.*
import com.HLPTLab7.ExplorerApp.enumerators.*
import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess
import org.springframework.stereotype.Component
@Component
class CommandHandler(
    private val parser: CommandParser,
    private val useCase: ResourceActionUseCase
){
    fun execute(args: Array<String>) : Int {
        return when (val result = parser.parse(args)) {
            is CommandParser.ParseResult.Error -> result.exitCode
            is CommandParser.ParseResult.Ok -> useCase.execute(result.command)
        }
}
}