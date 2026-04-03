package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @PostMapping("/add")
    public Prescription addPrescription(@ModelAttribute Prescription prescription) {
        prescription.setPrescribedDate(LocalDateTime.now());
        return prescriptionRepository.save(prescription);
    }
}