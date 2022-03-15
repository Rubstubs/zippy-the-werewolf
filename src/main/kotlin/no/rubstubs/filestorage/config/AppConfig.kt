package no.rubstubs.filestorage.config
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = ["no.rubstubs.filestorage.thread"])
class AppConfig