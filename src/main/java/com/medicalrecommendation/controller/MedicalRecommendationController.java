package com.medicalrecommendation.controller;

import com.medicalrecommendation.dto.PatientDTO;
import com.medicalrecommendation.model.Patient;
import com.medicalrecommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MedicalRecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public MedicalRecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "patient-form";
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateReport(@Valid @ModelAttribute PatientDTO patientDTO, 
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            // Convert DTO to Patient
            Patient patient = convertToPatient(patientDTO);
            
            // Process and generate PDF
            byte[] pdfContent = recommendationService.processPatientAndGeneratePDF(patient);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medical_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generating report: " + e.getMessage());
        }
    }

    private Patient convertToPatient(PatientDTO dto) {
        return new Patient(
            dto.getName(),
            dto.getGender(),
            dto.getAge(),
            dto.isPregnant(),
            dto.isAlcoholConsumption(),
            dto.isSmoking(),
            dto.getSystolicPressure(),
            dto.getDiastolicPressure(),
            dto.getPulse(),
            dto.getRespiratoryRate(),
            dto.getTemperature(),
            dto.getWeight(),
            dto.getHeight(),
            dto.getHaemoglobin(),
            dto.getEosinophil(),
            dto.getMcv(),
            dto.getLed(),
            dto.getUricAcid(),
            dto.getGlucose(),
            dto.getTotalCholesterol(),
            dto.getTriglyceride(),
            dto.getHdl(),
            dto.getLdl()
        );
    }
}