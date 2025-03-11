package com.medicalrecommendation;

import com.medicalrecommendation.model.Patient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RecommendationSummarizer {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationSummarizer.class);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    
    // Get the API key from application.properties or application.yml
    // If not using Spring's property injection, you can set this directly
    private static String OPENAI_API_KEY = null;
    
    // Simple value injection without nested placeholders
    @Value("${openai.api.key}")
    public void setApiKey(String apiKey) {
        // First try environment variable
        String envKey = System.getenv("OPEN_API_KEY");
        if (envKey != null && !envKey.isEmpty()) {
            OPENAI_API_KEY = envKey;
            logger.info("Using API key from environment variable");
        } 
        // Then fall back to properties file if no env var
        else if (apiKey != null && !apiKey.isEmpty() && !apiKey.equals("dummy-key-for-development")) {
            OPENAI_API_KEY = apiKey;
            logger.info("Using API key from properties file");
        }
        // Log warning if neither is available
        else {
            logger.warn("No valid API key found in environment or properties");
        }

}

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build();
    
    public static class RecommendationItem {
        private String category;
        private String status;
        private String summary;
        
        public RecommendationItem(String category, String status, String summary) {
            this.category = category;
            this.status = status;
            this.summary = summary;
        }
        
        // Getters
        public String getCategory() { return category; }
        public String getStatus() { return status; }
        public String getSummary() { return summary; }
    }
    
    public static List<RecommendationItem> generateSummary(Patient patient, Helper helper) {
        List<RecommendationItem> summaries = new ArrayList<>();
        
        // Add important vitals and lab results that need attention
        
        // Blood Pressure
        String bpCategory = helper.getBloodPressureCategory(patient.getSystolicPressure(), patient.getDiastolicPressure());
        if (!bpCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getBloodPressureRecommendation());
            summaries.add(new RecommendationItem("Tekanan Darah", bpCategory, recommendation));
        }
        
        // Pulse
        String pulseCategory = helper.getPulseCategory(patient.getPulse());
        if (!pulseCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getPulseRecommendation());
            summaries.add(new RecommendationItem("Denyut Nadi", pulseCategory, recommendation));
        }
        
        // Respiratory Rate
        String respCategory = helper.getRespiratoryCategory(patient.getRespiratoryRate());
        if (!respCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getRespiratoryRateRecommendation());
            summaries.add(new RecommendationItem("Laju Pernafasan", respCategory, recommendation));
        }
        
        // Temperature
        String tempCategory = helper.getTemperatureCategory(patient.getTemperature());
        if (!tempCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getTemperatureRecommendation());
            summaries.add(new RecommendationItem("Suhu", tempCategory, recommendation));
        }
        
        // BMI
        String bmiCategory = helper.getBMICategory(patient.getBMI());
        if (!bmiCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getBMIRecommendation());
            summaries.add(new RecommendationItem("Indeks Massa Tubuh", bmiCategory, recommendation));
        }
        
        // Hemoglobin
        String hbCategory = helper.getHemoglobinCategory(patient.getHaemoglobin(), patient.getGender());
        if (!hbCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getHaemoglobinRecommendation());
            summaries.add(new RecommendationItem("Hemoglobin", hbCategory, recommendation));
        }
        
        // Eosinophil
        String eoCategory = helper.getEosinophilCategory(patient.getEosinophil());
        if (!eoCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getEosinophilRecommendation());
            summaries.add(new RecommendationItem("Eosinofil", eoCategory, recommendation));
        }
        
        // MCV
        String mcvCategory = helper.getMCVCategory(patient.getMCV());
        if (!mcvCategory.equals("Normositik")) {
            String recommendation = summarizeRecommendation(patient.getMCVRecommendation());
            summaries.add(new RecommendationItem("MCV", mcvCategory, recommendation));
        }
        
        // LED
        String ledCategory = helper.getLEDCategory(patient.getLED(), patient.getGender());
        if (!ledCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getLEDRecommendation());
            summaries.add(new RecommendationItem("Laju Endap Darah", ledCategory, recommendation));
        }
        
        // Uric Acid
        String uaCategory = helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender());
        if (!uaCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getUricAcidRecommendation());
            summaries.add(new RecommendationItem("Asam Urat", uaCategory, recommendation));
        }
        
        // Glucose
        String glucoseCategory = helper.getGlucoseCategory(patient.getGlucose());
        if (!glucoseCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getGlucoseRecommendation());
            summaries.add(new RecommendationItem("Glukosa", glucoseCategory, recommendation));
        }
        
        // Total Cholesterol
        String cholesterolCategory = helper.getTotalCholesterolCategory(patient.getTotalCholesterol());
        if (!cholesterolCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getTotalCholesterolRecommendation());
            summaries.add(new RecommendationItem("Kolesterol Total", cholesterolCategory, recommendation));
        }
        
        // Triglyceride
        String trigCategory = helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender());
        if (!trigCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getTriglycerideRecommendation());
            summaries.add(new RecommendationItem("Trigliserida", trigCategory, recommendation));
        }
        
        // HDL
        String hdlCategory = helper.getHDLCategory(patient.getHDL());
        if (!hdlCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getHDLRecommendation());
            summaries.add(new RecommendationItem("HDL", hdlCategory, recommendation));
        }
        
        // LDL
        String ldlCategory = helper.getLDLCategory(patient.getLDL());
        if (!ldlCategory.equals("Normal")) {
            String recommendation = summarizeRecommendation(patient.getLDLRecommendation());
            summaries.add(new RecommendationItem("LDL", ldlCategory, recommendation));
        }
        
        return summaries;
    }
    
    // Method baru untuk menghasilkan rangkuman komprehensif dari semua parameter
    public static String generateComprehensiveRecommendation(Patient patient, Helper helper) {
        Map<String, String> allParameters = new HashMap<>();
        Map<String, String> allStatuses = new HashMap<>();
        Map<String, String> allRecommendations = new HashMap<>();
        
        // Kumpulkan semua parameter, status, dan rekomendasi
        
        // Tekanan Darah
        String bpCategory = helper.getBloodPressureCategory(patient.getSystolicPressure(), patient.getDiastolicPressure());
        allParameters.put("Tekanan Darah", patient.getSystolicPressure() + "/" + patient.getDiastolicPressure() + " mmHg");
        allStatuses.put("Tekanan Darah", bpCategory);
        allRecommendations.put("Tekanan Darah", patient.getBloodPressureRecommendation());
        
        // Denyut Nadi
        String pulseCategory = helper.getPulseCategory(patient.getPulse());
        allParameters.put("Denyut Nadi", patient.getPulse() + " bpm");
        allStatuses.put("Denyut Nadi", pulseCategory);
        allRecommendations.put("Denyut Nadi", patient.getPulseRecommendation());
        
        // Laju Pernafasan
        String respCategory = helper.getRespiratoryCategory(patient.getRespiratoryRate());
        allParameters.put("Laju Pernafasan", patient.getRespiratoryRate() + " rpm");
        allStatuses.put("Laju Pernafasan", respCategory);
        allRecommendations.put("Laju Pernafasan", patient.getRespiratoryRateRecommendation());
        
        // Suhu
        String tempCategory = helper.getTemperatureCategory(patient.getTemperature());
        allParameters.put("Suhu", patient.getTemperature() + " °C");
        allStatuses.put("Suhu", tempCategory);
        allRecommendations.put("Suhu", patient.getTemperatureRecommendation());
        
        // BMI
        String bmiCategory = helper.getBMICategory(patient.getBMI());
        allParameters.put("Indeks Massa Tubuh", patient.getBMI() + " kg/m²");
        allStatuses.put("Indeks Massa Tubuh", bmiCategory);
        allRecommendations.put("Indeks Massa Tubuh", patient.getBMIRecommendation());
        
        // Hemoglobin
        String hbCategory = helper.getHemoglobinCategory(patient.getHaemoglobin(), patient.getGender());
        allParameters.put("Hemoglobin", patient.getHaemoglobin() + " g/dL");
        allStatuses.put("Hemoglobin", hbCategory);
        allRecommendations.put("Hemoglobin", patient.getHaemoglobinRecommendation());
        
        // Eosinofil
        String eoCategory = helper.getEosinophilCategory(patient.getEosinophil());
        allParameters.put("Eosinofil", patient.getEosinophil() + " %");
        allStatuses.put("Eosinofil", eoCategory);
        allRecommendations.put("Eosinofil", patient.getEosinophilRecommendation());
        
        // MCV
        String mcvCategory = helper.getMCVCategory(patient.getMCV());
        allParameters.put("MCV", patient.getMCV() + " fL");
        allStatuses.put("MCV", mcvCategory);
        allRecommendations.put("MCV", patient.getMCVRecommendation());
        
        // LED
        String ledCategory = helper.getLEDCategory(patient.getLED(), patient.getGender());
        allParameters.put("Laju Endap Darah", patient.getLED() + " mm/jam");
        allStatuses.put("Laju Endap Darah", ledCategory);
        allRecommendations.put("Laju Endap Darah", patient.getLEDRecommendation());
        
        // Asam Urat
        String uaCategory = helper.getUricAcidCategory(patient.getUricAcid(), patient.getGender());
        allParameters.put("Asam Urat", patient.getUricAcid() + " mg/dL");
        allStatuses.put("Asam Urat", uaCategory);
        allRecommendations.put("Asam Urat", patient.getUricAcidRecommendation());
        
        // Glukosa
        String glucoseCategory = helper.getGlucoseCategory(patient.getGlucose());
        allParameters.put("Glukosa", patient.getGlucose() + " mg/dL");
        allStatuses.put("Glukosa", glucoseCategory);
        allRecommendations.put("Glukosa", patient.getGlucoseRecommendation());
        
        // Kolesterol Total
        String cholesterolCategory = helper.getTotalCholesterolCategory(patient.getTotalCholesterol());
        allParameters.put("Kolesterol Total", patient.getTotalCholesterol() + " mg/dL");
        allStatuses.put("Kolesterol Total", cholesterolCategory);
        allRecommendations.put("Kolesterol Total", patient.getTotalCholesterolRecommendation());
        
        // Trigliserida
        String trigCategory = helper.getTriglycerideCategory(patient.getTriglyceride(), patient.getGender());
        allParameters.put("Trigliserida", patient.getTriglyceride() + " mg/dL");
        allStatuses.put("Trigliserida", trigCategory);
        allRecommendations.put("Trigliserida", patient.getTriglycerideRecommendation());
        
        // HDL
        String hdlCategory = helper.getHDLCategory(patient.getHDL());
        allParameters.put("HDL", patient.getHDL() + " mg/dL");
        allStatuses.put("HDL", hdlCategory);
        allRecommendations.put("HDL", patient.getHDLRecommendation());
        
        // LDL
        String ldlCategory = helper.getLDLCategory(patient.getLDL());
        allParameters.put("LDL", patient.getLDL() + " mg/dL");
        allStatuses.put("LDL", ldlCategory);
        allRecommendations.put("LDL", patient.getLDLRecommendation());

         // Nonaktifkan OpenAI sementara, langsung gunakan metode manual
        //return createComprehensiveManualSummary(patient, allParameters, allStatuses, allRecommendations);
    
        
        try {
            // Coba gunakan OpenAI untuk membuat rangkuman komprehensif
            return createComprehensiveOpenAISummary(patient, allParameters, allStatuses, allRecommendations);
        } catch (Exception e) {
            logger.error("Error generating comprehensive recommendation: " + e.getMessage(), e);
            
            // Jika OpenAI gagal, gunakan metode pendekatan manual untuk membuat rangkuman
            return createComprehensiveManualSummary(patient, allParameters, allStatuses, allRecommendations);
        }
    }
    
    private static String createComprehensiveOpenAISummary(Patient patient, 
    Map<String, String> parameters,
    Map<String, String> statuses,
    Map<String, String> recommendations) throws IOException {
        // Persiapkan data pasien dalam format teks
        String gender = patient.getGender().equalsIgnoreCase("female") ? "Perempuan" : "Laki-laki";
        String pregnancyStatus = (gender.equals("Perempuan") && patient.isPregnant()) ? "Ya" : "Tidak";

        StringBuilder patientInfo = new StringBuilder();
        patientInfo.append("Profil Pasien:\n");
        patientInfo.append("- Nama: ").append(patient.getName()).append("\n");
        patientInfo.append("- Umur: ").append(patient.getAge()).append(" tahun\n");
        patientInfo.append("- Jenis Kelamin: ").append(gender).append("\n");
        if (gender.equals("Perempuan")) {
        patientInfo.append("- Hamil: ").append(pregnancyStatus).append("\n");
        }
        patientInfo.append("- Merokok: ").append(patient.isSmoking() ? "Ya" : "Tidak").append("\n");
        patientInfo.append("- Konsumsi Alkohol: ").append(patient.isAlcoholConsumption() ? "Ya" : "Tidak").append("\n");
        patientInfo.append("- Tinggi Badan: ").append(patient.getHeight()).append(" cm\n");
        patientInfo.append("- Berat Badan: ").append(patient.getWeight()).append(" kg\n");

        // Skip the health parameters section since we're focusing just on recommendations

        // Persiapkan semua rekomendasi yang ada
        StringBuilder allRecsText = new StringBuilder();
        allRecsText.append("Rekomendasi Per Parameter:\n");

        for (Map.Entry<String, String> entry : recommendations.entrySet()) {
        String paramName = entry.getKey();
        String recommendation = entry.getValue();

        if (recommendation != null && !recommendation.isEmpty()) {
        // Ekstrak hanya rekomendasi utama, hilangkan bagian penjelasan
        String mainRecommendation = extractMainRecommendation(recommendation);
        allRecsText.append("- ").append(paramName).append(": ").append(mainRecommendation).append("\n\n");
        }
        }

         // Buat prompt untuk OpenAI dengan format yang diperbarui
         String prompt = "Buatkan rangkuman rekomendasi medis komprehensif berdasarkan rekomendasi-rekomendasi individual dari hasil pemeriksaan pasien. "
         + "Organisasikan rekomendasi ke dalam kategori berikut dengan batasan yang SANGAT KETAT:\n\n"
         + "FORMAT OUTPUT SANGAT PENTING:\n"
         + "1. Setiap kategori HARUS diformat sebagai header dengan format '[NOMOR]. [NAMA KATEGORI]:'\n"
         + "2. Nama kategori HARUS DICETAK TEGAS sebagai header\n" 
         + "3. Konten rekomendasi harus langsung di bawah header (tanpa jeda baris)\n"
         + "4. SETIAP KATEGORI HARUS DALAM BENTUK BULLET POINTS bukan STRIP STRIP\n"
         + "5. INGAT JANGAN ADA DUPLIKASI\n"
         //+ "5. SETIAP KATEGORI JADIKAN DALAM SATU PARAGRAF LENGKAP YANG MENGALIR, KECUALI POLA MAKAN DAN NUTRISI YANG BISA LEBIH DARI SATU PARAGRAF\n\n"
         + "Kategori yang digunakan:\n\n"
         + "1. Pola Makan dan Nutrisi:\n"
         + "   - HANYA rekomendasi terkait makanan, minuman, nutrisi, dan pola diet\n"
         + "   - INGAT Mulai dengan semua makanan/minuman yang DISARANKAN untuk dikonsumsi\n"
         + "   - INGAT Setelah itu baru bahas yang perlu DIHINDARI/DIBATASI\n"
         + "   - Contoh yang TERMASUK: jenis makanan, porsi, frekuensi makan, nutrisi, vitamin, mineral\n\n"
         + "   - PLEASE PLEASE PLEASE PLEASE NO MORE THAN 120 WORDS. SANGAT SANGAT PENTING: Jumlah kata HARUS BANGET CUMA BOLEH maksimal 80 kata dalam satu kategori'\n\n"
         + "   - Ga perlu mulai dengan Untuk menjaga kesehatan dan berat badan ideal, langsung aja disarankan\n\n "
         + "   - KATEGORI POLA MAKAN DAN NUTRISI MAKSIMAL 4 BULLET POINTS SAJA\n"
         + "   - Tidak perlu pakai subjek seperti nama/Anda, langsung kata kerja aja'\n\n"
         + "   - Tidak perlu mention trigliserida, penyerapan zat besi optimal, dsb. Fokus saja terkait pola makan dan nutrisi\n\n "
         + "   - Ingat jika ada kalimat seperti ini 'Anda disarankan untuk membatasi natrium hingga 1.500 mg/hari dan mengurangi garam, makanan berpengawet, dan kafein jika sensitif' ini masuknya di kategori Pola Makan dan Nutrisi."
         + "2. Aktivitas Fisik dan Olahraga:\n"
         + "   - HANYA rekomendasi terkait gerakan tubuh dan latihan fisik\n"
         + "   - PENTING PENTING BANGET: Buat rangkuman yang mengandung jenis, durasi, dan frekuensi olahraga\n"
         + "   - Contoh yang TERMASUK: jalan kaki, berenang, yoga, latihan beban, frekuensi olahraga\n"
         + "   - Contoh yang TIDAK TERMASUK: menghindari paparan debu/polusi, tidur cukup, mengelola stres\n\n"
         + "   -  PLEASE PLEASE PLEASE PLEASE NO MORE THAN 40 WORDS. PENTING: Jumlah kata HARUS BANGET CUMA BOLEH maksimal 40 kata dalam kategori'\n\n"
         + "   - KATEGORI AKTIVITAS FISIK DAN OLAHRAGA MAKSIMAL 2 BULLET POINTS SAJA\n"
         + "   - Tidak perlu pakai subjek seperti nama/Anda, langsung kata kerja aja'\n\n"
         + "   - Jangan sampai ada kata yang terulang lagi\n\n"
         + "   - Kalimat Rutinitas ini penting untuk menjaga berat badan ideal dan kesehatan secara kesuluruhan tidak perlu\n\n"
         + "3. Gaya Hidup dan Pemantauan Kesehatan:\n"
         + "   - Rekomendasi terkait kebiasaan sehari-hari SELAIN makan dan olahraga\n"
         + "   - PENTING: JUMLAH JAM tidur perlu dimention ya, manajemen stres, menghindari paparan debu/polusi, pemantauan tekanan darah\n"
         + "   - Buat RUNUT seperti storytelling, dari pagi hingga malam, atau dari yang paling penting\n\n"
         + "   - Nutrisi ga ada yang disini.\n\n"
         + "   -  PLEASE PLEASE PLEASE PLEASE NO MORE THAN 40 WORDS. PENTING: Jumlah kata HARUS BANGET CUMA BOLEH maksimal 40 kata dalam satu kategori'\n\n"
         + "    - KATEGORI GAYA HIDUP DAN PEMAUNTAUAN KESEHATAN MAKSIMAL 4 BULLET POINTS SAJA\n"
         + "   - Tidak perlu pakai subjek seperti nama/Anda, langsung kata kerja aja'\n\n"
         + "4. Konsultasi Medis:\n"
         + "   - HANYA daftar hal yang memerlukan konsultasi dengan dokter atau tenaga medis\n"
         + "   - Format: 'Konsultasikan dengan dokter untuk [tujuan spesifik]'\n\n"
         + "   - PLEASE PLEASE PLEASE PLEASE NO MORE THAN 40WORDS.PENTING: Jumlah kata HARUS BANGET CUMA BOLEH maksimal 40 kata dalam satu kategori'\n\n"
         + "    - KATEGORI KONSULTASI MEDIS MAKSIMAL 2 BULLET POINTS SAJA\n"
         + "   - Tidak perlu pakai subjek seperti nama/Anda, langsung kata kerja aja'\n\n"
         + "   - Lebih kayak gini bisa ga  Konsultasikan dengan dokter untuk menentukan penyebab [...], [...], [...]'\n\n"
         + "SANGAT PENTING: Jangan mencampuradukkan kategori. Misalnya, menghindari paparan debu adalah bagian dari GAYA HIDUP, bukan Aktivitas Fisik. Tidur cukup adalah GAYA HIDUP, bukan Aktivitas Fisik.\n\n"
         + "PENTING UNTUK MEMASTIKAN OUTPUT FORMAT YANG BENAR:\n"
         + "1. Pastikan format header kategori persis seperti ini: '1. Pola Makan dan Nutrisi:'\n"
         + "2. Teks konten LANGSUNG di bawah header tanpa baris kosong di antaranya\n"
         + "3. Pastikan ada baris kosong sebelum header kategori berikutnya\n"
         + "4. JANGAN gunakan bullet points atau nomor dalam konten\n\n"
         + "SUDAH BAGUS, TAPI APA BISA DIBUAT LEBIH PADAT TAPI TETAP ALUR KALIMATNYA RAPI"
         + "TOLONG BACA PROMPTNYA SEKSAMA"
         + patientInfo.toString() + "\n"
         + allRecsText.toString();

        // Panggil OpenAI API
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4");

        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messagesArray.put(message);

        requestBody.put("messages", messagesArray);
        requestBody.put("temperature", 0.5);
        requestBody.put("max_tokens", 1500);

        RequestBody body = RequestBody.create(requestBody.toString(), JSON);

        Request request = new Request.Builder()
        .url(OPENAI_API_URL)
        .header("Authorization", "Bearer " + OPENAI_API_KEY)
        .header("Content-Type", "application/json")
        .post(body)
        .build();

        try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
        String errorBody = response.body() != null ? response.body().string() : "Unknown error";
        throw new IOException("OpenAI API request failed with code " + response.code() + ": " + errorBody);
        }

        String responseBody = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBody);

        String summary = jsonResponse.getJSONArray("choices")
        .getJSONObject(0)
        .getJSONObject("message")
        .getString("content")
        .trim();

        return summary;
        }
        }
    
    private static String createComprehensiveManualSummary(Patient patient, 
                                                          Map<String, String> parameters,
                                                          Map<String, String> statuses,
                                                          Map<String, String> recommendations) {
        // Kumpulkan rekomendasi berdasarkan kategori
        Map<String, List<String>> categorizedRecs = new HashMap<>();
        categorizedRecs.put("Pola Makan dan Nutrisi", new ArrayList<>());
        categorizedRecs.put("Aktivitas Fisik dan Olahraga", new ArrayList<>());
        categorizedRecs.put("Gaya Hidup dan Pemantauan Kesehatan", new ArrayList<>());
        categorizedRecs.put("Konsultasi Medis", new ArrayList<>());
        categorizedRecs.put("Lainnya", new ArrayList<>());
        
        // Kata kunci untuk pengelompokan rekomendasi
        String[] dietKeywords = {"makan", "konsumsi", "nutrisi", "diet", "buah", "sayur", "garam", "gula", 
                               "lemak", "protein", "kalori", "karbohidrat", "serat", "makanan", "minuman"};
        
        String[] exerciseKeywords = {"aktivitas", "olahraga", "jalan", "fisik", "gerakan", "latihan", 
                                    "senam", "aerobik", "berenang", "bersepeda", "yoga"};
        
        String[] lifestyleKeywords = {"tidur", "stres", "istirahat", "alkohol", "rokok", "merokok", "hidrasi", 
                                    "air", "cairan", "relaksasi", "monitor", "pantau", "teratur"};
        
        String[] medicalKeywords = {"dokter", "konsultasi", "pemeriksaan", "screening", "tes", "evaluasi", 
                                   "periksa", "spesialis", "medis", "klinik", "laboratorium", "obat", "rujukan"};
        
        // Kelompokkan rekomendasi berdasarkan kata kunci
        for (Map.Entry<String, String> entry : recommendations.entrySet()) {
            String paramName = entry.getKey();
            String recommendation = entry.getValue();
            
            if (recommendation == null || recommendation.isEmpty()) {
                continue;
            }
            
            // Ekstrak rekomendasi utama, hilangkan bagian penjelasan
            String mainRecommendation = extractMainRecommendation(recommendation);
            
            // Cek kategori berdasarkan kata kunci
            boolean categorized = false;
            
            for (String keyword : dietKeywords) {
                if (mainRecommendation.toLowerCase().contains(keyword)) {
                    categorizedRecs.get("Pola Makan dan Nutrisi").add(mainRecommendation);
                    categorized = true;
                    break;
                }
            }
            
            if (!categorized) {
                for (String keyword : exerciseKeywords) {
                    if (mainRecommendation.toLowerCase().contains(keyword)) {
                        categorizedRecs.get("Aktivitas Fisik dan Olahraga").add(mainRecommendation);
                        categorized = true;
                        break;
                    }
                }
            }
            
            if (!categorized) {
                for (String keyword : lifestyleKeywords) {
                    if (mainRecommendation.toLowerCase().contains(keyword)) {
                        categorizedRecs.get("Gaya Hidup dan Pemantauan Kesehatan").add(mainRecommendation);
                        categorized = true;
                        break;
                    }
                }
            }
            
            if (!categorized) {
                for (String keyword : medicalKeywords) {
                    if (mainRecommendation.toLowerCase().contains(keyword)) {
                        categorizedRecs.get("Konsultasi Medis").add(mainRecommendation);
                        categorized = true;
                        break;
                    }
                }
            }
            
            if (!categorized) {
                categorizedRecs.get("Lainnya").add(mainRecommendation);
            }
        }
        
        // Buat rangkuman komprehensif berdasarkan kategori
        StringBuilder summary = new StringBuilder();
        summary.append("REKOMENDASI KOMPREHENSIF\n\n");
        
        for (Map.Entry<String, List<String>> entry : categorizedRecs.entrySet()) {
            String category = entry.getKey();
            List<String> recs = entry.getValue();
            
            if (!recs.isEmpty()) {
                summary.append(category).append(":\n");
                
                // Deduplikasi rekomendasi yang serupa
                List<String> uniqueRecs = new ArrayList<>();
                for (String rec : recs) {
                    boolean isDuplicate = false;
                    for (String uniqueRec : uniqueRecs) {
                        if (isSimilarRecommendation(rec, uniqueRec)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        uniqueRecs.add(rec);
                    }
                }
                
                // Tambahkan rekomendasi unik ke ringkasan
                for (String rec : uniqueRecs) {
                    summary.append("• ").append(rec).append("\n");
                }
                
                summary.append("\n");
            }
        }
        
        return summary.toString();
    }
    
    // Metode untuk mendeteksi rekomendasi yang serupa
    private static boolean isSimilarRecommendation(String rec1, String rec2) {
        // Hitung Jaro-Winkler distance antara dua rekomendasi
        // Implementasi sederhana, bisa diganti dengan library jika tersedia
        // Untuk saat ini, gunakan perbandingan sederhana
        String[] words1 = rec1.toLowerCase().split("\\s+");
        String[] words2 = rec2.toLowerCase().split("\\s+");
        
        int commonWords = 0;
        for (String word1 : words1) {
            if (word1.length() < 4) continue; // Skip kata-kata pendek
            
            for (String word2 : words2) {
                if (word1.equals(word2)) {
                    commonWords++;
                    break;
                }
            }
        }
        
        double similarity = (double) commonWords / Math.min(words1.length, words2.length);
        return similarity > 0.5; // Threshold kesamaan
    }
    
    // Use OpenAI to summarize individual recommendations
    private static String summarizeRecommendation(String fullRecommendation) {
        if (fullRecommendation == null || fullRecommendation.isEmpty()) {
            return "";
        }
        
        // Remove explanation part if present
        String mainRecommendation = fullRecommendation;
        if (fullRecommendation.contains("---")) {
            String[] parts = fullRecommendation.split("---");
            mainRecommendation = parts[0].trim();
        }
        
        // If recommendation is already short, no need to summarize
        if (mainRecommendation.length() < 100) {
            return mainRecommendation;
        }
        
        try {
            return callOpenAIForSummary(mainRecommendation);
        } catch (Exception e) {
            logger.error("Error calling OpenAI API: " + e.getMessage(), e);
            
            // Fallback to simple truncation if API call fails
            if (mainRecommendation.length() > 150) {
                return mainRecommendation.substring(0, 147) + "...";
            } else {
                return mainRecommendation;
            }
        }
    }
    
    // Extract main recommendation without explanation part
    private static String extractMainRecommendation(String fullRecommendation) {
        if (fullRecommendation == null || fullRecommendation.isEmpty()) {
            return "";
        }
        
        // Split by "---" if present
        if (fullRecommendation.contains("---")) {
            String[] parts = fullRecommendation.split("---");
            return parts[0].trim();
        }
        
        return fullRecommendation.trim();
    }
    
    private static String callOpenAIForSummary(String text) throws IOException {
        String prompt = "Ringkas rekomendasi medis berikut menjadi satu kalimat singkat " +
                        "tanpa menghilangkan informasi penting. Hindari penggunaan jargon medis yang kompleks. " +
                        "Buat ringkasan tidak lebih dari 20 kata: " + text;
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        
        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messagesArray.put(message);
        
        requestBody.put("messages", messagesArray);
        requestBody.put("temperature", 0.3);
        requestBody.put("max_tokens", 100);
        
        RequestBody body = RequestBody.create(requestBody.toString(), JSON);
        
        Request request = new Request.Builder()
            .url(OPENAI_API_URL)
            .header("Authorization", "Bearer " + OPENAI_API_KEY)
            .header("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new IOException("OpenAI API request failed with code " + response.code() + ": " + errorBody);
            }
            
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
        }
    }
}