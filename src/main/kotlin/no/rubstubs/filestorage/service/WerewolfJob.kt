package no.rubstubs.filestorage.service

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI


/**
 * There is work to do here...
 */
@Service
class WerewolfJob: Job {
    @Throws(JobExecutionException::class)
    override fun execute(jobExecutionContext: JobExecutionContext) {
        val restTemplate = RestTemplate()

        val baseUrl = "http://localhost:8080/deleteAll"
        val uri = URI(baseUrl)
        restTemplate.postForEntity(uri, "", String::class.java)
        println("*HOOOWL* THE WEREWOLF IS AFOOT!")
    }
}