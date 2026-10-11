package de.fh.swf.notenverwaltung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fh.swf.notenverwaltung.entity.Abschlussarbeit;
import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.AbschlussarbeitRepository;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@Service
public class AbschlussarbeitService {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	AbschlussarbeitRepository abschlussarbeitRepository;
	
	public void createAbschlussarbeit(String name, String beschreibung, String note1, String note2) {
		
		/*zweistellige fließkommazahl*/
		String regex1 = "^[1-3]\\.[0-9]";
		String regex2 = "^[5]\\.[0]";
		String regex3 = "^[4]\\.[0]";

		if (!note1.matches("(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")")) {
		    note1 = "";
		}

		if (!note2.matches("(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")")) {
		    note2 = "";
		}

		Boolean passed = false;
		String endnote = "";
		if(note1.isEmpty() || note1.equals("5.0") == false ) { note2 = ""; }
				
		if(note1.isEmpty() == false) { // wenn empty dann gar nichts
			Float fnote1 = Float.parseFloat(note1);
			if(( fnote1 >= 1.0f && fnote1 <= 4.0f))  {
				passed = true;
				endnote = note1;
			if(fnote1 == 5.0f )
				note1 = fnote1.toString();
			}
		}
		
		// also wenn das eintritt dann lässt er den inout so wie war
		
		if((note2.isEmpty() == false)) { // wenn nicht empty aber note1 auch nicht empty und note1 aber auch nicht
			Float fnote2 = Float.parseFloat(note2);		
			if( (fnote2 >= 1.0f && fnote2 <= 4.0f))   {
				passed = true;
				endnote = note2;
			if(fnote2 == 5.0f)
				note2 = fnote2.toString();
			}
		}
		
		//
		
		Student s = studentRepository.findOneByNachname("langenbruch");
		if(s.getAbschlussarbeit() == null) {	
			Boolean bestanden = false;
			Abschlussarbeit check = abschlussarbeitRepository.findOneByThema(name);
			
			if(check == null) {
				Abschlussarbeit a = new Abschlussarbeit(name);
				a.setBeschreibung(beschreibung);
				
				a.setNote1(note1);
				a.setNote2(note2);
				
				a.setBestanden(passed);
				if(endnote.isEmpty() == false) {
					a.setEndnote(endnote);
				}
				
				a.setStudent(s);
				s.setAbschlussarbeit(a);
				abschlussarbeitRepository.save(a);
			}
			
			if(check != null) {
				check.setBeschreibung(beschreibung);
				
				check.setNote1(note1);
				check.setNote2(note2);
				
				abschlussarbeitRepository.save(check);
			}
		}
	}
	
	public void deleteAbschlussarbeit(Long id) {
		abschlussarbeitRepository.deleteById(id);
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.setAbschlussarbeit(null);
		s.berechnen();
	}
	
	public List<Abschlussarbeit> findAll() {
		return abschlussarbeitRepository.findAll();
	}
	
}
