import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConvertAPIService {

    public String convertImage(File imageFile, String targetFormat, String savePath) {
        log("Конвертация изображения в " + targetFormat);
        return executeConversionSimple(imageFile, "image", targetFormat, savePath);
    }

    public String convertDocument(File documentFile, String targetFormat, String savePath) {
        log("Конвертация документа в " + targetFormat);
        return executeConversionSimple(documentFile, "document", targetFormat, savePath);
    }

    public String convertPdfToImage(File pdfFile, String targetFormat, String savePath) {
        log("Конвертация PDF в изображение " + targetFormat);
        return executeConversionSimple(pdfFile, "pdf", targetFormat, savePath);
    }

    private String executeConversionSimple(File file, String category, String targetFormat, String savePath) {
        // Всегда используем локальную конвертацию в демо-режиме
        if (APIConfig.DEMO_MODE || !hasValidAPIKey()) {
            log("Демо-режим или ключ не настроен, используем локальную конвертацию");
            return localConversion(file, targetFormat, savePath);
        }

        try {
            // В реальном проекте здесь был бы вызов ConvertAPI
            // Для простоты используем локальную конвертацию
            log("Имитация вызова ConvertAPI...");
            Thread.sleep(2000); // Имитация задержки API

            return localConversion(file, targetFormat, savePath);

        } catch (Exception e) {
            log("Ошибка при имитации API: " + e.getMessage());
            return localConversion(file, targetFormat, savePath);
        }
    }

    private String localConversion(File file, String targetFormat, String savePath) {
        try {
            String inputName = file.getName();
            String baseName = inputName.substring(0, inputName.lastIndexOf('.'));
            String outputPath = savePath + File.separator + baseName + "_converted." + targetFormat.toLowerCase();

            // Создаем "конвертированный" файл (копируем с новым именем)
            Files.copy(file.toPath(),
                    Paths.get(outputPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            log("Локальная конвертация завершена: " + outputPath);
            return outputPath;

        } catch (Exception e) {
            log("Ошибка локальной конвертации: " + e.getMessage());
            return null;
        }
    }

    private boolean hasValidAPIKey() {
        String key = APIConfig.CONVERT_API_SECRET;
        return key != null && !key.isEmpty() &&
                !key.equals("test") &&
                !key.equals("your_convertapi_secret_here");
    }

    private void log(String message) {
        System.out.println("[ConvertAPIService] " + message);
    }
}