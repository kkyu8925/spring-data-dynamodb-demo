package com.example.demo.repository

import com.example.demo.entity.ItemEntity
import com.example.demo.util.dynamo.DynamoDBKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ItemEntityRepositoryTest {

    @Autowired
    private lateinit var itemEntityRepository: ItemEntityRepository

    @Test
    fun create() {
        val key = DynamoDBKey("pk", "sk")
        val itemEntity = ItemEntity(key = key, title = "title")
        itemEntityRepository.save(itemEntity)
    }
}
