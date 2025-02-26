package com.medicalrecommendation;

import com.medicalrecommendation.model.Patient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.BaseFont;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.FileOutputStream;
import java.util.List;

public class Main {

    // Modern color scheme
    private static final BaseColor PRIMARY_COLOR = new BaseColor(41, 128, 185);    // Professional blue
    private static final BaseColor SECONDARY_COLOR = new BaseColor(44, 62, 80);    // Dark slate
    private static final BaseColor ACCENT_COLOR = new BaseColor(39, 174, 96);      // Success green
    private static final BaseColor WARNING_COLOR = new BaseColor(211, 84, 0);      // Warning orange
    private static final BaseColor LIGHT_BG_COLOR = new BaseColor(245, 247, 250);  // Light background
    private static final BaseColor BORDER_COLOR = new BaseColor(218, 225, 231);    // Subtle border
    private static final BaseColor INFO_BLUE = new BaseColor(33, 150, 243);  // #2196F3

    // Typography
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_COLOR);
    private static final Font INDICATOR_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, SECONDARY_COLOR);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, SECONDARY_COLOR);
    private static final Font VALUE_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
    private static final Font CATEGORY_NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, ACCENT_COLOR);
    private static final Font CATEGORY_WARNING_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, WARNING_COLOR);
    private static final Font RECOMMENDATION_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
    private static final Font INFO_BLUE_BOLD = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, INFO_BLUE);

    // Helper methods for creating components
    private static Font createFont(String baseFont, float size, BaseColor color) {
        return new Font(Font.FontFamily.HELVETICA, size, Font.NORMAL, color);
    }

    // Add this helper method to create fonts with proper encoding
    private static Font getFontWithEncoding(String baseFont, float size, BaseColor color) {
        try {
            // Use CP1252 encoding instead of Identity-H
            BaseFont unicodeFont = BaseFont.createFont(baseFont, BaseFont.CP1252, BaseFont.EMBEDDED);
            return new Font(unicodeFont, size, Font.NORMAL, color);
        } catch (Exception e) {
            // Fallback to basic font creation
            return FontFactory.getFont(baseFont, size, color);
        }
    }

    public static void main(String[] args) {

        // Initialize Drools
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession("ksession-rules");
        
        PatientList patientList = new PatientList();

        List<Patient> patients = patientList.getPatient();

        // Process Patients
        for (Patient patient : patients) {
            kSession.insert(patient);
        }
        kSession.fireAllRules();

        // Generate PDF Report
        generatePDFReport(patients);
    }

    private static void generatePDFReport(List<Patient> patients) {
        try {
            Document document = new Document(PageSize.A4, 60, 60, 60, 60);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("medical_recommendations.pdf"));
            writer.setInitialLeading(20);
            document.open();

            Helper helper = new Helper();
            Patient patient = patients.get(0);

            addHeader(document);
            addPatientInfo(document, patient);
            addVitalSigns(document, patient, helper);
            addLaboratoryResults(document, patient, helper);

            document.close();
            System.out.println("Enhanced PDF Report Generated Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    // Add a new method that returns bytes
    public static byte[] generatePDFReportAsBytes(List<Patient> patients) {
        try {
            Document document = new Document(PageSize.A4, 60, 60, 60, 60);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setInitialLeading(20);
            document.open();

            Helper helper = new Helper();
            Patient patient = patients.get(0);

            addHeader(document);
            addPatientInfo(document, patient);
            addVitalSigns(document, patient, helper);
            addLaboratoryResults(document, patient, helper);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private static void addHeader(Document document) throws DocumentException {
        // Hospital Logo and Info Container
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(15);
    
        // Hospital Info Cell
        PdfPCell hospitalCell = new PdfPCell();
        hospitalCell.setBorder(Rectangle.NO_BORDER);
        hospitalCell.setPaddingBottom(15);
        
        // Hospital Name with increased size and emphasis
        Paragraph hospitalName = new Paragraph();
        hospitalName.add(new Phrase("MAHKOTA MEDICAL CENTRE\n", 
            createFont(FontFactory.HELVETICA_BOLD, 16, PRIMARY_COLOR)));
        hospitalName.setSpacingAfter(5);
        
        // Hospital Address with improved readability
        Paragraph hospitalAddress = new Paragraph();
        hospitalAddress.add(new Phrase(
            "No. 3, Mahkota Melaka, Jalan Merdeka, 75000 Melaka\n",
            createFont(FontFactory.HELVETICA, 9, SECONDARY_COLOR)));
        hospitalAddress.add(new Phrase(
            "Tel: +606 285 2999  |  Fax: +606 281 0560\n",
            createFont(FontFactory.HELVETICA, 9, SECONDARY_COLOR)));
        hospitalAddress.add(new Phrase(
            "www.mahkotamedical.com  |  info@mahkotamedical.com",
            createFont(FontFactory.HELVETICA, 9, SECONDARY_COLOR)));
        
        hospitalCell.addElement(hospitalName);
        hospitalCell.addElement(hospitalAddress);
        headerTable.addCell(hospitalCell);
        document.add(headerTable);
    
        // Title without box, just centered text
        // In the addHeader method, modify the title creation:
        Paragraph title = new Paragraph();
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, PRIMARY_COLOR);
        title.add(new Chunk("MEDICAL CHECK UP REPORT", titleFont));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(10);
        title.setSpacingAfter(10);
        document.add(title);
    
        // Add separator after title
        addSeparator(document);
    }
    
    private static void addSeparator(Document document) throws DocumentException {
        Paragraph separatorPara = new Paragraph();
        separatorPara.setSpacingBefore(-5);
        separatorPara.setSpacingAfter(0);
        
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(BORDER_COLOR);
        separator.setLineWidth(1);
        separator.setPercentage(100);
        
        separatorPara.add(new Chunk(separator));
        document.add(separatorPara);
    }
    
    private static void addPatientInfo(Document document, Patient patient) throws DocumentException {
        // Create table for patient details with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingAfter(10);
        try {
            table.setWidths(new float[]{1f, 1.5f, 1f, 1.5f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    
        // Convert gender to Indonesian
        String gender = patient.getGender() != null ? 
            (patient.getGender().equalsIgnoreCase("Female") ? "Perempuan" : "Laki-laki") : 
            "Laki-laki";
    
        // Convert boolean values to Indonesian
        String pregnant = patient.isPregnant() ? "Ya" : "Tidak";
        String smoking = patient.isSmoking() ? "Ya" : "Tidak";
        String alcohol = patient.isAlcoholConsumption() ? "Ya" : "Tidak";
    
        // Current date formatting
        String currentDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
    
        // Add patient information
        addPatientDataRow2Col(table, "Nama", patient.getName());
        addPatientDataRow2Col(table, "Merokok", smoking);
        addPatientDataRow2Col(table, "Umur", patient.getAge() + " tahun");
        addPatientDataRow2Col(table, "Tinggi Badan", patient.getHeight() + " cm");
        addPatientDataRow2Col(table, "Jenis Kelamin", gender);
        addPatientDataRow2Col(table, "Berat Badan", patient.getWeight() + " kg");
    
        // Add pregnancy status for females
        if (gender.equals("Perempuan")) {
            addPatientDataRowFullWidth(table, "Hamil", pregnant);
        }
        
        addPatientDataRow2Col(table, "Konsumsi Alkohol", alcohol);
        addPatientDataRow2Col(table, "Tanggal Pemeriksaan", currentDate);
        
        document.add(table);
        
        // Add separator after patient info
        addSeparator(document);
    }
    
    private static void addPatientDataRow2Col(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell();
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(8);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph labelPara = new Paragraph(label, LABEL_FONT);
        labelPara.setIndentationLeft(5);
        labelCell.addElement(labelPara);
        
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph valuePara = new Paragraph(": " + value, VALUE_FONT);
        valuePara.setIndentationLeft(5);
        valueCell.addElement(valuePara);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    private static void addPatientDataRowFullWidth(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell();
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(8);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph labelPara = new Paragraph(label, LABEL_FONT);
        labelPara.setIndentationLeft(5);
        labelCell.addElement(labelPara);
        
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(8);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        valueCell.setColspan(3);
        
        Paragraph valuePara = new Paragraph(": " + value, VALUE_FONT);
        valuePara.setIndentationLeft(5);
        valueCell.addElement(valuePara);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    private static void addIndicatorSection(Object target, String indicator, String result, 
                                      String category, String normalValue, String unit, 
                                      String recommendation) throws DocumentException {

    if (indicator == null || result == null || category == null || 
        normalValue == null || unit == null || recommendation == null) {
        return; // Skip this section if any required data is null
    }

    // Modern card-like container for each indicator
    PdfPTable cardTable = new PdfPTable(1);
    cardTable.setWidthPercentage(100);
    cardTable.setSpacingBefore(20);
    cardTable.setSpacingAfter(10);
    cardTable.setKeepTogether(true);

    // Indicator header
    PdfPCell headerCell = new PdfPCell(new Phrase(indicator, INDICATOR_FONT));
    headerCell.setBackgroundColor(LIGHT_BG_COLOR);
    headerCell.setPadding(15);
    headerCell.setBorderColor(BORDER_COLOR);
    headerCell.setBorderWidth(1);
    cardTable.addCell(headerCell);

    // Content table
    PdfPTable contentTable = new PdfPTable(2);
    contentTable.setWidths(new float[]{1.5f, 4.5f});
    contentTable.setWidthPercentage(100);

    // Add result with appropriate color based on category
    Font categoryFont = category.equalsIgnoreCase("Normal") ? 
        CATEGORY_NORMAL_FONT : CATEGORY_WARNING_FONT;
    addResultRow(contentTable, "Hasil", result, category, categoryFont);
    addContentRow(contentTable, "Nilai Normal", normalValue + " " + unit);

    String[] recommendationParts = recommendation.split("---");
    String mainRecommendation = recommendationParts[0].trim();
    String explanation = recommendationParts.length > 1 ? recommendationParts[1].trim() : "";    
    
    // Clean and normalize the text
    mainRecommendation = normalizeText(mainRecommendation);
    explanation = normalizeText(explanation);

    // Recommendation with more padding and different background
    PdfPCell recommendationLabelCell = new PdfPCell(new Phrase("Rekomendasi", LABEL_FONT));
    recommendationLabelCell.setBackgroundColor(LIGHT_BG_COLOR);
    recommendationLabelCell.setPadding(12);
    recommendationLabelCell.setBorderColor(BORDER_COLOR);
    
    PdfPCell recommendationValueCell = new PdfPCell();
    recommendationValueCell.setPadding(12);
    recommendationValueCell.setBorderColor(BORDER_COLOR);
    
    Paragraph recommendationParagraph = new Paragraph();
    recommendationParagraph.setLeading(18);
    recommendationParagraph.setMultipliedLeading(1.5f);

    // Process the recommendation text to highlight question marks
    String[] parts = mainRecommendation.split("\\(\\?\\)");
    for (int i = 0; i < parts.length; i++) {
        recommendationParagraph.add(new Phrase(parts[i], RECOMMENDATION_FONT));
        if (i < parts.length - 1) {
            recommendationParagraph.add(new Phrase("*", INFO_BLUE_BOLD));
        }
    }

    if (!explanation.isEmpty()) {
        recommendationParagraph.add(Chunk.NEWLINE);
        recommendationParagraph.add(Chunk.NEWLINE);
        
        Font asteriskFont = getFontWithEncoding(FontFactory.HELVETICA_BOLD, 10, INFO_BLUE);
        recommendationParagraph.add(new Phrase("*", asteriskFont));
        
        Font smallBoldItalicFont = getFontWithEncoding(FontFactory.HELVETICA_BOLD, 9, INFO_BLUE);
        smallBoldItalicFont.setStyle(Font.ITALIC);
        recommendationParagraph.add(new Phrase("Mengapa Anda mendapatkan rekomendasi ini?", smallBoldItalicFont));
    
        recommendationParagraph.add(Chunk.NEWLINE);

        Font smallItalicFont = getFontWithEncoding(FontFactory.HELVETICA, 9, SECONDARY_COLOR);
        smallItalicFont.setStyle(Font.ITALIC);
        Paragraph explanationParagraph = new Paragraph(explanation, smallItalicFont);
        explanationParagraph.setLeading(12f);
        recommendationParagraph.add(explanationParagraph);
    }

    recommendationValueCell.addElement(recommendationParagraph);
    
    contentTable.addCell(recommendationLabelCell);
    contentTable.addCell(recommendationValueCell);

    // Add content table to card
    PdfPCell contentCell = new PdfPCell(contentTable);
    contentCell.setPadding(15);
    contentCell.setBorderColor(BORDER_COLOR);
    cardTable.addCell(contentCell);

    // Add to either Document or PdfPCell based on target type
    if (target instanceof Document) {
        ((Document) target).add(cardTable);
    } else if (target instanceof PdfPCell) {
        ((PdfPCell) target).addElement(cardTable);
    }
}

private static String normalizeText(String text) {
    return text
    .replace("\u00e2\u20ac\u201c", "\u2013")  // Fix en-dash (â€")
    .replace("\u00e2\u20ac\u201d", "\u2014")  // Fix em-dash (â€")
    .replace("\u2018", "'")   // Fix single quotes
    .replace("\u2019", "'")   // Fix single quotes
    .replace("\u201c", "\"") 
    .replace("â€¢", "•")  // Fix double quotes 
    .replace("\u201d", "\"");  // Fix double quotes
}

    private static void addResultRow(PdfPTable table, String label, String result, String category, Font categoryFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setBackgroundColor(LIGHT_BG_COLOR);
        labelCell.setPadding(12);
        labelCell.setBorderColor(BORDER_COLOR);
        
        Paragraph valueParagraph = new Paragraph();
        valueParagraph.add(new Phrase(result + " ", VALUE_FONT));
        valueParagraph.add(new Phrase("(" + category + ")", categoryFont));
        
        PdfPCell valueCell = new PdfPCell(valueParagraph);
        valueCell.setPadding(12);
        valueCell.setBorderColor(BORDER_COLOR);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private static void addContentRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setBackgroundColor(LIGHT_BG_COLOR);
        labelCell.setPadding(12);
        labelCell.setBorderColor(BORDER_COLOR);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, VALUE_FONT));
        valueCell.setPadding(12);
        valueCell.setBorderColor(BORDER_COLOR);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    
    private static void addVitalSigns(Document document, Patient patient, Helper helper) throws DocumentException {
        // Create a table to keep title and first vital sign together
        Paragraph sectionTitle = new Paragraph("Tanda Vital", SECTION_FONT);
        PdfPTable containerTable = new PdfPTable(1);
        containerTable.setWidthPercentage(100);
        containerTable.setKeepTogether(true);

        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(10);
        titleCell.addElement(sectionTitle);
        containerTable.addCell(titleCell);

        // Add Blood Pressure to container
        PdfPCell bpCell = new PdfPCell();
        bpCell.setBorder(Rectangle.NO_BORDER);
        bpCell.setPadding(0);
        // Blood Pressure
        addIndicatorSection(bpCell,  // Changed from document to bpCell
        "Tekanan Darah",
        patient.getSystolicPressure() + "/" + patient.getDiastolicPressure(),
        helper.getBloodPressureCategory(patient.getSystolicPressure(), patient.getDiastolicPressure()),
        "90/60 - 120/80",
        "mmHg",
        patient.getBloodPressureRecommendation()
        );
        containerTable.addCell(bpCell);

        document.add(containerTable);

        // Pulse
        addIndicatorSection(document,
            "Denyut Nadi",
            String.valueOf(patient.getPulse()),
            helper.getPulseCategory(patient.getPulse()),
            "60 - 100",
            "bpm",
            patient.getPulseRecommendation()
        );

        // Respiratory Rate
        addIndicatorSection(document,
            "Laju Pernafasan",
            String.valueOf(patient.getRespiratoryRate()),
            helper.getRespiratoryCategory(patient.getRespiratoryRate()),
            "12 - 20",
            "rpm",
            patient.getRespiratoryRateRecommendation()
        );

        // Temperature
        addIndicatorSection(document,
            "Suhu",
            String.valueOf(patient.getTemperature()),
            helper.getTemperatureCategory(patient.getTemperature()),
            "36 - 37.5",
            "°C",
            patient.getTemperatureRecommendation()
        );

        // BMI
        addIndicatorSection(document,
            "Indeks Massa Tubuh",
            String.valueOf(patient.getBMI()),
            helper.getBMICategory(patient.getBMI()),
            "18.5 - 25",
            "kg/m²",
            patient.getBMIRecommendation()
        );
    }

    private static void addLaboratoryResults(Document document, Patient patient, Helper helper) throws DocumentException {

        // Container untuk judul dan hemoglobin
        PdfPTable containerTable = new PdfPTable(1);
        containerTable.setWidthPercentage(100);
        containerTable.setKeepTogether(true);

        // Tambahkan judul ke container
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        Paragraph sectionTitle = new Paragraph("Pemeriksaan Laboratorium", SECTION_FONT);
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(10);
        titleCell.addElement(sectionTitle);
        containerTable.addCell(titleCell);

        // Tambahkan Hemoglobin ke container
        PdfPCell hbCell = new PdfPCell();
        hbCell.setBorder(Rectangle.NO_BORDER);
        hbCell.setPadding(0);
        addIndicatorSection(hbCell,
            "Hemoglobin",
            String.valueOf(patient.getHaemoglobin()),
            helper.getHemoglobinCategory(patient.getHaemoglobin(), patient.getGender()),
            patient.getGender().equalsIgnoreCase("male") ? "13 - 18" : "12 - 16",
            "g/dL",
            patient.getHaemoglobinRecommendation()
        );
        containerTable.addCell(hbCell);
        
        document.add(containerTable);

        // Eosinophil
        addIndicatorSection(document,
            "Eosinofil",
            String.valueOf(patient.getEosinophil()),
            helper.getEosinophilCategory(patient.getEosinophil()),
            "0 - 6",
            "%",
            patient.getEosinophilRecommendation()
        );

        // MCV
        addIndicatorSection(document,
            "MCV",
            String.valueOf(patient.getMCV()),
            helper.getMCVCategory(patient.getMCV()),
            "80 - 100",
            "fL",
            patient.getMCVRecommendation()
        );

        // LED
        addIndicatorSection(document,
            "Laju Endap Darah",
            String.valueOf(patient.getLED()),
            helper.getLEDCategory(patient.getLED(), patient.getGender()),
            patient.getGender().equalsIgnoreCase("male") ? "< 15" : "< 20",
            "mm/jam",
            patient.getLEDRecommendation()
        );

        // Uric Acid
        addIndicatorSection(document,
            "Asam Urat",
            String.valueOf(patient.getUricAcid()),
            helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender()),
            patient.getGender().equalsIgnoreCase("male") ? "3.6 - 8.5" : "2.3 - 6.6",
            "mg/dL",
            patient.getUricAcidRecommendation()
        );

        // Glucose
        addIndicatorSection(document,
            "Glukosa",
            String.valueOf(patient.getGlucose()),
            helper.getGlucoseCategory(patient.getGlucose()),
            "70 - 100",
            "mg/dL",
            patient.getGlucoseRecommendation()
        );

        // Total Cholesterol
        addIndicatorSection(document,
            "Kolesterol Total",
            String.valueOf(patient.getTotalCholesterol()),
            helper.getTotalCholesterolCategory(patient.getTotalCholesterol()),
            "< 200",
            "mg/dL",
            patient.getTotalCholesterolRecommendation()
        );

        // Triglyceride
        addIndicatorSection(document,
            "Trigliserida",
            String.valueOf(patient.getTriglyceride()),
            helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender()),
            patient.getGender().equalsIgnoreCase("male") ? "35 - 135" : "40 - 160",
            "mg/dL",
            patient.getTriglycerideRecommendation()
        );

        // HDL
        addIndicatorSection(document,
            "HDL",
            String.valueOf(patient.getHDL()),
            helper.getHDLCategory(patient.getHDL()),
            "30 - 70",
            "mg/dL",
            patient.getHDLRecommendation()
        );

        // LDL
        addIndicatorSection(document,
            "LDL",
            String.valueOf(patient.getLDL()),
            helper.getLDLCategory(patient.getLDL()),
            "< 130",
            "mg/dL",
            patient.getLDLRecommendation()
        );
    }
}