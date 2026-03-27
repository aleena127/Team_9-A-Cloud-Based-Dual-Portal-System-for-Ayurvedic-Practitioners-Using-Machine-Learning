//PrescriptionController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @GetMapping("/patient/{patientId}")
    public List<Prescription> getHistory(@PathVariable Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    @PostMapping
    public Prescription addPrescription(@RequestBody Prescription prescription) {
        prescription.setPrescribedDate(LocalDateTime.now());
        return prescriptionRepository.save(prescription);
    }
}