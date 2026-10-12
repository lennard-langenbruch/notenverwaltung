package de.fh.swf.notenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fh.swf.notenverwaltung.entity.Wahlfach;
import de.fh.swf.notenverwaltung.service.WahlfachService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class WahlfachController {

	@Autowired
	WahlfachService wahlfachService;

	@GetMapping("/create/wahlfach")
	public String createWahlfach(
			@RequestParam("fach") String name,
			@RequestParam("note1") String note1,
			@RequestParam("note2") String note2,
			@RequestParam("note3") String note3) {

		wahlfachService.createWahlfach(name, note1, note2, note3);
		return "redirect:/index.html";
	}

	@GetMapping("/delete/wahlfach/{id}")
	public void deleteWahlfach(@PathVariable Long id) {
		wahlfachService.deleteWahlfach(id);
	    //reload after fetch in script
	}

	@GetMapping("/fetch/wahlfach")
	@ResponseBody
	public List<Wahlfach> fetchWahlfach(){
		return wahlfachService.findAll();
	}

	@GetMapping("/fetch/wahlfach/single/{name}")
	@ResponseBody
	public List<Wahlfach> fetchSingleWahlfach(@PathVariable String name){
		return wahlfachService.findByFachname(name);
	}

	@GetMapping("/bestanden")
	@ResponseBody
	public List<Wahlfach> bestanden(@PathVariable String name){
		return wahlfachService.findByFachname(name);
	}

}
