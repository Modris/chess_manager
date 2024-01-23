package com.modris.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.modris.model.Chess;
import com.modris.model.ClientObject;
import com.modris.model.FenValidator;
import com.modris.repositories.ChessRepository;

import jakarta.validation.Valid;

@Controller
public class MessageController {
	private final static Logger logger = LoggerFactory.getLogger(MessageController.class);
	private final RabbitTemplate rabbit;
	private final SimpMessageSendingOperations messageTemplate;
	private final ChessRepository chessRepository;
	private FenValidator fenValidator;
	
	@Autowired
	public MessageController(RabbitTemplate rabbit, SimpMessageSendingOperations messageTemplate,
			ChessRepository chessRepository, FenValidator fenValidator) {
		this.rabbit = rabbit;
		this.messageTemplate = messageTemplate;
		this.chessRepository = chessRepository;
		this.fenValidator = fenValidator;
	}

	@MessageMapping("/websocket") 
	public void test(@Valid @Payload ClientObject payload) {
		if(payload.getFen().equals("Ping")) {
			//System.out.println("Sending back Pong!");
			messageTemplate.convertAndSend("/topic/bestmove" + payload.getUserId(), "Pong");
		} else {
		
		if(fenValidator.isFenValid(payload.getFen())) {
			logger.info("Valid fen!");
			Chess history = new Chess(payload.getUserId(), payload.getFen(), payload.getMove());
			System.out.println(payload.toString());
			String response = (String) rabbit.convertSendAndReceive("main_exchange", "", payload);
			System.out.println("Received response from worker: " + response);
			messageTemplate.convertAndSend("/topic/bestmove" + payload.getUserId(), response);
			history.setMove(response);
			//chessRepository.save(history);
		} else {
			logger.info("Invalid fen!");
			messageTemplate.convertAndSend("/topic/bestmove" + payload.getUserId(), "Invalid fen!");
		}
		
		}	
	}
	

	@MessageExceptionHandler({MethodArgumentNotValidException.class,
								MessageConversionException.class})
	public void handleExceptions(Throwable t) {
	logger.error("Error handling message: " + t.getMessage());
	}
}
