package dislinkt.authservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic dislinktTopic() {
		return TopicBuilder.name("dislinkt-notifications").build();
	}
	
	@Bean
	public NewTopic eventsTopic() {
		return TopicBuilder.name("dislinkt-events").build();
	}
}
