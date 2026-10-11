package de.fh.swf.notenverwaltung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.service.StudentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class StudentController {

	@Autowired
	StudentService studentService;

	@GetMapping("/notendurchschnitt")
	@ResponseBody
	public String berechnen() {
		return studentService.getNotendurchschnitt();
	}

	@GetMapping("/creditpoints")
	@ResponseBody
	public String totalCreditpoints() {
		return studentService.getSummeCreditpoints();
	}

}
