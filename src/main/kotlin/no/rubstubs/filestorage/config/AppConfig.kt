package no.rubstubs.filestorage.config
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Setting up for running something on a separate thread
 */
@Configuration
@ComponentScan(basePackages = ["no.rubstubs.filestorage.thread"])
class AppConfig