package fr.hes.raynaudmonitoring.configuration;



import org.springframework.boot.context.properties.EnableConfigurationProperties;



@EnableConfigurationProperties({CouchbaseSetting.class /* other setting classes */})
public class AppConfiguration {

}
