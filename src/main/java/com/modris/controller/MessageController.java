package com.modris.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

	@MessageMapping("/websocket") //don't forget /app prefix. So /app/websocket for frontend
	@SendTo("/exchange/main_exchange")
	public String test(String incoming) {
		System.out.println(incoming);
		System.out.println("r1bqkb1r/pppn1ppp/3p1n2/4p3/3PP3/2N2N2/PPP2PPP/R1BQKB1R w KQkq - 0 1");
		return "r1bqkb1r/pppn1ppp/3p1n2/4p3/3PP3/2N2N2/PPP2PPP/R1BQKB1R w KQkq - 0 1";
	}
}
