package cz.vacek.opocket;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/feed")
    public String showFeed(Model model, HttpSession session) {
        List<Event> allEvents = (List<Event>) eventRepository.findAll();
        model.addAttribute("events", allEvents);
        model.addAttribute("today", LocalDate.now()); // Add today's date to the model

        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                Set<Long> registeredEventIds = registrationRepository.findByUser(user).stream()
                        .map(registration -> registration.getEvent().getId())
                        .collect(Collectors.toSet());
                model.addAttribute("registeredEventIds", registeredEventIds);
            }
        } else {
            model.addAttribute("registeredEventIds", Collections.emptySet());
        }

        return "feed";
    }

    @GetMapping("/my-events")
    public String showMyEvents(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/";
        }

        List<Event> myEvents = eventRepository.findByOrganizer(user);
        model.addAttribute("events", myEvents);

        return "my-events";
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
        if (user == null) {
            return "redirect:/"; // User not found
        }
        
        model.addAttribute("user", user);

        LocalDate today = LocalDate.now();
        List<Registration> allRegistrations = registrationRepository.findByUser(user);

        List<Registration> upcomingRegistrations = allRegistrations.stream()
                .filter(reg -> !reg.getEvent().getDate().isBefore(today))
                .collect(Collectors.toList());

        List<Registration> pastRegistrationsFull = allRegistrations.stream()
                .filter(reg -> reg.getEvent().getDate().isBefore(today))
                .sorted(Comparator.comparing((Registration reg) -> reg.getEvent().getDate()).reversed())
                .collect(Collectors.toList());

        model.addAttribute("upcomingRegistrations", upcomingRegistrations);
        model.addAttribute("pastRegistrations", pastRegistrationsFull.stream().limit(3).collect(Collectors.toList()));
        model.addAttribute("totalPastRaces", pastRegistrationsFull.size());

        return "profile";
    }

    @GetMapping("/race-history")
    public String showRaceHistory(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/";
        }

        LocalDate today = LocalDate.now();
        List<Registration> allRegistrations = registrationRepository.findByUser(user);

        List<Registration> pastRegistrationsFull = allRegistrations.stream()
                .filter(reg -> reg.getEvent().getDate().isBefore(today))
                .sorted(Comparator.comparing((Registration reg) -> reg.getEvent().getDate()).reversed())
                .collect(Collectors.toList());
        
        model.addAttribute("pastRegistrations", pastRegistrationsFull);
        return "race-history";
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String processEditProfile(@ModelAttribute User userForm, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null || !userId.equals(userForm.getId())) {
            // Security check: user can only edit their own profile
            return "redirect:/";
        }

        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(userForm.getFirstName());
            existingUser.setLastName(userForm.getLastName());
            existingUser.setEmail(userForm.getEmail());
            userRepository.save(existingUser);
        }

        return "redirect:/profile";
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
    public String showAddEventForm(HttpSession session) {
        if (session.getAttribute("loggedInUserId") == null) {
            return "redirect:/";
        }
        return "add-event";
    }

    @PostMapping("/event/add")
    public String processAddEvent(@RequestParam String name, @RequestParam String location,
                                  @RequestParam String date, @RequestParam String mapName, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }
        User organizer = userRepository.findById(userId).orElse(null);
        if (organizer == null) {
            return "redirect:/?error=userNotFound";
        }

        Event newEvent = new Event();
        newEvent.setName(name);
        newEvent.setLocation(location);
        newEvent.setDate(LocalDate.parse(date));
        newEvent.setMapName(mapName);
        newEvent.setOrganizer(organizer);

        Event savedEvent = eventRepository.save(newEvent);

        return "redirect:/event/categories?eventId=" + savedEvent.getId();
    }

    @GetMapping("/event/categories")
    public String showCategoryForm(@RequestParam Long eventId, Model model) {
        Event event = eventRepository.findById(eventId).orElse(null);
        model.addAttribute("event", event);
        if (event != null) {
            model.addAttribute("categories", event.getCategories());
        } else {
            model.addAttribute("categories", Collections.emptyList());
        }
        return "add-categories";
    }

    @Transactional
    @PostMapping("/event/categories/add")
    public String processAddCategory(@RequestParam Long eventId,
                                     @RequestParam String name,
                                     @RequestParam String length,
                                     @RequestParam String climbing) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            try {
                Category cat = new Category();
                cat.setName(name);
                cat.setLength(Double.parseDouble(length));
                cat.setClimbing(Integer.parseInt(climbing));
                cat.setEvent(event); // Set the owning side

                event.getCategories().add(cat); // Add to the list in the parent
                // No need to call save, @Transactional will handle it
            } catch (NumberFormatException e) {
                return "redirect:/event/categories?eventId=" + eventId + "&error=invalidNumberFormat";
            }
        }
        return "redirect:/event/categories?eventId=" + eventId;
    }

    @GetMapping("/event/join")
    public String showJoinForm(@RequestParam Long eventId, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUserId") == null) {
            return "redirect:/";
        }
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return "redirect:/feed"; // or some error page
        }
        // Explicitly fetch categories from the database
        List<Category> categories = categoryRepository.findByEventId(eventId);
        
        model.addAttribute("event", event);
        model.addAttribute("categories", categories);
        return "join-event";
    }

    @PostMapping("/event/join")
    public String processJoin(@RequestParam Long eventId, @RequestParam Long categoryId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }

        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);
        
        if (event != null && event.getDate().isBefore(LocalDate.now())) {
            return "redirect:/feed?error=eventHasPassed";
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (user != null && event != null && category != null) {
            Registration newRegistration = new Registration();
            newRegistration.setUser(user);
            newRegistration.setEvent(event);
            newRegistration.setCategory(category);
            registrationRepository.save(newRegistration);
        }

        return "redirect:/profile";
    }

    @PostMapping("/event/unregister")
    public String processUnregister(@RequestParam Long registrationId, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }

        Registration registration = registrationRepository.findById(registrationId).orElse(null);
        
        // Security check: Make sure the registration belongs to the logged-in user
        if (registration != null && registration.getUser().getId().equals(userId)) {
            registrationRepository.delete(registration);
        }

        return "redirect:/profile";
    }

    @GetMapping("/event/edit")
    public String showEditEventForm(@RequestParam Long eventId, Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }

        Event event = eventRepository.findById(eventId).orElse(null);
        // Security check: only the organizer can edit the event
        if (event == null || !event.getOrganizer().getId().equals(userId)) {
            return "redirect:/my-events";
        }

        model.addAttribute("event", event);
        return "edit-event";
    }

    @PostMapping("/event/edit")
    public String processEditEvent(@ModelAttribute Event eventForm, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/";
        }

        Event existingEvent = eventRepository.findById(eventForm.getId()).orElse(null);
        // Security check: only the organizer can edit the event
        if (existingEvent == null || !existingEvent.getOrganizer().getId().equals(userId)) {
            return "redirect:/my-events";
        }

        // Update fields
        existingEvent.setName(eventForm.getName());
        existingEvent.setLocation(eventForm.getLocation());
        existingEvent.setDate(eventForm.getDate());
        existingEvent.setMapName(eventForm.getMapName());

        eventRepository.save(existingEvent);

        return "redirect:/my-events";
    }
}