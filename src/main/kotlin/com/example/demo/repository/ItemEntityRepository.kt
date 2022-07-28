package com.example.demo.repository

import com.example.demo.entity.ItemEntity
import com.example.demo.util.dynamo.DynamoDBKey
import org.springframework.data.repository.CrudRepository

interface ItemEntityRepository : CrudRepository<ItemEntity, DynamoDBKey> {

}
