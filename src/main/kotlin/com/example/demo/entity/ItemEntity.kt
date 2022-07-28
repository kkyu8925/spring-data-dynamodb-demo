package com.example.demo.entity

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.example.demo.util.dynamo.DynamoDBBaseEntity
import com.example.demo.util.dynamo.DynamoDBKey

@DynamoDBTable(tableName = "table")
class ItemEntity(
    key: DynamoDBKey,
    title: String,
) : DynamoDBBaseEntity(key = key, title = title) {

}
