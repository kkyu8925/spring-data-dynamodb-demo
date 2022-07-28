package com.example.demo.util.dynamo

import com.amazonaws.services.dynamodbv2.datamodeling.*
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

abstract class DynamoDBBaseEntity(

    key: DynamoDBKey,

    @DynamoDBAttribute
    var title: String,

) : DynamoDBKeyEntity(key)

@DynamoDBDocument
abstract class DynamoDBKeyEntity(

    @Id
    @DynamoDBIgnore
    var key: DynamoDBKey = DynamoDBKey(),

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter::class)
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {

    @DynamoDBHashKey
    fun getPartitionKey() = key.PartitionKey

    fun setPartitionKey(partitionKey: String) {
        key.PartitionKey = partitionKey
    }

    @DynamoDBRangeKey
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "idx_global_sortKey")
    fun getSortKey() = key.SortKey

    fun setSortKey(sortKey: String) {
        key.SortKey = sortKey
    }
}

data class DynamoDBKey(

    @get:DynamoDBHashKey
    var PartitionKey: String = "",

    @get:DynamoDBRangeKey
    @get:DynamoDBIndexHashKey(globalSecondaryIndexName = "idx_global_sortKey")
    var SortKey: String = "",
)
