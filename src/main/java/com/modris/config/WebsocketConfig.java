package com.modris.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer{

	@Value("${RABBITMQ_USERNAME}")
	private String usernameRabbit;
	
	@Value("${RABBITMQ_PASSWORD}")
	private String passwordRabbit;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket")
				.setAllowedOriginPatterns("*");
				//.withSockJS();
		
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableStompBrokerRelay("/topic","/exchange","/queue")
				//.setRelayHost("localhost")
				.setRelayHost("rabbitmq")
				.setRelayPort(61613)
				.setClientLogin(usernameRabbit)
				.setClientPasscode(passwordRabbit)
				.setSystemLogin(usernameRabbit)
				.setSystemPasscode(passwordRabbit);
		config.setApplicationDestinationPrefixes("/app");
	}
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(messageConverter);
	    
	   
	    return rabbitTemplate;
	}

	@Bean
	public MessageConverter messageConverter(ObjectMapper jsonMapper) {
	    return new Jackson2JsonMessageConverter(jsonMapper);
	    
	}
	
}
