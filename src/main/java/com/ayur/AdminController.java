//AdminController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/stats")
    public String getQuickStats() {
        long patientCount = clientRepository.count();
        long appointmentCount = appointmentRepository.count();
        return "Total Patients: " + patientCount + " | Total Sessions: " + appointmentCount;
    }

    @PostMapping("/schedule")
    public Appointment scheduleTherapy(@RequestBody Appointment appointment) {
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }
}