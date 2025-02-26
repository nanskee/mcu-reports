package com.medicalrecommendation;

import java.util.Arrays;
import java.util.List;

import com.medicalrecommendation.model.Patient;

public class PatientList {

    public List<Patient> getPatient() {
        List<Patient> patients = Arrays.asList(
                new Patient("Nana", "female", 61, false, false, false, 
                140, 90, 110, 
                13, 38, 
                50, 190, 19, 
                7, 120, 21 , 
                3.4, 70, 
                210, 30, 80, 170)
        );

        return patients;
    }
}
