package mx.ipn.escom.ia.cerradura.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.ipn.escom.ia.cerradura.service.CerraduraService;


@RestController
@RequestMapping("/cerradura")
public class CerraduraController{

	
	@GetMapping("/estrella/{numero}")
	public String estrella(@PathVariable int numero) {
		CerraduraService service = new CerraduraService();
		Map<String, String> response = service.kleeneCerradura(numero);
		return ""+response;
	}

	@GetMapping("/positiva/{numero}")
	public String positiva(@PathVariable int numero) {
		CerraduraService service = new CerraduraService();
		Map<String, String> response = service.kleeneClausuraPositiva(numero);
		return ""+response;
	}

	

}
