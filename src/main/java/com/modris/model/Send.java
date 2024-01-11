package com.modris.model;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Send {

	private final RabbitTemplate rabbit;

	@Autowired
	public Send(RabbitTemplate rabbit) {
		this.rabbit = rabbit;
	}

	public void sendOrder() {
		System.out.println("We sending");
		String fen = "r1bqkb1r/pppn1ppp/3p1n2/4p3/3PP3/2N2N2/PPP2PPP/R1BQKB1R w KQkq - 0 1";
		String response = (String) rabbit.convertSendAndReceive("main_exchange", "", fen);
		System.out.println(response);

	}
/*
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		sendOrder();
	}
*/
}
