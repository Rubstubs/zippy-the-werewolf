package no.rubstubs.filestorage.repository

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.GridFSBuckets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

/**
 * Makes a connection with MongoDB. GridFS is necessary for big files.
 */
@Repository
class GridFSRepo (
    @Autowired private val env: Environment
        ) {
    fun makeConnectionWithGridFS(): GridFSBucket {
        val connectionString =
            ConnectionString("${env["spring.data.mongodb.uri"]}")
        val settings = MongoClientSettings
            .builder()
            .applyConnectionString(connectionString)
            .build()
        val mongoClient = MongoClients.create(settings)
        val db: MongoDatabase = mongoClient.getDatabase("zipper")
        return GridFSBuckets.create(db)
    }
}