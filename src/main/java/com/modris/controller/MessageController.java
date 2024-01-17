package com.modris.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.modris.model.Chess;
import com.modris.model.ClientObject;
import com.modris.repositories.ChessRepository;

@Controller
public class MessageController {

	private final RabbitTemplate rabbit;
	private final SimpMessageSendingOperations messageTemplate;
	private final ChessRepository chessRepository;

	@Autowired
	public MessageController(RabbitTemplate rabbit, SimpMessageSendingOperations messageTemplate,
			ChessRepository chessRepository) {
		this.rabbit = rabbit;
		this.messageTemplate = messageTemplate;
		this.chessRepository = chessRepository;
	}

	@MessageMapping("/websocket") // don't forget /app prefix. So /app/websocket for frontend
	// @SendToUser(value = "/topic/bestmove", broadcast = false) // will be
	// user/topic/bestmove
	public void test(ClientObject payload) {
		// Fen validation in the future here
		Chess history = new Chess(payload.getUserId(), payload.getFen(), payload.getMove());
		if(!payload.getMove().equals("begn")) {
			//chessRepository.save(history); //user move. Save into history. Dont ask for engine move
		} else {
		String response = (String) rabbit.convertSendAndReceive("main_exchange", "", payload);
		System.out.println("Received response from worker: " + response);
		messageTemplate.convertAndSend("/topic/bestmove" + payload.getUserId(), response);
		history.setMove(response);
		chessRepository.save(history);
		}
	}

}
