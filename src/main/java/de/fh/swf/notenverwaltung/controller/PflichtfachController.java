package de.fh.swf.notenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.entity.Pflichtfach;
import de.fh.swf.notenverwaltung.service.PflichtfachService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class PflichtfachController {

	@Autowired
	PflichtfachService pflichtfachService;

	@GetMapping("/create/fach")
	public String createFach(
			@RequestParam("semester") String semester,
			@RequestParam("creditpoints") String creditpoints,
			@RequestParam("fach") String name,
			@RequestParam("note1") String note1,
			@RequestParam("note2") String note2,
			@RequestParam("note3") String note3) {

		pflichtfachService.createFach(semester, creditpoints, name, note1, note2, note3);
		return "redirect:/index.html";
	}

	@GetMapping("/delete/fach/{id}")
	public void deleteFach(@PathVariable Long id) {
		pflichtfachService.deleteFach(id);
	    //reload after fetch in script
	}

	@GetMapping("/fetch/fach")
	@ResponseBody
	public List<Pflichtfach> fetchFach(){
		return pflichtfachService.findAll();
	}

	@GetMapping("/fetch/fach/single/{name}")
	@ResponseBody
	public List<Pflichtfach> fetchSingleFach(@PathVariable String name){
		return pflichtfachService.findByFachname(name);
	}

}
