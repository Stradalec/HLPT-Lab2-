package com.HLPTLab7.ExplorerApp
import com.HLPTLab7.ExplorerApp.interfaces.*
import com.HLPTLab7.ExplorerApp.models.*
import com.HLPTLab7.ExplorerApp.enumerators.*
import com.HLPTLab7.ExplorerApp.handlers.*
import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess
import com.HLPTLab7.ExplorerApp.repositories.*
import java.sql.SQLException
import com.HLPTLab7.ExplorerApp.database.DatabaseConnectionException
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication;
import org.slf4j.LoggerFactory

@SpringBootApplication
class ExplorerAppApplication(private val commandHandler: CommandHandler) : CommandLineRunner{
    private val logger = LoggerFactory.getLogger(ExplorerAppApplication::class.java)
	override fun run(args: Array<String>) {
        logger.info("A DEBUG Message");
        if (args.isEmpty() || args.any { it == "--help" || it == "-h" }) {
            exitProcess(ExitCode.HELP.code)
        }
        
        try {
            var result = commandHandler.execute(args)
            logger.info(result.toString());
            exitProcess(result)
        } catch (e: DatabaseConnectionException) {
            exitProcess(ExitCode.ERROR_DATABASE_CONNECTION.code)
        } catch (e: SQLException) {
            exitProcess(ExitCode.ERROR_SQL_REQUEST.code)
        } catch (e: Exception) {
            exitProcess(ExitCode.HELP.code)
        }
    }
	 companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ExplorerAppApplication::class.java, *args)
        }
    }
}
