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
import de.fh.swf.notenverwaltung.service.KolloqiumService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class KolloqiumController {

	@Autowired
	KolloqiumService kolloqiumService;

	@GetMapping("/create/kolloqium")
	public String createKolloqium(
			@RequestParam("thema") String name,
			@RequestParam("beschreibung") String beschreibung,
			@RequestParam("note1") String note1,
			@RequestParam("note2") String note2) {

		kolloqiumService.createKolloqium(name, beschreibung, note1, note2);
		return "redirect:/kolloqium.html";
	}

	@GetMapping("/delete/kolloqium/{id}")
	public void deleteKolloqium(@PathVariable Long id) {
		kolloqiumService.deleteKolloqium(id);
	    //reload after fetch in script
	}

	@GetMapping("/fetch/kolloqium")
	@ResponseBody
	public List<Kolloqium> fetchKolloqium(){
		return kolloqiumService.findAll();
	}

}
