package fr.hes.raynaudmonitoring.backend.backend.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;



@EnableConfigurationProperties({CouchbaseSetting.class /* other setting classes */})
public class AppConfiguration {

}
