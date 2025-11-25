import java.io.File;

public class APISelector {

    public static String selectBestAPI(File file, String sourceFormat, String targetFormat) {
        // Если демо-режим, используем локальную конвертацию
        if (APIConfig.DEMO_MODE) {
            log("Демо-режим активен, используем локальную конвертацию");
            return "Local";
        }

        // Проверка API ключа ConvertAPI
        if (!hasValidConvertAPIKey()) {
            log("ConvertAPI ключ не настроен, используем локальную конвертацию");
            return "Local";
        }

        // Проверка размера файла
        long fileSize = file.length();
        if (fileSize > APIConfig.MAX_FILE_SIZE) {
            log("Файл слишком большой (" + fileSize + " байт), используем локальную конвертацию");
            return "Local";
        }

        // Проверка поддерживаемых форматов для ConvertAPI
        if (!isConvertAPISupported(sourceFormat, targetFormat)) {
            log("ConvertAPI не поддерживает конвертацию " + sourceFormat + " в " + targetFormat);
            return "Local";
        }

        log("Используем ConvertAPI для конвертации " + sourceFormat + " → " + targetFormat);
        return "ConvertAPI";
    }

    private static boolean hasValidConvertAPIKey() {
        String key = APIConfig.CONVERT_API_SECRET;
        return key != null && !key.isEmpty() &&
                !key.equals("test") &&
                !key.equals("your_convertapi_secret_here");
    }

    private static boolean isConvertAPISupported(String source, String target) {
        source = source.toLowerCase();
        target = target.toLowerCase();

        // Проверяем поддерживаемые комбинации конвертаций ConvertAPI
        if (isImageFormat(source) && isImageFormat(target)) {
            return true; // JPG to PNG, PNG to JPG и т.д.
        }

        if (isDocumentFormat(source) && isDocumentFormat(target)) {
            return true; // DOC to PDF, PDF to TXT и т.д.
        }

        // ConvertAPI также поддерживает некоторые кросс-конвертации
        if (isDocumentFormat(source) && isImageFormat(target)) {
            return true; // PDF to JPG, DOC to PNG и т.д.
        }

        if (isImageFormat(source) && isDocumentFormat(target)) {
            return true; // JPG to PDF, PNG to PDF и т.д.
        }

        return false;
    }

    private static boolean isImageFormat(String format) {
        for (String supported : APIConfig.SUPPORTED_IMAGE_FORMATS) {
            if (supported.equalsIgnoreCase(format)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDocumentFormat(String format) {
        for (String supported : APIConfig.SUPPORTED_DOCUMENT_FORMATS) {
            if (supported.equalsIgnoreCase(format)) {
                return true;
            }
        }
        return false;
    }

    private static void log(String message) {
        System.out.println("[APISelector] " + message);
    }
}