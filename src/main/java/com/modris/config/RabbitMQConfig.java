package com.modris.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig  {

	@Value("${RABBITMQ_USERNAME}")
	private String usernameRabbit;
	
	@Value("${RABBITMQ_PASSWORD}")
	private String passwordRabbit;
		
	// Exchange and Queues
	@Bean
	public DirectExchange direct() {
		return new DirectExchange("main_exchange");
	}
	
	@Bean
	public Queue queue1() {
		return new Queue("direct_bestmove");
	}
	
	@Bean
	public Binding binding1(DirectExchange direct, Queue queue1) {
		return BindingBuilder.bind(queue1).to(direct).with("");
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("rabbitmq");
		connectionFactory.setUsername(usernameRabbit);
		connectionFactory.setPassword(passwordRabbit);
		return connectionFactory;
	}
}
