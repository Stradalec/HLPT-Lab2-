package database
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseConnectionException(cause: Throwable) : RuntimeException(cause)

object Database {
    private const val URL = "jdbc:h2:./data/coolDatabase;MODE=MySQL"
    private const val USER = "sa"
    private const val PASS = ""

    fun connection(): Connection =
        try {
            DriverManager.getConnection(URL, USER, PASS)
        } catch (e: SQLException) {
            throw DatabaseConnectionException(e)
        }
}