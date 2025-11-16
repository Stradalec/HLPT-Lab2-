package database
import java.sql.Connection
import java.sql.DriverManager


object Database {
    private const val URL = "jdbc:h2:./data/coolDatabase;MODE=MySQL"
    private const val USER = "sa"
    private const val PASS = ""

    fun connection(): Connection = DriverManager.getConnection(URL, USER, PASS)
}