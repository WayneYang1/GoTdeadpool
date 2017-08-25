package com.tae.deadpool.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tae.deadpool.models.Character;
import com.tae.deadpool.models.User;
import com.tae.deadpool.services.CharacterService;
import com.tae.deadpool.services.UserService;
import com.tae.deadpool.validators.UserValidator;

@Controller
public class UserController {
	private UserService userService;
	private CharacterService characterService;
	
	private UserValidator userValidator;
	
	public UserController(UserService userService, UserValidator userValidator, CharacterService characterService) {
		this.userService = userService;
		this.characterService = characterService;
		this.userValidator = userValidator;
	}
    
    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
    		List<User> users = userService.findAll(); 
    		userValidator.validate(user, result);
    		if (result.hasErrors()) {
    			return "loginreg.jsp";
    		}
    		if(users.size() == 0) {
    			userService.saveUserWithAdminRole(user);
    			return "redirect:/admin";
    		} else {
    			userService.saveWithUserRole(user);
    			return "redirect:/";
    		}
    	}

    
    @RequestMapping("/admin")
    public String adminPage(Principal principal, Model model) {
    		String email = principal.getName();
    		List<User> allUsers = userService.findAll();
    		List<Character> allCharacters = characterService.findAll();
    		model.addAttribute("currentUser", userService.findByEmail(email));
    		model.addAttribute("users", allUsers);
    		model.addAttribute("allCharacters", allCharacters);
    		return "adminPage.jsp";
    }
    
    @GetMapping("/users/delete/{index}")
    public String deleteUser(@PathVariable("index") Long index) {
    		userService.deleteUser(index);
    		return "redirect:/admin";
    }
    
    @GetMapping("/users/makeAdmin/{index}")
    public String promote(@PathVariable("index") Long index) {
    		userService.makeAdmin(index);
    		return "redirect:/admin";
    }
    
    @RequestMapping("/login")
    public String login(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required=false) String logout, Model model) {
    		if(error != null) {
    			System.out.println(result.getAllErrors());
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successfull!");
        }
        System.out.println("LOGIN PASSED");
        return "loginreg.jsp";
    }
    
    @RequestMapping(value = {"/", "/dashboard"})
    public String home(Principal principal, Model model) {
    		System.out.println("WE ARE IN HOMEPAGE!");
        String email = principal.getName();
        User currentUser = userService.findByEmail(email);
        model.addAttribute("currentUser", currentUser);
        return "dashboard.jsp";
    }
    
}
