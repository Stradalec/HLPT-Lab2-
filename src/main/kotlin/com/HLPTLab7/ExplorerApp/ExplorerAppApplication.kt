package com.HLPTLab7.ExplorerApp
import interfaces.*
import models.*
import enumerators.*
import handlers.*
import kotlinx.cli.*
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess
import repositories.*
import java.sql.SQLException
import database.DatabaseConnectionException
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication;

@SpringBootApplication
class ExplorerAppApplication : CommandLineRunner{
	override fun run(args: Array<String>) {
        
        if (args.isEmpty() || args.any { it == "--help" || it == "-h" }) {
            exitProcess(ExitCode.HELP.code)
        }
        val userRepository = UserRepository()           
        val resourceRepository = ResourceRepository()
        val permissionRepository = PermissionRepository()
        val handler = CommandHandler(
            AuthService(),
            userRepository,
            resourceRepository,
            permissionRepository
        )
        
        try {
            var result = handler.execute(args)
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
