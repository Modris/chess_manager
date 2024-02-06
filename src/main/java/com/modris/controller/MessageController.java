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

import com.modris.model.ClientObject;
import com.modris.model.FenValidator;

import jakarta.validation.Valid;

@Controller
public class MessageController {
	private final static Logger logger = LoggerFactory.getLogger(MessageController.class);
	private final RabbitTemplate rabbit;
	private final SimpMessageSendingOperations messageTemplate;
	//private final ChessRepository chessRepository;
	private FenValidator fenValidator;
/*
	@Autowired
	public MessageController(RabbitTemplate rabbit, SimpMessageSendingOperations messageTemplate,
			ChessRepository chessRepository, FenValidator fenValidator) {
		this.rabbit = rabbit;
		this.messageTemplate = messageTemplate;
		this.chessRepository = chessRepository;
		this.fenValidator = fenValidator;
	}
*/

	@Autowired
	public MessageController(RabbitTemplate rabbit, SimpMessageSendingOperations messageTemplate,
			FenValidator fenValidator) {
		this.rabbit = rabbit;
		this.messageTemplate = messageTemplate;
		
		this.fenValidator = fenValidator;
	}

	@MessageMapping("/websocket") 
	public void test(@Valid @Payload ClientObject payload) {
		if(payload.getFen().equals("Ping")) {
			messageTemplate.convertAndSend("/topic/bestmove" + payload.getUserId(), "Pong");
		} else {
		
		if(fenValidator.isFenValid(payload.getFen())) {
		//	Chess history = new Chess(payload.getUserId(), payload.getFen(), payload.getMove());
		//OFFLOADING BEST MOVE CALL TO SPRING BACKEND WORKER WHICH WILL ANSWER TO THE USER
			//logger.info("Sending payload to main exchange.");
			rabbit.convertAndSend("main_exchange", "", payload);
	
		} else {
			//Invalid Fen. 
			logger.error(fenValidator.getErrorMessage());
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
