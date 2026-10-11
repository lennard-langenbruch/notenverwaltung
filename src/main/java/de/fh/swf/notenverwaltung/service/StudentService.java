package de.fh.swf.notenverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	public String getNotendurchschnitt() {
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.berechnen();
		return s.getNotendurchschnitt();
	}
	
	public String getSummeCreditpoints() {
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.berechnen();
		return String.valueOf(s.getSummeCreditpoints());
	}
	
}
