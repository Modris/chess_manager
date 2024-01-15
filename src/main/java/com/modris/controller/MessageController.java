package com.modris.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.modris.model.Fen;

@Controller
public class MessageController {

	private final RabbitTemplate rabbit;
	private final SimpMessageSendingOperations messageTemplate;

	@Autowired
	public MessageController(RabbitTemplate rabbit, SimpMessageSendingOperations messageTemplate) {
		this.rabbit = rabbit;
		this.messageTemplate = messageTemplate;
	}

	@MessageMapping("/websocket") // don't forget /app prefix. So /app/websocket for frontend
	// @SendToUser(value = "/topic/bestmove", broadcast = false) // will be
	// /user/topic/bestmove
	public void test(Fen fen) {
		System.out.println("UserId: " + fen.getUserId());
		System.out.println(fen.getFen());
		String response = (String) rabbit.convertSendAndReceive("main_exchange", "", fen.getFen());
		System.out.println("Received response from worker: " + response);
		messageTemplate.convertAndSend("/topic/bestmove" + fen.getUserId(), response);
		
	}

}
