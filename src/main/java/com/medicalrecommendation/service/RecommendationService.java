package com.medicalrecommendation.service;

import com.medicalrecommendation.model.Patient;
import com.medicalrecommendation.Helper;
import com.medicalrecommendation.Main;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class RecommendationService {

    private final KieContainer kieContainer;
    private final Helper helper;

    @Autowired
    public RecommendationService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
        this.helper = new Helper();
    }

    public byte[] processPatientAndGeneratePDF(Patient patient) {
        try {
            // Create new KieSession
            KieSession kieSession = kieContainer.newKieSession();

            // Set patient's calculated BMI
            patient.setBMI(patient.getBMI());

            // Insert patient and helper into the session
            kieSession.insert(patient);
            kieSession.insert(helper);

            // Fire all rules
            kieSession.fireAllRules();

            // Generate PDF and get bytes
            byte[] pdfBytes = Main.generatePDFReportAsBytes(Collections.singletonList(patient));

            // Dispose session
            kieSession.dispose();

            return pdfBytes;
        } catch (Exception e) {
            throw new RuntimeException("Error processing patient data: " + e.getMessage(), e);
        }
    }

    // Helper method to check bloodPressure category
    public String getBloodPressureCategory(Patient patient) {
        return helper.getBloodPressureCategory(
            patient.getSystolicPressure(), 
            patient.getDiastolicPressure()
        );
    }

    // Helper method to check pulse category
    public String getPulseCategory(Patient patient) {
        return helper.getPulseCategory(patient.getPulse());
    }

    // Helper method to check respiratory category
    public String getRespiratoryCategory(Patient patient) {
        return helper.getRespiratoryCategory(patient.getRespiratoryRate());
    }

    // Helper method to check temperature category
    public String getTemperatureCategory(Patient patient) {
        return helper.getTemperatureCategory(patient.getTemperature());
    }

    // Helper method to check BMI category
    public String getBMICategory(Patient patient) {
        return helper.getBMICategory(patient.getBMI());
    }

    // Helper method to check hemoglobin category
    public String getHemoglobinCategory(Patient patient) {
        return helper.getHemoglobinCategory(patient.getHaemoglobin(), patient.getGender());
    }

    // Helper method to check eosinophil category
    public String getEosinophilCategory(Patient patient) {
        return helper.getEosinophilCategory(patient.getEosinophil());
    }

    // Helper method to check MCV category
    public String getMCVCategory(Patient patient) {
        return helper.getMCVCategory(patient.getMCV());
    }

    // Helper method to check LED category
    public String getLEDCategory(Patient patient) {
        return helper.getLEDCategory(patient.getLED(), patient.getGender());
    }

    // Helper method to check uric acid category
    public String getUricAcidCategory(Patient patient) {
        return helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender());
    }

    // Helper method to check glucose category
    public String getGlucoseCategory(Patient patient) {
        return helper.getGlucoseCategory(patient.getGlucose());
    }

    // Helper method to check total cholesterol category
    public String getTotalCholesterolCategory(Patient patient) {
        return helper.getTotalCholesterolCategory(patient.getTotalCholesterol());
    }

    // Helper method to check triglyceride category
    public String getTriglycerideCategory(Patient patient) {
        return helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender());
    }

    // Helper method to check HDL category
    public String getHDLCategory(Patient patient) {
        return helper.getHDLCategory(patient.getHDL());
    }

    // Helper method to check LDL category
    public String getLDLCategory(Patient patient) {
        return helper.getLDLCategory(patient.getLDL());
    }
}