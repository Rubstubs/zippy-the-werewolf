package no.rubstubs.filestorage.service

import com.mongodb.client.gridfs.model.GridFSUploadOptions
import no.rubstubs.filestorage.repository.GridFSRepo
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.io.*
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.name

@Service
class StorageService(
        @Autowired private val mongoRepo: GridFSRepo
) {
    private val tmpDownloadLocation: Path = Paths.get("src/main/resources/tmpDownload")
    private val tmpUploadLocation: Path = Paths.get("src/main/resources/tmpUpload")
    private val gridFSBucket = mongoRepo.makeConnectionWithGridFS()

    fun cleanDb() {
        gridFSBucket.drop()
        println("Database dropped")
    }

    /**
     * @param objectIdString
     * @return Resource
     */
    fun loadFile(objectIdString: String): Resource {
        val objectId = ObjectId(objectIdString)
        try {
            val streamToDownloadTo = FileOutputStream("$tmpDownloadLocation/$objectId")
            gridFSBucket.downloadToStream(objectId, streamToDownloadTo)
            streamToDownloadTo.close()
        } catch (e: IOException) {
            println(e)
        }

        val file = tmpDownloadLocation.resolve("$objectId")
        val resource = UrlResource(file.toUri())
        println("Uploaded from database: ${file.name}")

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("Cant find resource!")
        }
    }

    /**
     * Iterates through tmp folders and deletes all files
     */
    fun clearTempFolders() {
        File(tmpUploadLocation.toUri()).listFiles()?.let {
            for (file in it) {
                if (!file.isDirectory) {
                    file.delete()
                    println("deleted \"${file.name}\" from $tmpUploadLocation")
                }
            }
        }
        File(tmpDownloadLocation.toUri()).listFiles()?.let {
            for (file in it) {
                if (!file.isDirectory) {
                    file.delete()
                    println("deleted \"${file.name}\" from $tmpDownloadLocation")
                }
            }
        }
    }

    fun zip(files: Array<File>): File {
        print("Zipping:\n")
        val tmpFile = File.createTempFile("file", ".tmp")
        val fos = FileOutputStream(tmpFile)
        val zipOut = ZipOutputStream(fos)

        files.forEach { file ->
            println("\t\"${file.name}\"")
            zipOut.putNextEntry(ZipEntry(file.name))
            val fis = file.inputStream()
            val bytes = byteArrayOf(1024.toByte())
            var length = fis.read(bytes)
            while (length >= 0) {
                zipOut.write(bytes, 0, length)
                length = fis.read(bytes)
            }
            fis.close()
        }

        zipOut.close()
        fos.close()
        println("Done zipping")
        return tmpFile
    }

    fun saveToDb(file: File): ObjectId {
        try {
            val streamToUploadFrom: InputStream = FileInputStream(file)
            val options = GridFSUploadOptions()
                    .chunkSizeBytes(358400)
            println("Saving zip to database...")
            return gridFSBucket.uploadFromStream(file.name, streamToUploadFrom, options)
        } catch (e: FileNotFoundException) {
            println(e)
        }
        return ObjectId()
    }

}