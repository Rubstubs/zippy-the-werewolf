package no.rubstubs.filestorage.thread

import no.rubstubs.filestorage.service.MailService
import no.rubstubs.filestorage.service.StorageService
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import java.io.File

@Component
@Scope("prototype")
class FileThread(
        private val email: String,
        private val files: Array<File>,
        private val storageService: StorageService,
        private val emailService: MailService,
        private val env: Environment
    ) : Thread() {

    override fun run() {
        try {
            val file = storageService.zip(files)
            val id = storageService.saveToDb(file)
            emailService.sendMail(email, "${env["LINK_URL"]}$id")
        } catch (e: Exception) {
            println(e)
            emailService.sendApologyMail(email)
        }
        storageService.clearTempFolders()
        println("All done")
    }
}