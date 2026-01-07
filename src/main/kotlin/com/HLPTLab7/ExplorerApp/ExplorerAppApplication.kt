package com.HLPTLab7.ExplorerApp
import com.HLPTLab7.ExplorerApp.enumerators.*
import com.HLPTLab7.ExplorerApp.handlers.*
import kotlin.system.exitProcess
import java.sql.SQLException
import org.springframework.boot.autoconfigure.SpringBootApplication
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
            var result = commandHandler.execute(args.mapNotNull { it }.toTypedArray())
            logger.info("Выполнение завершено с кодом: " + result)
            exitProcess(result)
        } catch (exception: SQLException) {
            logger.error("Ошибка запроса SQL", exception)
            exitProcess(ExitCode.ERROR_SQL_REQUEST.code)
        } catch (exception: Exception) {
            logger.error("Неизвестная ошибка", exception)
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
