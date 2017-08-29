package com.tae.deadpool.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tae.deadpool.models.Character;
import com.tae.deadpool.services.CharacterService;
import com.tae.deadpool.services.PickService;


@Controller
public class AdminController {
	
	@Autowired
	CharacterService characterService;
	@Autowired
	PickService pickService;
	
	@GetMapping("/admin/characters/new")
	public String newCharacter(@ModelAttribute("char") Character character) {
		return "newChar.jsp";
	}
	
	@PostMapping("/admin/characters/new")
	public String createChar(@Valid @ModelAttribute("char") Character character, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/admin/characters/new";
		} else {
			characterService.saveChar(character);
			return "redirect:/admin/characters/new";
		}
	}
	
	@RequestMapping("/admin/characters/pages/{pageNumber}")
    public String getCharactersPerPage(Model model, @PathVariable("pageNumber") int pageNumber) {
        Page<Character> characters = characterService.charactersPerPage(pageNumber - 1);
        
        	int totalPages = characters.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("characters", characters);
        return "characterPages.jsp";
    }
    
   @GetMapping("/admin/characters/{characterId}/edit")
    public String characterInfo(Model model, @PathVariable("characterId") Long characterId) {
        Character newChar = characterService.getCharById(characterId);
        model.addAttribute("character", newChar);
        return "characterDetail.jsp";
    }
   
   @PostMapping("/admin/characters/{characterId}/edit")
   public String editCharacter(@Valid @ModelAttribute("character") Character character, BindingResult result, RedirectAttributes redirectAttributes, Model model, @PathVariable("characterId") Long characterId) {
       if(result.hasErrors()) {
    	   return "redirect:/admin/characters/{characterId}/edit";
       } else {
    	   characterService.updateCharacter(character, characterId);
    	   return "redirect:/admin/characters/{characterId}/edit";
    	   
       }
	   
   }
   
   @PostMapping("/admin/updateScores")
   public String updateScores(@RequestParam(value="killerId") Long killerId, @RequestParam(value="victimId") Long victimId) {
	   pickService.updateScores(victimId, killerId);
	   return "redirect:/admin";
   }

	
}
