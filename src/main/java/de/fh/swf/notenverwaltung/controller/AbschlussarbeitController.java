package de.fh.swf.notenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.entity.Abschlussarbeit;
import de.fh.swf.notenverwaltung.service.AbschlussarbeitService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class AbschlussarbeitController {

	@Autowired
	AbschlussarbeitService abschlussarbeitService;

	@GetMapping("/create/abschlussarbeit")
	public String createAbschlussarbeit(
			@RequestParam("thema") String name,
			@RequestParam("beschreibung") String beschreibung,
			@RequestParam("note1") String note1,
			@RequestParam("note2") String note2) {

		abschlussarbeitService.createAbschlussarbeit(name, beschreibung, note1, note2);
		return "redirect:/abschlussarbeit.html";
	}

	@GetMapping("/delete/abschlussarbeit/{id}")
	public void deleteAbschlussarbeit(@PathVariable Long id) {
		abschlussarbeitService.deleteAbschlussarbeit(id);
	    //reload after fetch in script
	}

	@GetMapping("/fetch/abschlussarbeit")
	@ResponseBody
	public List<Abschlussarbeit> fetchAbschlussarbeit(){
		return abschlussarbeitService.findAll();
	}

}
