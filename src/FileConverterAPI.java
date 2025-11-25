import java.io.File;

public class FileConverterAPI {
    private ConvertAPIService convertAPI;

    public FileConverterAPI() {
        this.convertAPI = new ConvertAPIService();
    }

    public String convertFile(File inputFile, String sourceFormat, String targetFormat, String savePath) {
        try {
            String selectedAPI = APISelector.selectBestAPI(inputFile, sourceFormat, targetFormat);

            log("Выбран метод: " + selectedAPI + " для конвертации " + sourceFormat + " → " + targetFormat);

            switch (selectedAPI) {
                case "ConvertAPI":
                    return useConvertAPI(inputFile, sourceFormat, targetFormat, savePath);

                case "Local":
                default:
                    return localConversion(inputFile, sourceFormat, targetFormat, savePath);
            }
        } catch (Exception e) {
            log("Ошибка при выборе метода конвертации: " + e.getMessage());
            return localConversion(inputFile, sourceFormat, targetFormat, savePath);
        }
    }

    private String useConvertAPI(File file, String source, String target, String savePath) {
        log("Использование ConvertAPI для конвертации...");

        // Определяем тип конвертации
        if (isImageFormat(source)) {
            if (isImageFormat(target)) {
                return convertAPI.convertImage(file, target.toLowerCase(), savePath);
            } else if (target.equalsIgnoreCase("pdf")) {
                return convertAPI.convertDocument(file, "pdf", savePath);
            }
        } else if (isDocumentFormat(source)) {
            if (isDocumentFormat(target)) {
                return convertAPI.convertDocument(file, target.toLowerCase(), savePath);
            } else if (isImageFormat(target)) {
                return convertAPI.convertPdfToImage(file, target.toLowerCase(), savePath);
            }
        }

        // Если ConvertAPI не поддерживает эту конвертацию, используем локальную
        log("ConvertAPI не поддерживает эту конвертацию, используем локальную");
        return localConversion(file, source, target, savePath);
    }

    private String localConversion(File file, String source, String target, String savePath) {
        log("Использование локальной конвертации...");
        try {
            // Создаем файл с новым расширением в указанной папке
            String inputName = file.getName();
            String baseName = inputName.substring(0, inputName.lastIndexOf('.'));
            String outputPath = savePath + File.separator + baseName + "_converted." + target.toLowerCase();

            // Копируем файл с новым именем
            java.nio.file.Files.copy(file.toPath(),
                    java.nio.file.Paths.get(outputPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            log("Локальная конвертация завершена: " + outputPath);
            return outputPath;

        } catch (Exception e) {
            log("Ошибка локальной конвертации: " + e.getMessage());
            return null;
        }
    }

    // Вспомогательные методы проверки форматов
    private boolean isImageFormat(String format) {
        return format.matches("(?i)jpg|jpeg|png|gif|bmp|webp|tiff|ico");
    }

    private boolean isDocumentFormat(String format) {
        return format.matches("(?i)pdf|txt|doc|docx|odt|rtf|xls|xlsx|ppt|pptx");
    }

    private void log(String message) {
        System.out.println("[FileConverterAPI] " + message);
    }
}