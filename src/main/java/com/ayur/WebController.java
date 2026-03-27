package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository; 

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/patient-dashboard")
    public String showPatientDashboard(Model model) {
        Client patient = clientRepository.findAll().stream()
                            .filter(c -> "PATIENT".equalsIgnoreCase(c.getRole()))
                            .findFirst()
                            .orElse(null);

        if (patient == null) {
            patient = new Client();
            patient.setName("Valued Patient");
            patient.setPrimaryDosha("Pitta");
        }

        model.addAttribute("patient", patient);
        return "patient";
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(Model model) {
        long pCount = clientRepository.count();
        long aCount = appointmentRepository.count();
        
        model.addAttribute("patientCount", pCount);
        model.addAttribute("appointmentCount", aCount);
        
        return "admin";
    }

    @GetMapping("/doctor-dashboard")
    public String showDoctorDashboard(Model model) {
        return "doctor";
    }
}