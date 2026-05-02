package cz.vacek.opocket;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    // prihlaseni
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUserId", user.getId());
            return "redirect:/profile";
        } else {
            return "redirect:/";
        }
    }

    // profil
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("loggedInUserId");

        if (userId == null) {
            return "redirect:/";
        }

        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("user", user);

        return "profile";
    }

    // odhlaseni
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage() { return "register"; }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String firstName,
                                      @RequestParam String lastName,
                                      @RequestParam String email,
                                      @RequestParam String username,
                                      @RequestParam String password) {
        User newUser = new User();
        newUser.setFirstName(firstName); newUser.setLastName(lastName);
        newUser.setEmail(email); newUser.setUsername(username); newUser.setPassword(password);
        userRepository.save(newUser);
        return "redirect:/";
    }

    @GetMapping("/event/add")
    public String showAddEventForm() { return "add-event"; }

    @PostMapping("/event/add")
    public String processAddEvent(@RequestParam String name,
                                  @RequestParam String location,
                                  @RequestParam String date,
                                  @RequestParam String mapName) {
        Event newEvent = new Event();
        newEvent.setName(name); newEvent.setLocation(location);
        newEvent.setDate(LocalDate.parse(date)); newEvent.setMapName(mapName);
        eventRepository.save(newEvent);
        return "redirect:/event/add";
    }
}