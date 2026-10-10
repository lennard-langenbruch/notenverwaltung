package de.fh.swf.notenverwaltung.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.entity.Wahlfach;
import de.fh.swf.notenverwaltung.repository.StudentRepository;
import de.fh.swf.notenverwaltung.repository.WahlfachRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class WahlfachController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	WahlfachRepository wahlfachRepository;
	
	@GetMapping("/create/wahlfach")
	public String createWahlfach(
			@RequestParam("fach") String name,
			@RequestParam("note1") String note1,
			@RequestParam("note2") String note2,
			@RequestParam("note3") String note3) {
		
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

		if (!note3.matches("(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")")) {
		    note3 = "";
		}
		
		Boolean passed = false;
		String endnote = "";
		if(note1.isEmpty() || note1.equals("5.0") == false ) { note2 = ""; }
		if(note1.isEmpty() || note2.isEmpty() || note1.equals("5.0") == false|| note2.equals("5.0") == false){ note3 = "";}
				
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
		
		if((note3.isEmpty() == false)) {
			Float fnote3 = Float.parseFloat(note3);
			if( ((fnote3 >= 1.0f && fnote3 <= 4.0f)))  {
				passed = true;
				endnote = note3;
			if(fnote3 == 5.0f)
				note3 = fnote3.toString();
			}
		}
		
		/*gucken ob fach mit dem namen existiert*/
		Wahlfach check = wahlfachRepository.findOneByFachname(name);
		Student user = studentRepository.findOneByNachname("langenbruch");
		
		if(check == null) {
			Wahlfach fach = new Wahlfach(name);
			fach.setStudent_wahl(user);
			fach.setNote1(note1);
			fach.setNote2(note2);
			fach.setNote3(note3);
			fach.setBestanden(passed);
			if(endnote.isEmpty() == false) {
				fach.setEndnote(endnote);
			}
			
			wahlfachRepository.save(fach);
		}
		
		if(check != null) {
			check.setNote1(note1);
			check.setNote2(note2);
			check.setNote3(note3);
			check.setBestanden(passed);
			if(endnote.isEmpty() == false) {
				check.setEndnote(endnote);
			}
			wahlfachRepository.save(check);
		}
				
		return "redirect:/index.html";
	}
	
	@GetMapping("/delete/wahlfach/{id}")
	public void deleteWahlfach(@PathVariable Long id) {
		wahlfachRepository.deleteById(id);
		Student s = studentRepository.findOneByNachname("langenbruch");
		s.berechnen();
	    //reload after fetch in script
	}
	
	@GetMapping("/fetch/wahlfach")
	@ResponseBody
	public List<Wahlfach> fetchWahlfach(){
		return wahlfachRepository.findAll();
	}
	
	@GetMapping("/fetch/wahlfach/single/{name}")
	@ResponseBody
	public List<Wahlfach> fetchSingleWahlfach(@PathVariable String name){
		
		List<Wahlfach> ret = new ArrayList<>();
		ret.add(this.wahlfachRepository.findOneByFachname(name));
		return ret;
	}
	
	@GetMapping("/bestanden")
	@ResponseBody
	public List<Wahlfach> bestanden(@PathVariable String name){
		
		List<Wahlfach> ret = new ArrayList<>();
		ret.add(this.wahlfachRepository.findOneByFachname(name));
		return ret;
	}
	
}
