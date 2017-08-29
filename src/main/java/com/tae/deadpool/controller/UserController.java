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
import com.tae.deadpool.models.Deadpool;
import com.tae.deadpool.models.Pick;
import com.tae.deadpool.models.User;
import com.tae.deadpool.models.userDeadpool;
import com.tae.deadpool.services.CharacterService;
import com.tae.deadpool.services.DeadpoolService;
import com.tae.deadpool.services.PickService;
import com.tae.deadpool.services.UserDeadpoolService;
import com.tae.deadpool.services.UserService;
import com.tae.deadpool.validators.UserValidator;

@Controller
public class UserController {
	private UserService userService;
	private CharacterService characterService;
	private DeadpoolService deadpoolService;
	private PickService pickService;
	private UserDeadpoolService userDeadpoolService;
	
	private UserValidator userValidator;
	
	public UserController(UserDeadpoolService userDeadpoolService,UserService userService, UserValidator userValidator, CharacterService characterService, DeadpoolService deadpoolService, PickService pickService) {
		this.userService = userService;
		this.pickService = pickService;
		this.characterService = characterService;
		this.deadpoolService = deadpoolService;
		this.userValidator = userValidator;
		this.userDeadpoolService = userDeadpoolService;
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
    
    //start of waynes
    
    @RequestMapping("/users/createPool")
    public String poolForm(@ModelAttribute("deadpool") Deadpool deadpool, Principal principal, Model model) {
    		String email = principal.getName();
    		User host = userService.findByEmail(email);
    		model.addAttribute("host", host);
    		return "createPool.jsp";
    }
    
    @PostMapping("/users/processPool")
    public String makePool(@Valid @ModelAttribute("deadpool") Deadpool deadpool, BindingResult result, @RequestParam(value="hostId") Long hostId) {
    		if(result.hasErrors()) {
    			return "redirect:/users/createPool";
    		} else {
        		User host = userService.findById(hostId);
        		deadpoolService.saveDeadpool(deadpool, host);
    		}
    		return "redirect:/users/deadpool/" + deadpool.getId();
    }
    
    @PostMapping("/users/addUser")
    public String inviteUser(@RequestParam("invitedUserId") Long invitedUserId, @RequestParam("deadpoolId") Long deadpoolId) {
    		User invitedUser = userService.findById(invitedUserId);
    		Deadpool deadpool = deadpoolService.getDeadpoolById(deadpoolId);
    		deadpoolService.inviteUser(deadpool, invitedUser);
    		return "redirect:/users/deadpool/" + deadpool.getId();
    }
    
    @RequestMapping("/users/deadpool/{index}")
    public String showPool(@PathVariable("index") Long deadpoolId, Model model, Principal principal) {
    String email = principal.getName();
    User currentUser = userService.findByEmail(email);
        List<User> allUsers = userService.findAll();
        Deadpool deadpool = deadpoolService.getDeadpoolById(deadpoolId);
        model.addAttribute("deadpool", deadpool);
        List<User> allUsersInvitedPool = deadpool.getInvitedUsersInDeadpool();
        List<User> allUsersInPool = deadpool.getUsersInDeadpool();
        allUsers.removeAll(allUsersInPool);
        allUsers.removeAll(allUsersInvitedPool);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allUsersInPool", allUsersInPool);
        User host = deadpool.getHost();
        model.addAttribute("host", host);
        model.addAttribute("currentUser", currentUser);
        
        
        model.addAttribute("ud", userDeadpoolService.findAll());
        
        
        return "showPool.jsp";
    }
    
    @RequestMapping("/users/deadpool/{deadpoolId}/accept")
    public String acceptInvite(@PathVariable("deadpoolId") Long deadpoolId, Principal principal) {
    		String email = principal.getName();
    		User user = userService.findByEmail(email);
    		Deadpool deadpool = deadpoolService.getDeadpoolById(deadpoolId);
    		deadpoolService.addUser(deadpool, user);
    		return "redirect:/users/deadpool/" + deadpoolId;
    }
    
    //end of waynes
    
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
        String email = principal.getName();
        User currentUser = userService.findByEmail(email);
        model.addAttribute("currentUser", currentUser);
        return "dashboard.jsp";
    }
    
    @GetMapping("/users/deadpool/{deadpoolId}/addpicks")
    public String addPicks (Principal principal, Model model, @PathVariable("deadpoolId") Long deadpoolId, @ModelAttribute("pick") Pick pick) {
    	String email = principal.getName();
    	User user = userService.findByEmail(email);
		List<Character> allCharacters = characterService.findAll();
		Deadpool currentDeadpool = deadpoolService.findById(deadpoolId);
		Long userId = user.getId();
		
//		List<Object[]> userPicks = pickService.getUsersPicks(userId, deadpoolId);
		
		model.addAttribute("currentUser", user);
		model.addAttribute("allCharacters", allCharacters);
		model.addAttribute("deadpool", currentDeadpool);
//		model.addAttribute("usersPicksInDeadpool", userPicks);
		
    	return "addPicks.jsp";
    }
    
    @PostMapping("/users/deadpool/{deadpoolId}/addpicks")
    public String createPicks (@Valid @ModelAttribute("pick") Pick pick, BindingResult result,  @PathVariable("deadpoolId") Long deadpoolId, @RequestParam(value="killerId") Long killerId, @RequestParam(value="victimId")Long victimId, Principal principal) {
    	String email = principal.getName();
    	User user = userService.findByEmail(email);
    	Long userId = user.getId();
    	pickService.createPick(pick, userId, deadpoolId, victimId, killerId);
    	return "redirect:/users/deadpool/{deadpoolId}/addpicks";
    }
    
    
}
