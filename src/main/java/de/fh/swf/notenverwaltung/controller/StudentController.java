package de.fh.swf.notenverwaltung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@GetMapping("/notendurchschnitt")
	@ResponseBody
	public String berechnen() {
		
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.berechnen();		
		return s.getNotendurchschnitt();
	};
	
	@GetMapping("/creditpoints")
	@ResponseBody
	public String totalCreditpoints() {
		
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.berechnen();
	
		return String.valueOf(s.getSummeCreditpoints());
	}
	
}
