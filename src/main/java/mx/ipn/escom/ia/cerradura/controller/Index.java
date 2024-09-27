package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController

public class Index {

	@RequestMapping("/")
	String home() {
		return "Hola mundo con controller";
	}

}
