package com.medicalrecommendation.dto;

import javax.validation.constraints.*;

public class PatientDTO {
    @NotNull(message = "Gender is required")
    private String gender;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age must be less than 150")
    private Integer age;

    private boolean pregnant;
    private boolean alcoholConsumption;
    private boolean smoking;

    @NotNull(message = "Systolic pressure is required")
    @Min(value = 70, message = "Systolic pressure must be at least 70")
    @Max(value = 200, message = "Systolic pressure must be less than 200")
    private Integer systolicPressure;

    @NotNull(message = "Diastolic pressure is required")
    @Min(value = 40, message = "Diastolic pressure must be at least 40")
    @Max(value = 130, message = "Diastolic pressure must be less than 130")
    private Integer diastolicPressure;

    @NotNull(message = "Pulse is required")
    @Min(value = 40, message = "Pulse must be at least 40")
    @Max(value = 200, message = "Pulse must be less than 200")
    private Integer pulse;

    @NotNull(message = "Respiratory rate is required")
    @Min(value = 8, message = "Respiratory rate must be at least 8")
    @Max(value = 40, message = "Respiratory rate must be less than 40")
    private Integer respiratoryRate;

    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "35.0", message = "Temperature must be at least 35.0")
    @DecimalMax(value = "42.0", message = "Temperature must be less than 42.0")
    private Double temperature;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "20.0", message = "Weight must be at least 20.0")
    @DecimalMax(value = "300.0", message = "Weight must be less than 300.0")
    private Double weight;

    @NotNull(message = "Height is required")
    @DecimalMin(value = "100.0", message = "Height must be at least 100.0")
    @DecimalMax(value = "250.0", message = "Height must be less than 250.0")
    private Double height;

    @NotNull(message = "Haemoglobin is required")
    @DecimalMin(value = "0.0", message = "Haemoglobin must be positive")
    private Double haemoglobin;

    @NotNull(message = "Eosinophil is required")
    @DecimalMin(value = "0.0", message = "Eosinophil must be positive")
    private Double eosinophil;

    @NotNull(message = "MCV is required")
    @DecimalMin(value = "0.0", message = "MCV must be positive")
    private Double mcv;

    @NotNull(message = "LED is required")
    @Min(value = 0, message = "LED must be positive")
    private Integer led;

    @NotNull(message = "Uric acid is required")
    @DecimalMin(value = "0.0", message = "Uric acid must be positive")
    private Double uricAcid;

    @NotNull(message = "Glucose is required")
    @DecimalMin(value = "0.0", message = "Glucose must be positive")
    private Double glucose;

    @NotNull(message = "Total cholesterol is required")
    @DecimalMin(value = "0.0", message = "Total cholesterol must be positive")
    private Double totalCholesterol;

    @NotNull(message = "Triglyceride is required")
    @DecimalMin(value = "0.0", message = "Triglyceride must be positive")
    private Double triglyceride;

    @NotNull(message = "HDL is required")
    @Min(value = 0, message = "HDL must be positive")
    private Integer hdl;

    @NotNull(message = "LDL is required")
    @Min(value = 0, message = "LDL must be positive")
    private Integer ldl;

    // Getters and Setters
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public boolean isPregnant() { return pregnant; }
    public void setPregnant(boolean pregnant) { this.pregnant = pregnant; }

    public boolean isAlcoholConsumption() { return alcoholConsumption; }
    public void setAlcoholConsumption(boolean alcoholConsumption) { this.alcoholConsumption = alcoholConsumption; }

    public boolean isSmoking() { return smoking; }
    public void setSmoking(boolean smoking) { this.smoking = smoking; }

    public Integer getSystolicPressure() { return systolicPressure; }
    public void setSystolicPressure(Integer systolicPressure) { this.systolicPressure = systolicPressure; }

    public Integer getDiastolicPressure() { return diastolicPressure; }
    public void setDiastolicPressure(Integer diastolicPressure) { this.diastolicPressure = diastolicPressure; }

    public Integer getPulse() { return pulse; }
    public void setPulse(Integer pulse) { this.pulse = pulse; }

    public Integer getRespiratoryRate() { return respiratoryRate; }
    public void setRespiratoryRate(Integer respiratoryRate) { this.respiratoryRate = respiratoryRate; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getHaemoglobin() { return haemoglobin; }
    public void setHaemoglobin(Double haemoglobin) { this.haemoglobin = haemoglobin; }

    public Double getEosinophil() { return eosinophil; }
    public void setEosinophil(Double eosinophil) { this.eosinophil = eosinophil; }

    public Double getMcv() { return mcv; }
    public void setMcv(Double mcv) { this.mcv = mcv; }

    public Integer getLed() { return led; }
    public void setLed(Integer led) { this.led = led; }

    public Double getUricAcid() { return uricAcid; }
    public void setUricAcid(Double uricAcid) { this.uricAcid = uricAcid; }

    public Double getGlucose() { return glucose; }
    public void setGlucose(Double glucose) { this.glucose = glucose; }

    public Double getTotalCholesterol() { return totalCholesterol; }
    public void setTotalCholesterol(Double totalCholesterol) { this.totalCholesterol = totalCholesterol; }

    public Double getTriglyceride() { return triglyceride; }
    public void setTriglyceride(Double triglyceride) { this.triglyceride = triglyceride; }

    public Integer getHdl() { return hdl; }
    public void setHdl(Integer hdl) { this.hdl = hdl; }

    public Integer getLdl() { return ldl; }
    public void setLdl(Integer ldl) { this.ldl = ldl; }
}