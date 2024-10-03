package mx.ipn.escom.ia.cerradura.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mx.ipn.escom.ia.cerradura.service.CerraduraService;


@RestController
@RequestMapping("/cerradura")
public class CerraduraController{

	
	@GetMapping("/estrella/{numero}")
	@ResponseBody
	//Se utiliza el objeto Map, para que spring boot lo convierta automaticamente en JSON
	public Map<String, String> estrella(@PathVariable String numero) {
		
		try {
			int valor = Integer.parseInt(numero);
            CerraduraService service = new CerraduraService();
            return service.kleeneCerradura(valor);
		} catch (NumberFormatException e) {
			return Map.of("error", "Argumento no valido: " + numero);	
		}

	}

	@GetMapping("/positiva/{numero}")
	@ResponseBody
	public Map<String, String> positiva(@PathVariable String numero) {

		try {
			int valor = Integer.parseInt(numero);
            CerraduraService service = new CerraduraService();
            return service.kleeneClausuraPositiva(valor);
		} catch (NumberFormatException e) {
			return Map.of("error", "Argumento no valido: " + numero);	
		}
		
	}
}