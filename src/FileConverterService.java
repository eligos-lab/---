import java.io.File;

public class FileConverterService {
    private FileConverterAPI api;

    public FileConverterService() {
        this.api = new FileConverterAPI();
    }

    public String convertFile(File inputFile, String targetFormat, String savePath) {
        try {
            String fileName = inputFile.getName();
            String fileExtension = getFileExtension(fileName);

            log("Определение типа файла: " + fileExtension);
            log("Папка сохранения: " + savePath);

            // Используем единый API, который сам выбирает лучший сервис
            String outputPath = api.convertFile(inputFile, fileExtension, targetFormat.toUpperCase(), savePath);

            if (outputPath != null) {
                log("Файл успешно сконвертирован: " + outputPath);
            } else {
                log("Не удалось сконвертировать файл");
            }

            return outputPath;

        } catch (Exception e) {
            log("Ошибка в сервисе конвертации: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    private void log(String message) {
        System.out.println("[FileConverterService] " + message);
    }
}