package no.rubstubs.filestorage.controller

import no.rubstubs.filestorage.config.AppConfig
import no.rubstubs.filestorage.service.MailService
import no.rubstubs.filestorage.service.StorageService
import no.rubstubs.filestorage.thread.FileThread
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.env.Environment
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import javax.servlet.http.HttpServletResponse

@RestController
class StorageController(
        @Autowired private val storageService: StorageService,
        @Autowired private val emailService: MailService,
        @Autowired private val env: Environment,
        @Autowired private val ctx: ApplicationContext = AnnotationConfigApplicationContext(AppConfig::class.java)
) {

    @GetMapping("/")
    fun getIndex(
        httpServletResponse: HttpServletResponse
    ) {
        httpServletResponse.status = 200
        httpServletResponse.sendRedirect("/index.html")
    }

    @PostMapping("/")
    fun handleSaveFile(
        httpServletResponse: HttpServletResponse,
        @RequestParam("files") files: Array<MultipartFile>,
        @RequestParam("email") email: String
    ) {
        storageService.clearTempFolders()

        // Saving incoming files in tmp folder
        for (file in files) {
            file.originalFilename?.let {
                val multipartFile: MultipartFile = MockMultipartFile(it, file.inputStream)
                val mewFile = File("src/main/resources/tmpUpload/$it")
                FileOutputStream(mewFile).use { os -> os.write(multipartFile.bytes) }
            }
        }

        // Reading saved files from tmp folder
        val fileList = File("src/main/resources/tmpUpload").listFiles() as Array<File>

        // Start new thread before closing post request. This thread runs necessary methods to save zipped files in db.
        val fileThread: FileThread = ctx.getBean("fileThread", email, fileList, storageService, emailService, env) as FileThread

        // Closing post request
        httpServletResponse.status = 200
        httpServletResponse.sendRedirect("/filesSubmitted.html")
        fileThread.start()
    }

    @GetMapping("/files/{objectId}")
    fun downloadFile(@PathVariable objectId: String): ResponseEntity<Resource> {
        val file = storageService.loadFile(objectId)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + ".zip" + "\"")
            .body(file)
    }

    @PostMapping("/deleteAll")
    fun handleDeleteAll() {
        storageService.cleanDb()
    }
}