package no.rubstubs.filestorage

import no.rubstubs.filestorage.service.FolderSetupService
import no.rubstubs.filestorage.service.WerewolfScheduler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FileStorageApplication

fun main(args: Array<String>) {
	runApplication<FileStorageApplication>(*args)
	FolderSetupService().setupFolders()
	WerewolfScheduler().midnightDestruction()
}
