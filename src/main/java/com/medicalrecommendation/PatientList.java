package com.medicalrecommendation;

import java.util.Arrays;
import java.util.List;

import com.medicalrecommendation.model.Patient;

public class PatientList {

    public List<Patient> getPatient() {
        List<Patient> patients = Arrays.asList(
                new Patient("Nana", "female", 31, true, true, true, 
                120, 80, 70, 
                21, 36, 
                50, 150, 10, 
                7, 50, 21 , 
                8.6, 130, 
                250, 170, 50, 140)
        );

        return patients;
    }
}
