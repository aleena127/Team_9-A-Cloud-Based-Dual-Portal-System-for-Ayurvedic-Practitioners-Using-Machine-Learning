//WebController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // Add this
import java.util.List; // Add this - Fixes the red error on List
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository; 

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
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
    public String showPatientDashboard(@RequestParam(required = false) String username, Model model) {
        Client patient = clientRepository.findByUsername(username != null ? username : "patient")
                .orElseGet(() -> clientRepository.findByRole("PATIENT").stream().findFirst()
                .orElseGet(() -> {
                    Client guest = new Client();
                    guest.setName("Guest");
                    guest.setPrimaryDosha("Pitta");
                    return guest;
                }));

        model.addAttribute("patient", patient);
        return "patient";
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("patientCount", clientRepository.count());
        model.addAttribute("appointmentCount", appointmentRepository.count());
        return "admin";
    }
    @GetMapping("/doctor-dashboard")
    public String showDoctorDashboard(@RequestParam(required = false) Long selectedPatientId, Model model) {
        List<Client> patients = clientRepository.findByRole("PATIENT");
        model.addAttribute("patients", patients);

        Client activePatient = null;

        if (selectedPatientId != null) {
            activePatient = clientRepository.findById(selectedPatientId).orElse(null);
        } else if (!patients.isEmpty()) {
            activePatient = patients.get(0); // auto-select first registered patient
        }

        if (activePatient != null) {
            model.addAttribute("activePatient", activePatient);
            model.addAttribute("history", prescriptionRepository.findByPatientId(activePatient.getId()));
        } else {
            model.addAttribute("history", java.util.Collections.emptyList());
        }

        return "doctor";
    }
}