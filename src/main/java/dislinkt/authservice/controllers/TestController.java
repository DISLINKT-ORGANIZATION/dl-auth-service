package dislinkt.authservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dislinkt.authservice.entities.Notification;
import dislinkt.authservice.entities.NotificationType;

import org.springframework.security.access.prepost.PreAuthorize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/test")
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Value("${server.port}")
	private String port;

	private KafkaTemplate<String, Notification> kafkaTemplate;

	public TestController(KafkaTemplate<String, Notification> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@GetMapping("/kafka")
	public ResponseEntity<String> sendNotification() {
		logger.info("Sending message...");
		Notification notification = new Notification(1L, 2L, 3L, "Notification Test", NotificationType.POST);
		kafkaTemplate.send("dislinkt-notifications", notification);
		return ResponseEntity.ok("FROM AUTH SERVICE, Port# is: " + port);
	}

	@PreAuthorize("hasRole('ROLE_AGENT')")
	@GetMapping("/agents-endpoint")
	public ResponseEntity<String> agent() {
		logger.info("Agent's endpoint...");
		return ResponseEntity.ok("AGENT'S ENDPOINT");
	}
}
