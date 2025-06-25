package com.medicalrecommendation;

public class Helper {
    
    //Vital Signs

    //pending
    public String getBloodPressureCategory(int systolic, int diastolic) {
        // Normal
        if (systolic < 130 && diastolic < 85) {
            return "Normal";
        }
        // Pra-hipertensi
        else if ((systolic >= 130 && systolic <= 139) || (diastolic >= 85 && diastolic <= 89)) {
            return "Normal-tinggi";
        }
        // Hipertensi tingkat 1
        else if ((systolic >= 140 && systolic <= 159) || (diastolic >= 90 && diastolic <= 99)) {
            return "Hipertensi derajat 1";
        }
        // Hipertensi tingkat 2
        else if (systolic >= 160 || diastolic >= 100) {
            return "Hipertensi derajat 2";
        }
        else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getPulseCategory(int pulse) {
        if (pulse < 60) {
            return "Bradikardia";
        } else if (pulse >= 60 && pulse <= 100) {
            return "Normal";
        } else if (pulse > 100) {
            return "Takikardia";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getRespiratoryCategory(int respiratoryRate) {
        if (respiratoryRate >= 12 && respiratoryRate <= 20) {
            return "Normal";
        } else if (respiratoryRate > 20) {
            return "Takipnea";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getTemperatureCategory(double temperature) {
        if (temperature >= 36.0 && temperature <= 37.5) {
            return "Normal";
        } else if (temperature >= 37.6 && temperature <= 40) {
            return "Demam";
        } else if (temperature > 40) {
            return "Hipertermia";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getBMICategory(double bmi) {
        if (bmi < 17) {
            return "Kekurangan berat badan tingkat berat";
        } else if (bmi >= 17 && bmi <= 18.4) {
            return "Kekurangan berat badan tingkat ringan";
        } else if (bmi >= 18.5 && bmi <= 25) {
            return "Normal";
        } else if (bmi >= 25.1 && bmi <= 27) {
            return "Kelebihan berat badan tingkat ringan";
        } else if (bmi > 27) {
            return "Kelebihan berat badan tingkat berat";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    // Laboratory Results Categories
    public String getHemoglobinCategory(double haemoglobin, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            if (haemoglobin < 13.0) {
                return "Rendah";
            } else if (haemoglobin <= 18) {
                return "Normal";
            } else {
                return "Tinggi";
            }
        } else if (gender.equalsIgnoreCase("female")) {
            if (haemoglobin < 12.0) {
                return "Rendah";
            } else if (haemoglobin <= 16) {
                return "Normal";
            } else {
                return "Tinggi";
            }
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getEosinophilCategory(double eosinophil) {
        if (eosinophil >= 0 && eosinophil <= 6) {
            return "Normal";
        } else if (eosinophil > 6) {
            return "Tinggi";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getMCVCategory(double mcv) {
        if (mcv < 80) {
            return "Mikrositik";
        } else if (mcv >= 80 && mcv <= 100) {
            return "Normositik";
        } else if (mcv > 100) {
            return "Makrositik";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getLEDCategory(int led, String gender) {
        if (gender == null) {
            return "Nilai yang anda masukkan tidak valid"; // Mencegah NullPointerException jika gender null
        }
        
        if (gender.equalsIgnoreCase("male")) {
            return (led < 15) ? "Normal" : "Tinggi";
        } else if (gender.equalsIgnoreCase("female")) {
            return (led < 20) ? "Normal" : "Tinggi";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }
    

    public String getUricAcidCategory(double uricAcid, String gender) {
        if (gender == null) {
            return "Nilai yang anda masukkan tidak valid"; // Mencegah error jika gender null
        }
    
        if (gender.equalsIgnoreCase("male")) {
            if (uricAcid < 3.6) {
                return "Rendah";
            } else if (uricAcid <= 8.5) {
                return "Normal";
            } else {
                return "Tinggi";
            }
        } else if (gender.equalsIgnoreCase("female")) {
            if (uricAcid < 2.3) {
                return "Rendah";
            } else if (uricAcid <= 6.6) {
                return "Normal";
            } else {
                return "Tinggi";
            }
        } else {
            return "Nilai yang anda masukkan tidak valid"; // Untuk gender yang tidak valid
        }
    }
    

    public String getGlucoseCategory(double glucose) {
        if (glucose < 70) {
            return "Rendah";
        } else if (glucose >= 70 && glucose <= 100) {
            return "Normal";
        } else if (glucose > 100) {
            return "Tinggi";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getTotalCholesterolCategory(double totalCholesterol) {
        if (totalCholesterol < 200) {
            return "Normal";
        } else if (totalCholesterol >= 200 && totalCholesterol <= 240) {
            return "Borderline";
        } else if (totalCholesterol > 240) {
            return "Tinggi";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getTriglycerideCategory(double triglyceride, String gender) {
        if (gender == null) {
            return "Nilai yang anda masukkan tidak valid"; // Mencegah error jika gender null
        }
    
        if (gender.equalsIgnoreCase("male")) {
            if (triglyceride < 35) {
                return "Rendah";
            } else if (triglyceride <= 135) {
                return "Normal";
            } else {
                return "High";
            }
        } else if (gender.equalsIgnoreCase("female")) {
            if (triglyceride < 40) {
                return "Rendah";
            } else if (triglyceride <= 160) {
                return "Normal";
            } else {
                return "Tinggi";
            }
        } else {
            return "Nilai yang anda masukkan tidak valid"; // Untuk gender tidak valid
        }
    }
    

    public String getHDLCategory(int hdl) {
        if (hdl < 30) {
            return "Rendah";
        } else if (hdl >= 30 && hdl <= 70) {
            return "Normal";
        } else if (hdl > 70) {
            return "Tinggi";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }

    public String getLDLCategory(int ldl) {
        if (ldl < 130) {
            return "Normal";
        } else if (ldl >= 130 && ldl <= 159) {
            return "Borderline";
        } else if (ldl > 160) {
            return "Tinggi";
        } else {
            return "Nilai yang anda masukkan tidak valid";
        }
    }
}