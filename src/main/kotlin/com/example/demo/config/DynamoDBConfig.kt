package com.example.demo.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.ConversionSchemas
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverterFactory
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@EnableDynamoDBRepositories(
    dynamoDBMapperConfigRef = "dynamoDBMapperConfig",
    basePackages = ["com.example.demo.repository"]
)
class DynamoDBConfig(
    private val aws: AppConfigManager.Aws
) {

    @Bean
    fun dynamoDBMapperConfig(tableNameOverrider: DynamoDBMapperConfig.TableNameOverride): DynamoDBMapperConfig =
        DynamoDBMapperConfig.builder()
            .withTypeConverterFactory(DynamoDBTypeConverterFactory.standard())
            .withTableNameResolver(DynamoDBMapperConfig.DefaultTableNameResolver.INSTANCE)
            .withTableNameOverride(tableNameOverrider())
            .withConversionSchema(ConversionSchemas.V2_COMPATIBLE).build()

    @Bean
    fun tableNameOverrider(): DynamoDBMapperConfig.TableNameOverride {
        return DynamoDBMapperConfig
            .TableNameOverride.withTableNameReplacement(aws.dynamoDBTableName)
    }

    private val localstackImage =
        DockerImageName.parse("localstack/localstack:latest")

    @Profile("default")
    @Bean(initMethod = "start", destroyMethod = "stop")
    fun localStackContainer(): LocalStackContainer {
        return LocalStackContainer(localstackImage).withServices(
            LocalStackContainer.Service.DYNAMODB
        )
    }

    @Profile("default")
    @Bean(name = ["amazonDynamoDB"])
    fun embeddedAmazonDynamoDB(localStackContainer: LocalStackContainer): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(
                localStackContainer.getEndpointConfiguration(
                    LocalStackContainer.Service.DYNAMODB
                )
            )
            .withCredentials(localStackContainer.defaultCredentialsProvider)
            .build()
    }

    @Profile("local")
    @Bean(name = ["amazonDynamoDB"])
    fun localAmazonDynamoDB(): AmazonDynamoDB =
        AmazonDynamoDBClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(aws.accessKey, aws.secretKey)))
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(aws.dynamodbEndpoint, aws.region))
            .build()

    @Profile("dev | prod")
    @Bean(name = ["amazonDynamoDB"])
    fun stageAmazonDynamoDB(): AmazonDynamoDB =
        AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()
}
