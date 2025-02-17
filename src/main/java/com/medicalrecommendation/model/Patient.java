package com.medicalrecommendation.model;

import java.text.DecimalFormat;

public class Patient {
    private String name;
    private String gender;
    private int age;
    private boolean pregnant;
    private boolean alcoholConsumption;
    private boolean smoking;
    private int diastolicPressure;
    private int systolicPressure;
    private int pulse;
    private int respiratoryRate;
    private double temperature;
    private double weight;
    private double height;
    private double bmi;
    private double haemoglobin;
    private double eosinophil;
    private double mcv;
    private int led;
    private double uricAcid;
    private double glucose;
    private double totalCholesterol;
    private double triglyceride;
    private int hdl;
    private int ldl;
    private String bloodPressureRecommendation;
    private String pulseRecommendation;
    private String respiratoryRateRecommendation;
    private String temperatureRecommendation;
    private String bmiRecommendation;
    private String haemoglobinRecommendation;
    private String eosinophilRecommendation;
    private String mcvRecommendation;
    private String ledRecommendation;
    private String uricAcidRecommendation;
    private String glucoseRecommendation;
    private String totalCholesterolRecommendation;
    private String triglycerideRecommendation;
    private String hdlRecommendation;
    private String ldlRecommendation;

    // Constructors

    public Patient(String name, String gender, int age,  
                   boolean pregnant, boolean alcoholConsumption, boolean smoking, 
                   int systolicPressure, int diastolicPressure, int pulse, int respiratoryRate,
                   double temperature, double weight, double height,
                   double haemoglobin, double eosinophil, double mcv, int led, 
                   double uricAcid, double glucose, 
                   double totalCholesterol, double triglyceride, int hdl, int ldl) {
        this.gender = gender;
        this.name = name; 
        this.age = age;
        this.pregnant = pregnant;
        this.alcoholConsumption = alcoholConsumption;
        this.smoking = smoking;
        this.diastolicPressure = diastolicPressure;
        this.systolicPressure = systolicPressure;
        this.pulse=pulse;
        this.respiratoryRate = respiratoryRate;
        this.temperature = temperature;
        this.weight = weight;
        this.height = height;
        this.haemoglobin = haemoglobin;
        this.eosinophil = eosinophil;
        this.mcv = mcv; 
        this.led = led;
        this.uricAcid = uricAcid;
        this.glucose = glucose; 
        this.totalCholesterol = totalCholesterol;   
        this.triglyceride = triglyceride; 
        this.hdl = hdl; 
        this.ldl = ldl; 
    }

    // Getters and Setters

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public boolean isPregnant() { return pregnant; }
    public void setPregnant(boolean pregnant) { this.pregnant = pregnant; }

    public boolean isAlcoholConsumption() { return alcoholConsumption; }
    public void setAlcoholConsumption(boolean alcoholConsumption) { this.alcoholConsumption = alcoholConsumption; }

    public boolean isSmoking() { return smoking; }
    public void setSmoking(boolean smoking) { this.smoking = smoking; }

    public int getSystolicPressure() { return systolicPressure; }
    public void setSystolicPressure(int systolicPressure) {this.systolicPressure = systolicPressure; }

    public int getDiastolicPressure() { return diastolicPressure; }
    public void getDiastolicPressure(int diastolicPressure) {this.diastolicPressure = diastolicPressure; }

    public int getPulse() { return pulse; }
    public void getPulse(int pulse) {this.pulse = pulse; }

    public int getRespiratoryRate() { return respiratoryRate; }
    public void getRespiratoryRate(int respiratoryRate) {this.respiratoryRate = respiratoryRate; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getBMI() {
        bmi = weight / (Math.pow(height / 100, 2));
        DecimalFormat df = new DecimalFormat("#.#");
        return Double.parseDouble(df.format(bmi).replace(",", ".")); // Ensure proper decimal format
    }

    public void setBMI(double bmi) {
        this.bmi = bmi;
    }

    public double getHaemoglobin() { return haemoglobin; }
    public void setHaemoglobin(double haemoglobin) { this.haemoglobin = haemoglobin; }

    public double getEosinophil() { return eosinophil; }
    public void setEosinophil(double eosinophil) { this.eosinophil = eosinophil; }

    public double getMCV() { return mcv; }
    public void setMCV(double mcv) { this.mcv = mcv; }

    public int getLED() { return led; }
    public void setLED(int led) { this.led = led; }

    public double getUricAcid() { return uricAcid; }
    public void setUricAcid(double uricAcid) { this.uricAcid = uricAcid; }

    public double getGlucose() { return glucose; }
    public void setGlucose(double glucose) { this.glucose = glucose; }

    public double getTotalCholesterol() { return totalCholesterol; }
    public void setTotalCholesterol(double totalCholesterol) { this.totalCholesterol = totalCholesterol; }

    public double getTriglyceride() { return triglyceride; }
    public void setTriglyceride(double triglyceride) { this.triglyceride = triglyceride; }

    public int getHDL() { return hdl; }
    public void setHDL(int hdl) { this.hdl = hdl; }

    public int getLDL() { return ldl; }
    public void setLDL(int ldl) { this.ldl = ldl; }

    public String getBloodPressureRecommendation() { return bloodPressureRecommendation; }
    public void setBloodPressureRecommendation(String bloodPressureRecommendation) { this.bloodPressureRecommendation = bloodPressureRecommendation; }

    public String getPulseRecommendation() { return pulseRecommendation; }
    public void setPulseRecommendation(String pulseRecommendation) { this.pulseRecommendation = pulseRecommendation; }

    public String getRespiratoryRateRecommendation() { return respiratoryRateRecommendation; }
    public void setRespiratoryRateRecommendation(String respiratoryRateRecommendation) { this.respiratoryRateRecommendation = respiratoryRateRecommendation; }

    public String getTemperatureRecommendation() { return temperatureRecommendation; }
    public void setTemperatureRecommendation(String temperatureRecommendation) { this.temperatureRecommendation = temperatureRecommendation; }

    public String getBMIRecommendation() { return bmiRecommendation; }
    public void setBMIRecommendation(String bmiRecommendation) { this.bmiRecommendation = bmiRecommendation; }

    public String getHaemoglobinRecommendation() { return haemoglobinRecommendation; }
    public void setHaemoglobinRecommendation(String haemoglobinRecommendation) { this.haemoglobinRecommendation = haemoglobinRecommendation; }

    public String getEosinophilRecommendation() { return eosinophilRecommendation; }
    public void setEosinophilRecommendation(String eosinophilRecommendation) { this.eosinophilRecommendation = eosinophilRecommendation; }

    public String getMCVRecommendation() { return mcvRecommendation; }
    public void setMCVRecommendation(String mcvRecommendation) { this.mcvRecommendation = mcvRecommendation; }

    public String getLEDRecommendation() { return ledRecommendation; }
    public void setLEDRecommendation(String ledRecommendation) { this.ledRecommendation = ledRecommendation; }

    public String getUricAcidRecommendation() { return uricAcidRecommendation; }
    public void setUricAcidRecommendation(String uricAcidRecommendation) { this.uricAcidRecommendation = uricAcidRecommendation; }

    public String getGlucoseRecommendation() { return glucoseRecommendation; }
    public void setGlucoseRecommendation(String glucoseRecommendation) { this.glucoseRecommendation = glucoseRecommendation; }

    public String getTotalCholesterolRecommendation() { return totalCholesterolRecommendation; }
    public void setTotalCholesterolRecommendation(String totalCholesterolRecommendation) { this.totalCholesterolRecommendation = totalCholesterolRecommendation; }

    public String getTriglycerideRecommendation() { return triglycerideRecommendation; }
    public void setTriglycerideRecommendation(String triglycerideRecommendation) { this.triglycerideRecommendation = triglycerideRecommendation; }
    
    public String getHDLRecommendation() { return hdlRecommendation; }
    public void setHDLRecommendation(String hdlRecommendation) { this.hdlRecommendation = hdlRecommendation; }

    public String getLDLRecommendation() { return ldlRecommendation; }
    public void setLDLRecommendation(String ldlRecommendation) { this.ldlRecommendation = ldlRecommendation; }
}
