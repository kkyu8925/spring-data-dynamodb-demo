package com.example.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AppConfigManager.Aws::class)
class AppConfigManager {

    @ConstructorBinding
    @ConfigurationProperties(prefix = "aws")
    class Aws private constructor(
        val accessKey: String,
        val secretKey: String,
        val region: String,
        val dynamodbEndpoint: String,
        val dynamoDBTableName: String,
    )
}
