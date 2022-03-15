package no.rubstubs.filestorage.service

import kotlin.Throws
import org.quartz.SchedulerException
import org.quartz.SchedulerFactory
import org.quartz.impl.StdSchedulerFactory
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.CronScheduleBuilder
import org.springframework.stereotype.Service

@Service
class WerewolfScheduler {
    /**
     * Weird cron, reference examples here http://www.quartz-scheduler.org/documentation/quartz-2.3.0/examples/Example3.html
     */
    @Throws(SchedulerException::class)
    fun midnightDestruction() {
        val sf: SchedulerFactory = StdSchedulerFactory()
        val job = JobBuilder.newJob(WerewolfJob::class.java)
            .withIdentity("job1", "group1")
            .build()
        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("trigger1", "group1")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0am * * ?"))
            .build()
        sf.scheduler.scheduleJob(job, trigger)
        sf.scheduler.start()
    }
}