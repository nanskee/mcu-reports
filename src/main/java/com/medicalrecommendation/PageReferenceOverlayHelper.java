package com.medicalrecommendation;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PageReferenceOverlayHelper extends PdfPageEventHelper {
    
    private BaseColor PRIMARY_COLOR;
    private BaseColor SECONDARY_COLOR;
    
    // Map to store parameter to page mappings
    private Map<String, Integer> parameterPageMap = new HashMap<>();
    
    // Store reference to the summary table page and its position
    private int summaryTablePage = 1; // Default to first page
    private Map<String, Float> rowYPositions = new HashMap<>();
    
    // Coordinates of the page number column in the summary table
    private float pageColX;
    private float tableWidth;
    private float tableStartY;
    
    private BaseFont baseFont;
    
    public PageReferenceOverlayHelper(BaseColor primaryColor, BaseColor secondaryColor) {
        this.PRIMARY_COLOR = primaryColor;
        this.SECONDARY_COLOR = secondaryColor;
        
        // Initialize the row Y positions map with parameter positions in the table
        setupParameterRowPositions();
        
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Set up the Y positions for each parameter in the summary table
    private void setupParameterRowPositions() {
        // These will be set up based on table structure
        // The row heights should match those in your summary table
        
        // Vital Signs section
        // Top header row and category row - not needed in map
        
        // First parameter row - Tekanan Darah (adjust for your table layout)
        float rowHeight = 18; // Height of each data row
        float currentY = 650; // Initial Y position (this is approximate - adjust to match your layout)
        
        // Store positions for vital signs parameters
        rowYPositions.put("Tekanan Darah", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Denyut Nadi", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Laju Pernafasan", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Suhu", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Indeks Massa Tubuh", currentY);
        currentY -= rowHeight;
        
        // Category row - subtract height
        currentY -= 20;
        
        // Lab Results parameters
        rowYPositions.put("Hemoglobin", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Eosinofil", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("MCV", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Laju Endap Darah (LED)", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Asam Urat", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Glukosa", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Kolesterol Total", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("Trigliserida", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("HDL", currentY);
        currentY -= rowHeight;
        
        rowYPositions.put("LDL", currentY);
    }
    
    // Set the coordinates for the summary table
    public void setSummaryTableCoordinates(int page, float tableWidth, float startY, float pageColX) {
        this.summaryTablePage = page;
        this.tableWidth = tableWidth;
        this.tableStartY = startY;
        this.pageColX = pageColX;
    }
    
    // Method to add parameter to page mapping when a parameter section is added
    public void setParameterPage(String paramName, int pageNumber) {
        parameterPageMap.put(paramName, pageNumber);
    }
    
    // Update these positions based on actual measurement of your table
    public void setRowPosition(String paramName, float yPosition) {
        rowYPositions.put(paramName, yPosition);
    }
    
    // For compatibility with the method calls in your code
    public void setSummaryTable(PdfPTable table) {
        // This method exists for compatibility but doesn't need to do anything
    }
    
    // For compatibility with the method calls in your code
    public void registerPageCell(String paramName, PdfPCell cell) {
        // This method exists for compatibility but doesn't need to do anything
    }
    
    // This will overlay the page numbers onto the summary table
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // Get the direct content for the summary table page
        PdfContentByte canvas = writer.getDirectContent();
        
        // Set the page to the summary table page
        canvas.saveState();
        
        // Get the page table appears on
        PdfContentByte canvasForPage = writer.getDirectContentUnder();
        // Add page numbers to the stored positions for each parameter
        for (Map.Entry<String, Integer> entry : parameterPageMap.entrySet()) {
            String paramName = entry.getKey();
            int pageNum = entry.getValue();
            
            // Only add if we have a position for this parameter
            if (rowYPositions.containsKey(paramName)) {
                // Draw the page number at the stored position
                float yPos = rowYPositions.get(paramName);
                
                // Create a template to overlay on the first page
                PdfTemplate template = canvasForPage.createTemplate(20, 15);
                template.beginText();
                template.setFontAndSize(baseFont, 9);
                template.showTextAligned(Element.ALIGN_CENTER, String.valueOf(pageNum), 10, 5, 0);
                template.endText();
                
                // Add the template to the first page at the appropriate position
                canvasForPage.addTemplate(template, pageColX, yPos - 5);
            }
        }
        
        canvas.restoreState();
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // Standard page numbering at the bottom
        Rectangle pageSize = document.getPageSize();
        PdfContentByte canvas = writer.getDirectContent();
        
        // Add page number
        canvas.beginText();
        canvas.setFontAndSize(baseFont, 8);
        canvas.setColorFill(SECONDARY_COLOR);
        String pageText = "Halaman " + writer.getPageNumber();
        canvas.showTextAligned(Element.ALIGN_CENTER, pageText, 
                               (pageSize.getLeft() + pageSize.getRight()) / 2, 
                               pageSize.getBottom() + 40, 0);
        canvas.endText();
    }
}