package com.core.chatgpt.gpt.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class DbConfig {
    @Value("\${spring.datasource.url}")
    private val dbUrl: String? = null

    @Value("\${spring.datasource.username}")
    private val dbUsername: String? = null

    @Value("\${spring.datasource.password}")
    private val dbPassword: String? = null

    @Bean
    fun dataSource(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = dbUrl
        config.username = dbUsername
        config.password = dbPassword
        return HikariDataSource(config)
    }
}