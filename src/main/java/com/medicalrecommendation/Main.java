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
import java.util.HashMap;
import java.util.Map;

public class Main {

    // Modern color scheme
    private static final BaseColor PRIMARY_COLOR = new BaseColor(41, 128, 185);    // Professional blue
    private static final BaseColor SECONDARY_COLOR = new BaseColor(44, 62, 80);    // Dark slate
    private static final BaseColor ACCENT_COLOR = new BaseColor(39, 174, 96);      // Success green
    private static final BaseColor WARNING_COLOR = new BaseColor(255, 0, 0);      // Warning red
    private static final BaseColor LIGHT_BG_COLOR = new BaseColor(245, 247, 250);  // Light background
    private static final BaseColor BORDER_COLOR = new BaseColor(218, 225, 231);    // Subtle border
    private static final BaseColor INFO_BLUE = new BaseColor(0, 123, 255);  // Hyperlink blue

    // Typography
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_COLOR);
    private static final Font INDICATOR_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, SECONDARY_COLOR);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, SECONDARY_COLOR);
    private static final Font VALUE_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
    private static final Font CATEGORY_NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, ACCENT_COLOR);
    private static final Font CATEGORY_WARNING_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, WARNING_COLOR);
    private static final Font RECOMMENDATION_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
    private static final Font INFO_BLUE_BOLD = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, INFO_BLUE);

    // Modified helper method for creating fonts with reduced complexity
    private static Font createFont(String fontName, float size, BaseColor color) {
        int style = Font.NORMAL;
        if (fontName != null) {
            String lowercaseFontName = fontName.toLowerCase();
            if (lowercaseFontName.contains("bold")) {
                style |= Font.BOLD;
            }
            if (lowercaseFontName.contains("italic")) {
                style |= Font.ITALIC;
            }
        }
        return new Font(Font.FontFamily.HELVETICA, size, style, color);
    }

    // Simplified font creation method with encoding handling
    private static Font getFontWithEncoding(String baseFont, float size, BaseColor color) {
        try {
            BaseFont unicodeFont = BaseFont.createFont(baseFont, BaseFont.CP1252, BaseFont.EMBEDDED);
            return new Font(unicodeFont, size, Font.NORMAL, color);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            return createFont(baseFont, size, color);
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
            // First pass - create temporary document to track page numbers
            Map<String, Integer> pageNumberMap = new HashMap<>();
            
            // Create temporary document
            Document tempDoc = new Document(PageSize.A4, 60, 60, 60, 60);
            java.io.ByteArrayOutputStream tempBaos = new java.io.ByteArrayOutputStream();
            PdfWriter tempWriter = PdfWriter.getInstance(tempDoc, tempBaos);
            
            // Create a page tracker
            PageTracker pageTracker = new PageTracker();
            tempWriter.setPageEvent(pageTracker);
            
            tempDoc.open();
            
            Helper helper = new Helper();
            Patient patient = patients.get(0);
            
            // Add header and patient info
            addHeader(tempDoc);
            addPatientInfo(tempDoc, patient);
            
            // Start a new page to simulate the exact flow
            tempDoc.newPage();
            
            // Add summary section
            addSummarySection(tempDoc, patient, helper);
            
            // Add parameter sections while tracking pages
            addVitalSignsWithTracking(tempDoc, patient, helper, pageTracker);
            addLaboratoryResultsWithTracking(tempDoc, patient, helper, pageTracker);
            
            tempDoc.close();
            
            // Get page number map for parameters
            pageNumberMap = pageTracker.getPageNumberMap();
            
            // Second pass - create the actual document
            Document document = new Document(PageSize.A4, 60, 60, 60, 60);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("medical_recommendations.pdf"));
            
            // Add regular page numbering
            PageReferenceOverlayHelper pageOverlay = new PageReferenceOverlayHelper(PRIMARY_COLOR, SECONDARY_COLOR);
            writer.setPageEvent(pageOverlay);
            
            document.open();
            
            // Add header and patient info
            addHeader(document);
            addPatientInfo(document, patient);
            
            // Add summary table with the page numbers we got from first pass
            addSummaryTableWithPageNumbers(document, patient, helper, pageNumberMap);
            
            document.newPage();
            
            // Add summary section
            addSummarySection(document, patient, helper);
            
            // Add all parameter sections - now using pageOverlay
            addVitalSigns(document, patient, helper, pageOverlay);
            addLaboratoryResults(document, patient, helper, pageOverlay);
            
            document.close();
            System.out.println("Enhanced PDF Report Generated Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }
    
    // The fixed generatePDFReportAsBytes method should look like this:
    public static byte[] generatePDFReportAsBytes(List<Patient> patients) {
        try {
            // First pass - create temporary document to track page numbers
            Map<String, Integer> pageNumberMap = new HashMap<>();
            
            // Create temporary document
            Document tempDoc = new Document(PageSize.A4, 60, 60, 60, 60);
            java.io.ByteArrayOutputStream tempBaos = new java.io.ByteArrayOutputStream();
            PdfWriter tempWriter = PdfWriter.getInstance(tempDoc, tempBaos);
            
            // Create a page tracker
            PageTracker pageTracker = new PageTracker();
            tempWriter.setPageEvent(pageTracker);
            
            tempDoc.open();
            
            Helper helper = new Helper();
            Patient patient = patients.get(0);
            
            // Add header and patient info
            addHeader(tempDoc);
            addPatientInfo(tempDoc, patient);
            
            // Start a new page to simulate the exact flow
            tempDoc.newPage();
            
            // Add summary section
            addSummarySection(tempDoc, patient, helper);
            
            // Add parameter sections while tracking pages
            addVitalSignsWithTracking(tempDoc, patient, helper, pageTracker);
            addLaboratoryResultsWithTracking(tempDoc, patient, helper, pageTracker);
            
            tempDoc.close();
            
            // Get page number map for parameters
            pageNumberMap = pageTracker.getPageNumberMap();
            
            // Second pass - create the actual document
            Document document = new Document(PageSize.A4, 60, 60, 60, 60);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            // Add regular page numbering - FIXED using PageReferenceOverlayHelper
            PageReferenceOverlayHelper pageOverlay = new PageReferenceOverlayHelper(PRIMARY_COLOR, SECONDARY_COLOR);
            writer.setPageEvent(pageOverlay);
            
            document.open();
            
            // Add header and patient info
            addHeader(document);
            addPatientInfo(document, patient);
            
            // Add summary table with the page numbers we got from first pass
            addSummaryTableWithPageNumbers(document, patient, helper, pageNumberMap);
            
            document.newPage();
            
            // Add summary section
            addSummarySection(document, patient, helper);
            
            // Add all parameter sections - now using pageOverlay
            addVitalSigns(document, patient, helper, pageOverlay);
            addLaboratoryResults(document, patient, helper, pageOverlay);
            
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
        headerTable.setSpacingAfter(8);
     
        // Hospital Info Cell
        PdfPCell hospitalCell = new PdfPCell();
        hospitalCell.setBorder(Rectangle.NO_BORDER);
        hospitalCell.setPaddingBottom(5);
        
        // Hospital Name dengan warna yang lebih jelas
        Paragraph hospitalName = new Paragraph();
        Font hospitalNameFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, PRIMARY_COLOR);
        hospitalName.add(new Phrase("MAHKOTA MEDICAL CENTRE\n", hospitalNameFont));
        hospitalName.setSpacingAfter(3);
        hospitalName.setAlignment(Element.ALIGN_LEFT); // Center aligned
    
        // Hospital Address yang lebih rapi
        Paragraph hospitalAddress = new Paragraph();
        Font addressFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, SECONDARY_COLOR);
        
        // Gunakan satu baris dengan spacing yang lebih baik
        hospitalAddress.add(new Phrase(
            "No. 3, Mahkota Melaka, Jalan Merdeka, 75000 Melaka  |  ", addressFont));
        hospitalAddress.add(new Phrase(
            "Tel: +606 285 2999  |  Fax: +606 281 0560", addressFont));
        hospitalAddress.setAlignment(Element.ALIGN_LEFT); // Center aligned
        
        // Satu baris untuk website dan email
        Paragraph contactInfo = new Paragraph();
        contactInfo.add(new Phrase(
            "www.mahkotamedical.com  |  info@mahkotamedical.com", addressFont));
        contactInfo.setAlignment(Element.ALIGN_LEFT); // Center aligned
                
        hospitalCell.addElement(hospitalName);
        hospitalCell.addElement(hospitalAddress);
        hospitalCell.addElement(contactInfo);
        headerTable.addCell(hospitalCell);
        document.add(headerTable);
    
        // Title dengan desain yang lebih baik
        Paragraph title = new Paragraph();
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY_COLOR);
        title.add(new Chunk("LAPORAN HASIL PEMERIKSAAN KESEHATAN", titleFont));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(3);
        title.setSpacingAfter(5);
        document.add(title);
    
        // Add separator yang lebih tipis namun tegas
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(PRIMARY_COLOR); // Menggunakan warna utama untuk garis
        separator.setLineWidth(0.8f); // Sedikit lebih tebal untuk visibility
        separator.setPercentage(100);
        
        Paragraph separatorPara = new Paragraph();
        separatorPara.add(new Chunk(separator));
        separatorPara.setSpacingBefore(0);
        separatorPara.setSpacingAfter(0);
        document.add(separatorPara);
    }
    
    private static void addPatientInfo(Document document, Patient patient) throws DocumentException {
        // Menerapkan desain yang lebih profesional untuk informasi pasien
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingAfter(8);
        
        try {
            // Proporsi kolom yang lebih baik
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
    
        // Add patient information dengan format yang lebih baik
        addImprovePatientDataRow2Col(table, "Nama", patient.getName());
        addImprovePatientDataRow2Col(table, "Merokok", smoking);
        addImprovePatientDataRow2Col(table, "Umur", patient.getAge() + " tahun");
        addImprovePatientDataRow2Col(table, "Tinggi Badan", patient.getHeight() + " cm");
        addImprovePatientDataRow2Col(table, "Jenis Kelamin", gender);
        addImprovePatientDataRow2Col(table, "Berat Badan", patient.getWeight() + " kg");
    
        // Add pregnancy status for females dengan gaya yang konsisten
        if (gender.equals("Perempuan")) {
            addImprovePatientDataRowFullWidth(table, "Hamil", pregnant);
        }
        
        addImprovePatientDataRow2Col(table, "Konsumsi Alkohol", alcohol);
        addImprovePatientDataRow2Col(table, "Tanggal Pemeriksaan", currentDate);
        
        document.add(table);

        // Add separator yang lebih tipis namun tegas
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(PRIMARY_COLOR); // Menggunakan warna utama untuk garis
        separator.setLineWidth(0.8f); // Sedikit lebih tebal untuk visibility
        separator.setPercentage(100);
        
        Paragraph separatorPara = new Paragraph();
        separatorPara.add(new Chunk(separator));
        separatorPara.setSpacingBefore(-7);
        separatorPara.setSpacingAfter(-10);
        document.add(separatorPara);
        
        // Vertical spacing yang tepat
        document.add(Chunk.NEWLINE);
    }
    
    // Metode yang ditingkatkan untuk baris data pasien
    private static void addImprovePatientDataRow2Col(PdfPTable table, String label, String value) {
        // Font yang lebih konsisten
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, SECONDARY_COLOR);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
        
        // Label Cell
        PdfPCell labelCell = new PdfPCell();
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph labelPara = new Paragraph(label, labelFont);
        labelCell.addElement(labelPara);
        
        // Value Cell
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        // Value menggunakan : dengan jarak yang tepat
        Paragraph valuePara = new Paragraph(": " + value, valueFont);
        valueCell.addElement(valuePara);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    // Metode untuk full width rows dengan format yang ditingkatkan
    private static void addImprovePatientDataRowFullWidth(PdfPTable table, String label, String value) {
        // Font yang konsisten
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, SECONDARY_COLOR);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
        
        // Label Cell
        PdfPCell labelCell = new PdfPCell();
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph labelPara = new Paragraph(label, labelFont);
        labelCell.addElement(labelPara);
        
        // Value Cell
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        valueCell.setColspan(3);
        
        // Value dengan format yang sama
        Paragraph valuePara = new Paragraph(": " + value, valueFont);
        valueCell.addElement(valuePara);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    

    // Metode untuk menambahkan baris tabel dengan desain yang lebih baik
    private static void addImproveSummaryRow(PdfPTable table, String parameter, String normalValue, 
                                        String result, String category, int page) {
        // Font dan warna yang konsisten
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
        
        // Parameter cell dengan font konsisten
        PdfPCell parameterCell = new PdfPCell();
        parameterCell.setPadding(4);
        parameterCell.setBorderColor(BORDER_COLOR);
        parameterCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        parameterCell.setMinimumHeight(18); // Tinggi baris konsisten
        
        Paragraph paramPara = new Paragraph(parameter, valueFont);
        parameterCell.addElement(paramPara);
        
        // Normal Value cell dengan alignment yang konsisten
        PdfPCell normalCell = new PdfPCell();
        normalCell.setPadding(4);
        normalCell.setBorderColor(BORDER_COLOR);
        normalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        normalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        normalCell.setMinimumHeight(18);
        
        Paragraph normalPara = new Paragraph(normalValue, valueFont);
        normalPara.setAlignment(Element.ALIGN_CENTER);
        normalCell.addElement(normalPara);
        
        // Result cell dengan formatting yang konsisten
        PdfPCell resultCell = new PdfPCell();
        resultCell.setPadding(4);
        resultCell.setBorderColor(BORDER_COLOR);
        resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        resultCell.setMinimumHeight(18);
        
        Paragraph resultPara = new Paragraph(result, valueFont);
        resultPara.setAlignment(Element.ALIGN_CENTER);
        resultCell.addElement(resultPara);
        
        // Category cell dengan warna yang sesuai
        PdfPCell categoryCell = new PdfPCell();
        categoryCell.setPadding(4);
        categoryCell.setBorderColor(BORDER_COLOR);
        categoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        categoryCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        categoryCell.setMinimumHeight(18);
        
        // Warna kategori yang lebih jelas berdasarkan status
        Font categoryFont;
        if (category.equalsIgnoreCase("Normal")) {
            categoryFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, ACCENT_COLOR);
        } else {
            categoryFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, WARNING_COLOR);
        }
        
        Paragraph categoryPara = new Paragraph(category, categoryFont);
        categoryPara.setAlignment(Element.ALIGN_CENTER);
        categoryCell.addElement(categoryPara);
        
        // Page cell dengan style yang konsisten
        PdfPCell pageCell = new PdfPCell();
        pageCell.setPadding(4);
        pageCell.setBorderColor(BORDER_COLOR);
        pageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pageCell.setMinimumHeight(18);
        
        Paragraph pagePara = new Paragraph(String.valueOf(page), valueFont);
        pagePara.setAlignment(Element.ALIGN_CENTER);
        pageCell.addElement(pagePara);
        
        // Add semua cell ke tabel
        table.addCell(parameterCell);
        table.addCell(normalCell);
        table.addCell(resultCell);
        table.addCell(categoryCell);
        table.addCell(pageCell);
    }

    // New method to add the summary section
    // Modifications to the addSummarySection method in Main.java

    private static void addSummarySection(Document document, Patient patient, Helper helper) throws DocumentException {
        // Header for the summary section
        Paragraph summaryTitle = new Paragraph("Rangkuman Rekomendasi", SECTION_FONT);
        summaryTitle.setSpacingBefore(15);
        summaryTitle.setSpacingAfter(20);
        document.add(summaryTitle);
        
        // Add AI disclaimer warning at the top in a bordered box with background color
        PdfPTable disclaimerTable = new PdfPTable(1);
        disclaimerTable.setWidthPercentage(100);
        disclaimerTable.setSpacingAfter(15);
        
        PdfPCell disclaimerCell = new PdfPCell();
        disclaimerCell.setPadding(8);
        disclaimerCell.setBorderColor(new BaseColor(220, 53, 69)); // Red border for warning
        disclaimerCell.setBorderWidth(1.5f);
        disclaimerCell.setBackgroundColor(new BaseColor(255, 243, 243)); // Light red background
        
        // Create a composite paragraph with different styles
        Paragraph disclaimerParagraph = new Paragraph();
        
        // Add warning symbol and red "PERINGATAN" text
        Font warningFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(180, 0, 0)); // Bold red text
        Chunk warningChunk = new Chunk("⚠️PERINGATAN: ", warningFont);
        disclaimerParagraph.add(warningChunk);
        
        // Add the rest of the text in black
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        Chunk normalChunk = new Chunk(
            "Rangkuman ini dibuat secara otomatis oleh sistem berbasis kecerdasan buatan (AI) untuk memberikan gambaran umum atas hasil medical check-up Anda. " +
            "Untuk keakuratan, selalu baca detail setiap hasil pemeriksaan pada halaman-halaman selanjutnya.", 
            normalFont);
        disclaimerParagraph.add(normalChunk);
        
        disclaimerCell.addElement(disclaimerParagraph);
        disclaimerTable.addCell(disclaimerCell);
        document.add(disclaimerTable);
        
        // Check if any parameters are outside normal range
        @SuppressWarnings("unused")
        List<RecommendationSummarizer.RecommendationItem> abnormalItems = 
            RecommendationSummarizer.generateSummary(patient, helper);
        
        // Get the comprehensive recommendation - regardless of normal or abnormal status
        String comprehensiveRecommendation = RecommendationSummarizer.generateComprehensiveRecommendation(patient, helper);
        
        // Create container for recommendation content
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingAfter(10);
        
        PdfPCell cell = new PdfPCell();
        cell.setPadding(12);
        cell.setBorderColor(BORDER_COLOR);
        
        // Format recommendation text with improved header styling
        Paragraph recommendationParagraph = new Paragraph();
        recommendationParagraph.setLeading(14f); // Line spacing
        
        // Parse the comprehensive recommendation by lines for better formatting
        String[] lines = comprehensiveRecommendation.split("\n");
        boolean isFirstLine = true;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }
            
            // Check if this is a category header (e.g., "1. Pola Makan dan Nutrisi:")
            if (line.matches("\\d+\\. [^:]+:.*")) {
                // Add spacing before all category headers except the first one
                if (!isFirstLine) {
                    recommendationParagraph.add(Chunk.NEWLINE);
                }
                isFirstLine = false;
                
                // This is a category header - format with larger, colored font
                Font categoryFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, PRIMARY_COLOR);
                Paragraph categoryPara = new Paragraph(line, categoryFont);
                categoryPara.setSpacingAfter(5);
                recommendationParagraph.add(categoryPara);
                
                // If there's content on the next line, add it
                if (i + 1 < lines.length && !lines[i + 1].trim().isEmpty() && !lines[i + 1].matches("\\d+\\. [^:]+:.*")) {
                    Font contentFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
                    Paragraph contentPara = new Paragraph(lines[i + 1], contentFont);
                    contentPara.setIndentationLeft(10); // Indent content under headers
                    recommendationParagraph.add(contentPara);
                    i++; // Skip the next line since we've already processed it
                }
            } else {
                // Regular content text
                Font contentFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
                Paragraph contentPara = new Paragraph(line, contentFont);
                contentPara.setIndentationLeft(10); // Indent content under headers
                recommendationParagraph.add(contentPara);
            }
        }
        
        cell.addElement(recommendationParagraph);
        table.addCell(cell);
        document.add(table);
        
        // Add note at the bottom
        Font noteFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, SECONDARY_COLOR);
        Paragraph note = new Paragraph("Catatan: Untuk detail lengkap dari setiap indikator, silakan lihat halaman-halaman berikutnya.", 
            noteFont);
        note.setSpacingBefore(5);
        note.setSpacingAfter(15);
        document.add(note);
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
        
        Font smallBoldItalicFont = getFontWithEncoding(FontFactory.HELVETICA, 9, INFO_BLUE);
        smallBoldItalicFont.setStyle(Font.BOLD | Font.ITALIC);
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

    // Methods to add vital signs and track their page numbers
private static void addVitalSignsWithTracking(Document document, Patient patient, Helper helper, PageTracker pageTracker) throws DocumentException {
    // Create title for the section
    Paragraph sectionTitle = new Paragraph("Tanda Vital", SECTION_FONT);
    sectionTitle.setSpacingBefore(20);
    sectionTitle.setSpacingAfter(10);
    document.add(sectionTitle);
    
    // Track Tekanan Darah
    pageTracker.startTracking("Tekanan Darah");
    
    // Add Tekanan Darah section
    addIndicatorSection(document,
        "Tekanan Darah",
        patient.getSystolicPressure() + "/" + patient.getDiastolicPressure(),
        helper.getBloodPressureCategory(patient.getSystolicPressure(), patient.getDiastolicPressure()),
        "90/60 - 120/80",
        "mmHg",
        patient.getBloodPressureRecommendation()
    );
    
    // Track Denyut Nadi
    pageTracker.startTracking("Denyut Nadi");
    
    // Add Denyut Nadi section
    addIndicatorSection(document,
        "Denyut Nadi",
        String.valueOf(patient.getPulse()),
        helper.getPulseCategory(patient.getPulse()),
        "60 - 100",
        "bpm",
        patient.getPulseRecommendation()
    );
    
    // Track Laju Pernafasan
    pageTracker.startTracking("Laju Pernafasan");
    
    // Add Laju Pernafasan section
    addIndicatorSection(document,
        "Laju Pernafasan",
        String.valueOf(patient.getRespiratoryRate()),
        helper.getRespiratoryCategory(patient.getRespiratoryRate()),
        "12 - 20",
        "rpm",
        patient.getRespiratoryRateRecommendation()
    );
    
    // Track Suhu
    pageTracker.startTracking("Suhu");
    
    // Add Suhu section
    addIndicatorSection(document,
        "Suhu",
        String.valueOf(patient.getTemperature()),
        helper.getTemperatureCategory(patient.getTemperature()),
        "36 - 37.5",
        "°C",
        patient.getTemperatureRecommendation()
    );
    
    // Track Indeks Massa Tubuh
    pageTracker.startTracking("Indeks Massa Tubuh");
    
    // Add Indeks Massa Tubuh section
    addIndicatorSection(document,
        "Indeks Massa Tubuh",
        String.valueOf(patient.getBMI()),
        helper.getBMICategory(patient.getBMI()),
        "18.5 - 25",
        "kg/m²",
        patient.getBMIRecommendation()
    );
}

// Methods to add laboratory results and track their page numbers
private static void addLaboratoryResultsWithTracking(Document document, Patient patient, Helper helper, PageTracker pageTracker) throws DocumentException {
    // Create title for the section
    Paragraph sectionTitle = new Paragraph("Pemeriksaan Laboratorium", SECTION_FONT);
    sectionTitle.setSpacingBefore(20);
    sectionTitle.setSpacingAfter(10);
    document.add(sectionTitle);
    
    // Track Hemoglobin
    pageTracker.startTracking("Hemoglobin");
    
    // Add Hemoglobin section
    addIndicatorSection(document,
        "Hemoglobin",
        String.valueOf(patient.getHaemoglobin()),
        helper.getHemoglobinCategory(patient.getHaemoglobin(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "13 - 18" : "12 - 16",
        "g/dL",
        patient.getHaemoglobinRecommendation()
    );
    
    // Track Eosinofil
    pageTracker.startTracking("Eosinofil");
    
    // Add Eosinofil section
    addIndicatorSection(document,
        "Eosinofil",
        String.valueOf(patient.getEosinophil()),
        helper.getEosinophilCategory(patient.getEosinophil()),
        "0 - 6",
        "%",
        patient.getEosinophilRecommendation()
    );
    
    // Track MCV
    pageTracker.startTracking("MCV");
    
    // Add MCV section
    addIndicatorSection(document,
        "MCV",
        String.valueOf(patient.getMCV()),
        helper.getMCVCategory(patient.getMCV()),
        "80 - 100",
        "fL",
        patient.getMCVRecommendation()
    );
    
    // Track Laju Endap Darah
    pageTracker.startTracking("Laju Endap Darah (LED)");
    
    // Add Laju Endap Darah section
    addIndicatorSection(document,
        "Laju Endap Darah (LED)",
        String.valueOf(patient.getLED()),
        helper.getLEDCategory(patient.getLED(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "< 15" : "< 20",
        "mm/jam",
        patient.getLEDRecommendation()
    );
    
    // Track Asam Urat
    pageTracker.startTracking("Asam Urat");
    
    // Add Asam Urat section
    addIndicatorSection(document,
        "Asam Urat",
        String.valueOf(patient.getUricAcid()),
        helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "3.6 - 8.5" : "2.3 - 6.6",
        "mg/dL",
        patient.getUricAcidRecommendation()
    );
    
    // Track Glukosa
    pageTracker.startTracking("Glukosa");
    
    // Add Glukosa section
    addIndicatorSection(document,
        "Glukosa",
        String.valueOf(patient.getGlucose()),
        helper.getGlucoseCategory(patient.getGlucose()),
        "70 - 100",
        "mg/dL",
        patient.getGlucoseRecommendation()
    );
    
    // Track Kolesterol Total
    pageTracker.startTracking("Kolesterol Total");
    
    // Add Kolesterol Total section
    addIndicatorSection(document,
        "Kolesterol Total",
        String.valueOf(patient.getTotalCholesterol()),
        helper.getTotalCholesterolCategory(patient.getTotalCholesterol()),
        "< 200",
        "mg/dL",
        patient.getTotalCholesterolRecommendation()
    );
    
    // Track Trigliserida
    pageTracker.startTracking("Trigliserida");
    
    // Add Trigliserida section
    addIndicatorSection(document,
        "Trigliserida",
        String.valueOf(patient.getTriglyceride()),
        helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "35 - 135" : "40 - 160",
        "mg/dL",
        patient.getTriglycerideRecommendation()
    );
    
    // Track HDL
    pageTracker.startTracking("HDL");
    
    // Add HDL section
    addIndicatorSection(document,
        "HDL",
        String.valueOf(patient.getHDL()),
        helper.getHDLCategory(patient.getHDL()),
        "30 - 70",
        "mg/dL",
        patient.getHDLRecommendation()
    );
    
    // Track LDL
    pageTracker.startTracking("LDL");
    
    // Add LDL section
    addIndicatorSection(document,
        "LDL",
        String.valueOf(patient.getLDL()),
        helper.getLDLCategory(patient.getLDL()),
        "< 130",
        "mg/dL",
        patient.getLDLRecommendation()
    );
}

private static void addSummaryTableWithPageNumbers(Document document, Patient patient, Helper helper, 
                                              Map<String, Integer> pageNumberMap) throws DocumentException {
    // Create summary table
    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(100);
    table.setSpacingAfter(10);
    
    try {
        // Set column widths
        table.setWidths(new float[]{3f, 2f, 1.5f, 2f, 1f});
    } catch (DocumentException e) {
        e.printStackTrace();
    }
    
    // Header design
    Font headerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
    
    // Column header styles
    PdfPCell headerCell1 = new PdfPCell(new Phrase("Parameter", headerFont));
    headerCell1.setBackgroundColor(PRIMARY_COLOR);
    headerCell1.setPadding(6);
    headerCell1.setBorderColor(BORDER_COLOR);
    headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
    headerCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerCell1.setMinimumHeight(22);
    
    PdfPCell headerCell2 = new PdfPCell(new Phrase("Nilai Normal", headerFont));
    headerCell2.setBackgroundColor(PRIMARY_COLOR);
    headerCell2.setPadding(6);
    headerCell2.setBorderColor(BORDER_COLOR);
    headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
    headerCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerCell2.setMinimumHeight(22);
    
    PdfPCell headerCell3 = new PdfPCell(new Phrase("Hasil", headerFont));
    headerCell3.setBackgroundColor(PRIMARY_COLOR);
    headerCell3.setPadding(6);
    headerCell3.setBorderColor(BORDER_COLOR);
    headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
    headerCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerCell3.setMinimumHeight(22);
    
    PdfPCell headerCell4 = new PdfPCell(new Phrase("Kategori", headerFont));
    headerCell4.setBackgroundColor(PRIMARY_COLOR);
    headerCell4.setPadding(6);
    headerCell4.setBorderColor(BORDER_COLOR);
    headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
    headerCell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerCell4.setMinimumHeight(22);
    
    PdfPCell headerCell5 = new PdfPCell(new Phrase("Hal.", headerFont));
    headerCell5.setBackgroundColor(PRIMARY_COLOR);
    headerCell5.setPadding(6);
    headerCell5.setBorderColor(BORDER_COLOR);
    headerCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
    headerCell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerCell5.setMinimumHeight(22);
    
    table.addCell(headerCell1);
    table.addCell(headerCell2);
    table.addCell(headerCell3);
    table.addCell(headerCell4);
    table.addCell(headerCell5);
    
    // Category header style
    Font categoryHeaderFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, SECONDARY_COLOR);
    
    // TANDA VITAL category header
    PdfPCell vitalSignsCategoryCell = new PdfPCell(new Phrase("TANDA VITAL", categoryHeaderFont));
    vitalSignsCategoryCell.setColspan(5);
    vitalSignsCategoryCell.setBackgroundColor(LIGHT_BG_COLOR);
    vitalSignsCategoryCell.setPadding(5);
    vitalSignsCategoryCell.setBorderColor(BORDER_COLOR);
    vitalSignsCategoryCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    vitalSignsCategoryCell.setMinimumHeight(20);
    table.addCell(vitalSignsCategoryCell);
    
    // Add vital signs rows with dynamic page numbers (default to 2 if not found)
    addImproveSummaryRow(table, "Tekanan Darah (mmHg)", 
        "90/60 - 120/80", 
        patient.getSystolicPressure() + "/" + patient.getDiastolicPressure(),
        helper.getBloodPressureCategory(patient.getSystolicPressure(), patient.getDiastolicPressure()),
        pageNumberMap.getOrDefault("Tekanan Darah", 2));
    
    addImproveSummaryRow(table, "Denyut Nadi (bpm)", 
        "60 - 100", 
        String.valueOf(patient.getPulse()),
        helper.getPulseCategory(patient.getPulse()),
        pageNumberMap.getOrDefault("Denyut Nadi", 2));
    
    addImproveSummaryRow(table, "Laju Pernafasan (rpm)", 
        "12 - 20", 
        String.valueOf(patient.getRespiratoryRate()),
        helper.getRespiratoryCategory(patient.getRespiratoryRate()),
        pageNumberMap.getOrDefault("Laju Pernafasan", 3));
    
    addImproveSummaryRow(table, "Suhu (°C)", 
        "36 - 37.5", 
        String.valueOf(patient.getTemperature()),
        helper.getTemperatureCategory(patient.getTemperature()),
        pageNumberMap.getOrDefault("Suhu", 3));
    
    addImproveSummaryRow(table, "Indeks Massa Tubuh (kg/m²)", 
        "18.5 - 25", 
        String.valueOf(patient.getBMI()),
        helper.getBMICategory(patient.getBMI()),
        pageNumberMap.getOrDefault("Indeks Massa Tubuh", 4));
    
    // HASIL LABORATORIUM category header
    PdfPCell labResultsCategoryCell = new PdfPCell(new Phrase("HASIL LABORATORIUM", categoryHeaderFont));
    labResultsCategoryCell.setColspan(5);
    labResultsCategoryCell.setBackgroundColor(LIGHT_BG_COLOR);
    labResultsCategoryCell.setPadding(5);
    labResultsCategoryCell.setBorderColor(BORDER_COLOR);
    labResultsCategoryCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    labResultsCategoryCell.setMinimumHeight(20);
    table.addCell(labResultsCategoryCell);
    
    // Add lab parameter rows with dynamic page numbers (default to standard values if not found)
    addImproveSummaryRow(table, "Hemoglobin (g/dL)", 
        patient.getGender().equalsIgnoreCase("male") ? "13 - 18" : "12 - 16", 
        String.valueOf(patient.getHaemoglobin()),
        helper.getHemoglobinCategory(patient.getHaemoglobin(), patient.getGender()),
        pageNumberMap.getOrDefault("Hemoglobin", 6));
    
    addImproveSummaryRow(table, "Eosinofil (%)", 
        "0 - 6", 
        String.valueOf(patient.getEosinophil()),
        helper.getEosinophilCategory(patient.getEosinophil()),
        pageNumberMap.getOrDefault("Eosinofil", 6));
    
    addImproveSummaryRow(table, "MCV (fL)", 
        "80 - 100", 
        String.valueOf(patient.getMCV()),
        helper.getMCVCategory(patient.getMCV()),
        pageNumberMap.getOrDefault("MCV", 7));
    
    addImproveSummaryRow(table, "Laju Endap Darah (LED) (mm/jam)", 
        patient.getGender().equalsIgnoreCase("male") ? "< 15" : "< 20", 
        String.valueOf(patient.getLED()),
        helper.getLEDCategory(patient.getLED(), patient.getGender()),
        pageNumberMap.getOrDefault("Laju Endap Darah (LED)", 7));
    
    addImproveSummaryRow(table, "Asam Urat (mg/dL)", 
        patient.getGender().equalsIgnoreCase("male") ? "3.6 - 8.5" : "2.3 - 6.6", 
        String.valueOf(patient.getUricAcid()),
        helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender()),
        pageNumberMap.getOrDefault("Asam Urat", 8));
    
    addImproveSummaryRow(table, "Glukosa (mg/dL)", 
        "70 - 100", 
        String.valueOf(patient.getGlucose()),
        helper.getGlucoseCategory(patient.getGlucose()),
        pageNumberMap.getOrDefault("Glukosa", 8));
    
    addImproveSummaryRow(table, "Kolesterol Total (mg/dL)", 
        "< 200", 
        String.valueOf(patient.getTotalCholesterol()),
        helper.getTotalCholesterolCategory(patient.getTotalCholesterol()),
        pageNumberMap.getOrDefault("Kolesterol Total", 9));
    
    addImproveSummaryRow(table, "Trigliserida (mg/dL)", 
        patient.getGender().equalsIgnoreCase("male") ? "35 - 135" : "40 - 160", 
        String.valueOf(patient.getTriglyceride()),
        helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender()),
        pageNumberMap.getOrDefault("Trigliserida", 9));
    
    addImproveSummaryRow(table, "HDL (mg/dL)", 
        "30 - 70", 
        String.valueOf(patient.getHDL()),
        helper.getHDLCategory(patient.getHDL()),
        pageNumberMap.getOrDefault("HDL", 10));
    
    addImproveSummaryRow(table, "LDL (mg/dL)", 
        "< 130", 
        String.valueOf(patient.getLDL()),
        helper.getLDLCategory(patient.getLDL()),
        pageNumberMap.getOrDefault("LDL", 11));
    
    // Add the table to the document
    document.add(table);
    
    // Footer note
    Font noteFont = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC, SECONDARY_COLOR);
    Paragraph notePara = new Paragraph("Catatan: Halaman merujuk pada detail rekomendasi untuk setiap parameter", 
        noteFont);
    notePara.setSpacingBefore(2);
    notePara.setSpacingAfter(5);
    notePara.setAlignment(Element.ALIGN_LEFT);
    document.add(notePara);
}

    // Modified addVitalSigns method - now accepts PageReferenceOverlayHelper
    private static void addVitalSigns(Document document, Patient patient, Helper helper, PageReferenceOverlayHelper pageHelper) throws DocumentException {
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
    addIndicatorSection(bpCell,
    "Tekanan Darah",
    patient.getSystolicPressure() + "/" + patient.getDiastolicPressure(),
    helper.getBloodPressureCategory(patient.getSystolicPressure(), patient.getDiastolicPressure()),
    "90/60 - 120/80",
    "mmHg",
    patient.getBloodPressureRecommendation()
    );
    containerTable.addCell(bpCell);

    document.add(containerTable);
    
    // Record page number for Tekanan Darah
    pageHelper.setParameterPage("Tekanan Darah", document.getPageNumber());

    // Pulse
    addIndicatorSection(document,
        "Denyut Nadi",
        String.valueOf(patient.getPulse()),
        helper.getPulseCategory(patient.getPulse()),
        "60 - 100",
        "bpm",
        patient.getPulseRecommendation()
    );
    pageHelper.setParameterPage("Denyut Nadi", document.getPageNumber());

    // Respiratory Rate
    addIndicatorSection(document,
        "Laju Pernafasan",
        String.valueOf(patient.getRespiratoryRate()),
        helper.getRespiratoryCategory(patient.getRespiratoryRate()),
        "12 - 20",
        "rpm",
        patient.getRespiratoryRateRecommendation()
    );
    pageHelper.setParameterPage("Laju Pernafasan", document.getPageNumber());

    // Temperature
    addIndicatorSection(document,
        "Suhu",
        String.valueOf(patient.getTemperature()),
        helper.getTemperatureCategory(patient.getTemperature()),
        "36 - 37.5",
        "°C",
        patient.getTemperatureRecommendation()
    );
    pageHelper.setParameterPage("Suhu", document.getPageNumber());

    // BMI
    addIndicatorSection(document,
        "Indeks Massa Tubuh",
        String.valueOf(patient.getBMI()),
        helper.getBMICategory(patient.getBMI()),
        "18.5 - 25",
        "kg/m²",
        patient.getBMIRecommendation()
    );
    pageHelper.setParameterPage("Indeks Massa Tubuh", document.getPageNumber());
}

// Modified addLaboratoryResults method - now accepts PageReferenceOverlayHelper
private static void addLaboratoryResults(Document document, Patient patient, Helper helper, PageReferenceOverlayHelper pageHelper) throws DocumentException {
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
    
    // Record page number for Hemoglobin
    pageHelper.setParameterPage("Hemoglobin", document.getPageNumber());

    // Eosinophil
    addIndicatorSection(document,
        "Eosinofil",
        String.valueOf(patient.getEosinophil()),
        helper.getEosinophilCategory(patient.getEosinophil()),
        "0 - 6",
        "%",
        patient.getEosinophilRecommendation()
    );
    pageHelper.setParameterPage("Eosinofil", document.getPageNumber());

    // MCV
    addIndicatorSection(document,
        "MCV",
        String.valueOf(patient.getMCV()),
        helper.getMCVCategory(patient.getMCV()),
        "80 - 100",
        "fL",
        patient.getMCVRecommendation()
    );
    pageHelper.setParameterPage("MCV", document.getPageNumber());

    // LED
    addIndicatorSection(document,
        "Laju Endap Darah (LED)",
        String.valueOf(patient.getLED()),
        helper.getLEDCategory(patient.getLED(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "< 15" : "< 20",
        "mm/jam",
        patient.getLEDRecommendation()
    );
    pageHelper.setParameterPage("Laju Endap Darah (LED)", document.getPageNumber());

    // Uric Acid
    addIndicatorSection(document,
        "Asam Urat",
        String.valueOf(patient.getUricAcid()),
        helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "3.6 - 8.5" : "2.3 - 6.6",
        "mg/dL",
        patient.getUricAcidRecommendation()
    );
    pageHelper.setParameterPage("Asam Urat", document.getPageNumber());

    // Glucose
    addIndicatorSection(document,
        "Glukosa",
        String.valueOf(patient.getGlucose()),
        helper.getGlucoseCategory(patient.getGlucose()),
        "70 - 100",
        "mg/dL",
        patient.getGlucoseRecommendation()
    );
    pageHelper.setParameterPage("Glukosa", document.getPageNumber());

    // Total Cholesterol
    addIndicatorSection(document,
        "Kolesterol Total",
        String.valueOf(patient.getTotalCholesterol()),
        helper.getTotalCholesterolCategory(patient.getTotalCholesterol()),
        "< 200",
        "mg/dL",
        patient.getTotalCholesterolRecommendation()
    );
    pageHelper.setParameterPage("Kolesterol Total", document.getPageNumber());

    // Triglyceride
    addIndicatorSection(document,
        "Trigliserida",
        String.valueOf(patient.getTriglyceride()),
        helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender()),
        patient.getGender().equalsIgnoreCase("male") ? "35 - 135" : "40 - 160",
        "mg/dL",
        patient.getTriglycerideRecommendation()
    );
    pageHelper.setParameterPage("Trigliserida", document.getPageNumber());

    // HDL
    addIndicatorSection(document,
        "HDL",
        String.valueOf(patient.getHDL()),
        helper.getHDLCategory(patient.getHDL()),
        "30 - 70",
        "mg/dL",
        patient.getHDLRecommendation()
    );
    pageHelper.setParameterPage("HDL", document.getPageNumber());

    // LDL
    addIndicatorSection(document,
        "LDL",
        String.valueOf(patient.getLDL()),
        helper.getLDLCategory(patient.getLDL()),
        "< 130",
        "mg/dL",
        patient.getLDLRecommendation()
    );
    pageHelper.setParameterPage("LDL", document.getPageNumber());
}
}