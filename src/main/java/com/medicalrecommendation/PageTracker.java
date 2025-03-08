package com.medicalrecommendation;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to track which parameters appear on which pages
 */
public class PageTracker extends PdfPageEventHelper {
    
    private Map<String, Integer> pageNumberMap = new HashMap<>();
    private String currentParameter = null;
    private int currentPageNumber = 0;
    
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        currentPageNumber = writer.getPageNumber();
        
        // If we're tracking a parameter, record the current page
        if (currentParameter != null) {
            pageNumberMap.put(currentParameter, currentPageNumber);
            // Keep tracking - don't reset currentParameter to null
        }
    }
    
    /**
     * Call this before adding a parameter section
     */
    public void startTracking(String parameterName) {
        this.currentParameter = parameterName;
        // Store the current page number
        pageNumberMap.put(parameterName, currentPageNumber);
    }
    
    /**
     * Explicitly set a page number for a parameter
     */
    public void setParameterPage(String parameterName, int pageNumber) {
        pageNumberMap.put(parameterName, pageNumber);
    }
    
    /**
     * Get the final page number map
     */
    public Map<String, Integer> getPageNumberMap() {
        return pageNumberMap;
    }
}