package de.fh.swf.notenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.entity.Kolloqium;
import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.KolloqiumRepository;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class KolloqiumController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	KolloqiumRepository kolloqiumRepository;
	
	@GetMapping("/create/kolloqium")
	public String createKolloqium(
			@RequestParam("thema") String name,
			@RequestParam("beschreibung") String beschreibung,
			@RequestParam("note1") String note1,
			@RequestParam("note2") String note2) {
		
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
		if(s.getKolloqium() == null) {
			Boolean bestanden = false;
			Kolloqium check = kolloqiumRepository.findOneByThema(name);
			
			if(check == null) {
				Kolloqium k = new Kolloqium(name);
				k.setBeschreibung(beschreibung);
				
				
				k.setNote1(note1);
				k.setNote2(note2);
				
				k.setBestanden(passed);
				if(endnote.isEmpty() == false) {
					k.setEndnote(endnote);
				}
				
				
				k.setStudent(s);
				s.setKolloqium(k);
				kolloqiumRepository.save(k);
			}
			
			if(check != null) {
				check.setBeschreibung(beschreibung);
				
				check.setNote1(note1);
				check.setNote2(note2);
				
				kolloqiumRepository.save(check);
			}	
		}
		
		return "redirect:/kolloqium.html";
	};
	
	@GetMapping("/delete/kolloqium/{id}")
	public void deleteKolloqium(@PathVariable Long id) {
		kolloqiumRepository.deleteById(id);
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.setKolloqium(null);
		s.berechnen();
	    //reload after fetch in script
	}
	
	@GetMapping("/fetch/kolloqium")
	@ResponseBody
	public List<Kolloqium> fetchKolloqium(){
		return kolloqiumRepository.findAll();
	}
	
}
