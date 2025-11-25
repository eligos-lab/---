public class APIConfig {
    // ConvertAPI - бесплатный тариф: 100 конвертаций в месяц
    // Получите ключ на: https://convertapi.com/
    public static final String CONVERT_API_SECRET = "test"; // Замените на реальный ключ
    public static final String CONVERT_API_URL = "https://v2.convertapi.com/convert";

    // Лимиты файлов
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB максимум для ConvertAPI

    // Настройки таймаутов
    public static final int CONNECTION_TIMEOUT = 30000; // 30 секунд
    public static final int SOCKET_TIMEOUT = 60000; // 60 секунд

    // Режимы работы
    public static final boolean DEMO_MODE = true; // true - локальная конвертация, false - реальный API
    public static final boolean FALLBACK_TO_LOCAL = true; // Автоматический переход на локальную конвертацию при ошибках

    // Поддерживаемые форматы ConvertAPI
    public static final String[] SUPPORTED_IMAGE_FORMATS = {"jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "ico"};
    public static final String[] SUPPORTED_DOCUMENT_FORMATS = {"pdf", "txt", "doc", "docx", "odt", "rtf", "xls", "xlsx", "ppt", "pptx"};
}