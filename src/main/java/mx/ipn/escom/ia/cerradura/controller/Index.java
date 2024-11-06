package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class Index {

	@RequestMapping("/")
	public String   home() {
		//ModelAndView modelAndView = new ModelAndView();
		//modelAndView.setViewName("login.html");
		//return modelAndView;
		return "redirect:/formlogin";
	}

}
