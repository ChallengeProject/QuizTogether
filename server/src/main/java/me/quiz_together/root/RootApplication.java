package me.quiz_together.root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RootApplication  {

    public static void main(String[] args) {
        SpringApplication.run(RootApplication.class, args);
    }

//    @Autowired
//    private ConfigurableEnvironment env;
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
//        return objectMapper;
//    }
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer configurer() {
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new ClassPathResource("application.yml"));
//        PropertySourcesPlaceholderConfigurer holder = new PropertySourcesPlaceholderConfigurer();
//        holder.setPropertiesArray(yaml.getObject());
//        holder.setIgnoreResourceNotFound(true);
//        return holder;
//    }

    @Value("${fcm.apiKey}")
    private String apiKey;

}