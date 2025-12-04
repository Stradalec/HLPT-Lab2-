package com.HLPTLab7.ExplorerApp.database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

@Configuration
class DataSourceConfig {

    @Bean
    fun dataSource(): DataSource {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:h2:./data/coolDatabase;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE"
            username = "sa"
            password = ""
            driverClassName = "org.h2.Driver"
            maximumPoolSize = 10
            validate()
        }
        return HikariDataSource(config)
    }
}