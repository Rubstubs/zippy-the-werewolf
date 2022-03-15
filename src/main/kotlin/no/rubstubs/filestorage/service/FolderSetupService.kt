package no.rubstubs.filestorage.service

import org.springframework.stereotype.Service
import java.io.File

/**
 * Sets up tmp folders when server starts
 */
@Service
class FolderSetupService {
    private val tmpUploadLocationString = "src/main/resources/tmpUpload"
    private val tmpDownloadLocationString = "src/main/resources/tmpDownload"

    fun setupFolders() {
        if (File(tmpUploadLocationString).mkdirs()) println("$tmpUploadLocationString created")
        if (File(tmpDownloadLocationString).mkdirs()) println("$tmpDownloadLocationString created")
    }
}