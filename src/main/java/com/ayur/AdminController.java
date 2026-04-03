//AdminController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    // GET /api/admin/stats
    @GetMapping("/admin/stats")
    public String getQuickStats() {
        long patientCount = clientRepository.countByRole("PATIENT");
        long appointmentCount = appointmentRepository.count();
        return "Total Patients: " + patientCount + " | Total Sessions: " + appointmentCount;
    }

    // POST /api/admin/schedule
    @PostMapping("/admin/schedule")
    public Appointment scheduleTherapy(@RequestBody Appointment appointment) {
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }

    // POST /api/appointments/book
    @PostMapping("/appointments/book")
    @ResponseBody
    public String bookAppointment(@RequestParam String therapyType,
                                  @RequestParam String scheduleDate,
                                  @RequestParam(required = false) String notes,
                                  @RequestParam(required = false) Long patientId) {

        Appointment appt = new Appointment();
        appt.setTherapyName(therapyType);
        appt.setStatus("PENDING");
        appt.setPatientId(patientId);

        // optional: save date as text only in response for now
        // if later you want, we can parse scheduleDate into LocalDateTime
        // and store it in scheduleTime

        appointmentRepository.save(appt);

        return "<p class='text-green-700 font-medium text-sm animate-pulse'>" +
               "✓ Request for " + therapyType + " on " + scheduleDate + " logged. " +
               "Our coordinator will confirm shortly.</p>";
    }
}