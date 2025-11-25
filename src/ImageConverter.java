import java.io.File;

public class ImageConverter {
    public String convert(File inputFile, String targetFormat, Object api) {
        // Этот класс оставлен для обратной совместимости
        // В новой архитектуре конвертация выполняется через сервисы
        throw new UnsupportedOperationException("Используйте FileConverterAPI вместо прямого вызова конвертеров");
    }
}