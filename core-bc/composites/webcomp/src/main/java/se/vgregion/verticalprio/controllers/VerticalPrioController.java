package se.vgregion.verticalprio.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VerticalPrioController {
	private static final Log log = LogFactory.getLog(VerticalPrioController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String test() {
		log.info("in test() method");
		return "test";
	}
}
