import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileConverterUI extends JFrame {
    private JTextField filePathField;
    private JTextField savePathField;
    private JComboBox<String> formatComboBox;
    private JButton convertButton;
    private JButton browseFileButton;
    private JButton browseSaveButton;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private FileConverterService converterService;
    private Font montserratFont;

    public FileConverterUI() {
        converterService = new FileConverterService();
        loadCustomFont();
        initializeUI();
    }

    private void loadCustomFont() {
        try {
            // Пробуем загрузить Montserrat из ресурсов или использовать системный шрифт
            InputStream fontStream = getClass().getResourceAsStream("/fonts/Montserrat-Regular.ttf");
            if (fontStream != null) {
                montserratFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(12f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(montserratFont);
            } else {
                // Fallback на системные шрифты
                montserratFont = new Font("Segoe UI", Font.PLAIN, 12);
                System.out.println("Montserrat font not found, using fallback: " + montserratFont.getFontName());
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading Montserrat font: " + e.getMessage());
            montserratFont = new Font("Segoe UI", Font.PLAIN, 12);
        }
    }

    private void initializeUI() {
        setTitle("File Converter Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Устанавливаем минимальный и максимальный размер окна
        setMinimumSize(new Dimension(820, 720));
        setMaximumSize(new Dimension(1200, 800));
        setPreferredSize(new Dimension(800, 650));

        setLocationRelativeTo(null);
        setIconImage(createAppIcon());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Основная панель с современным оформлением
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Заголовок
        JLabel titleLabel = new JLabel("File Converter Pro", JLabel.CENTER);
        titleLabel.setFont(getDerivedFont(Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 100, 200));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Центральная панель с настройками
        JPanel centerPanel = createCenterPanel();

        // Панель лога
        JPanel logPanel = createLogPanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setupEventHandlers();

        // Упаковываем и центрируем
        pack();
        setLocationRelativeTo(null);
    }

    private Font getDerivedFont(int style, float size) {
        if (montserratFont != null) {
            return montserratFont.deriveFont(style, size);
        }
        return new Font("Segoe UI", style, (int) size);
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Выбор файла
        panel.add(createFileSelectionPanel());

        // Выбор пути сохранения
        panel.add(createSavePathPanel());

        // Выбор формата
        panel.add(createFormatPanel());

        // Кнопка конвертации
        panel.add(createButtonPanel());

        return panel;
    }

    private JPanel createFileSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("Выберите файл для конвертации:");
        label.setFont(getDerivedFont(Font.PLAIN, 14));

        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        filePathField = new JTextField();
        filePathField.setEditable(false);
        filePathField.setBackground(Color.WHITE);
        filePathField.setPreferredSize(new Dimension(300, 35));
        filePathField.setFont(getDerivedFont(Font.PLAIN, 13));
        filePathField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        browseFileButton = createStyledButton("Обзор", new Color(70, 130, 180));
        browseFileButton.setPreferredSize(new Dimension(80, 35));

        fieldPanel.add(filePathField, BorderLayout.CENTER);
        fieldPanel.add(browseFileButton, BorderLayout.EAST);

        panel.add(label, BorderLayout.NORTH);
        panel.add(fieldPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSavePathPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("Сохранить в папку:");
        label.setFont(getDerivedFont(Font.PLAIN, 14));

        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        savePathField = new JTextField();
        savePathField.setEditable(true);
        savePathField.setText(System.getProperty("user.home") + File.separator + "Downloads");
        savePathField.setBackground(Color.WHITE);
        savePathField.setPreferredSize(new Dimension(300, 35));
        savePathField.setFont(getDerivedFont(Font.PLAIN, 13));
        savePathField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        browseSaveButton = createStyledButton("Выбрать", new Color(70, 130, 180));
        browseSaveButton.setPreferredSize(new Dimension(80, 35));

        fieldPanel.add(savePathField, BorderLayout.CENTER);
        fieldPanel.add(browseSaveButton, BorderLayout.EAST);

        panel.add(label, BorderLayout.NORTH);
        panel.add(fieldPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormatPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("Целевой формат:");
        label.setFont(getDerivedFont(Font.PLAIN, 14));

        String[] formats = {"JPEG", "PNG", "PDF", "TXT", "DOCX", "JPG", "GIF", "BMP"};
        formatComboBox = new JComboBox<>(formats);
        formatComboBox.setBackground(Color.WHITE);
        formatComboBox.setFont(getDerivedFont(Font.PLAIN, 14));
        formatComboBox.setPreferredSize(new Dimension(300, 40));
        formatComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        panel.add(label, BorderLayout.NORTH);
        panel.add(formatComboBox, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(245, 245, 245));

        convertButton = createStyledButton("Конвертировать файл", new Color(53, 158, 98));
        convertButton.setPreferredSize(new Dimension(220, 50));
        convertButton.setFont(getDerivedFont(Font.BOLD, 16));

        panel.add(convertButton);
        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("Лог выполнения:");
        label.setFont(getDerivedFont(Font.BOLD, 14));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));

        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setBackground(new Color(253, 253, 253));
        logArea.setFont(getDerivedFont(Font.PLAIN, 12));
        logArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(700, 150));

        // Прогресс бар
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setForeground(new Color(41, 128, 185));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setBorder(new EmptyBorder(10, 0, 0, 0));
        progressBar.setPreferredSize(new Dimension(700, 20));

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // Полностью заливаем кнопку цветом
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Заливка фона
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Текст
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Рисуем границу
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground().darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(getDerivedFont(Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Эффект при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
        });

        return button;
    }

    private Image createAppIcon() {
        // Создаем простую иконку
        int size = 32;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Фон
        g2d.setColor(new Color(70, 130, 180));
        g2d.fillRoundRect(0, 0, size, size, 8, 8);

        // Иконка
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(8, 8, 10, 12); // Файл
        g2d.drawLine(8, 11, 18, 11); // Линии файла
        g2d.drawLine(8, 14, 18, 14);

        g2d.drawRect(14, 14, 10, 12); // Перекрывающийся файл
        g2d.drawLine(14, 17, 24, 17);
        g2d.drawLine(14, 20, 24, 20);

        g2d.dispose();
        return image;
    }

    private void setupEventHandlers() {
        browseFileButton.addActionListener(this::browseFile);
        browseSaveButton.addActionListener(this::browseSaveFolder);
        convertButton.addActionListener(this::convertFile);
    }

    private void browseFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Выберите файл для конвертации");

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            log("Выбран файл: " + selectedFile.getName());
            updateAvailableFormats(selectedFile);
        }
    }

    private void browseSaveFolder(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Выберите папку для сохранения");
        fileChooser.setCurrentDirectory(new File(savePathField.getText()));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            savePathField.setText(selectedFolder.getAbsolutePath());
            log("Папка сохранения: " + selectedFolder.getAbsolutePath());
        }
    }

    private void updateAvailableFormats(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.matches(".*\\.(jpg|jpeg|png|gif|bmp|webp|tiff|ico)$")) {
            String[] imageFormats = {"JPEG", "PNG", "JPG", "GIF", "BMP", "PDF"};
            formatComboBox.setModel(new DefaultComboBoxModel<>(imageFormats));
            log("Доступные форматы для изображений");
        } else if (fileName.matches(".*\\.(pdf|doc|docx|txt|odt|rtf)$")) {
            String[] docFormats = {"PDF", "TXT", "DOCX", "JPG", "PNG"};
            formatComboBox.setModel(new DefaultComboBoxModel<>(docFormats));
            log("Доступные форматы для документов");
        }
    }

    private void convertFile(ActionEvent e) {
        String filePath = filePathField.getText();
        String savePath = savePathField.getText();
        String targetFormat = (String) formatComboBox.getSelectedItem();

        if (filePath.isEmpty()) {
            showError("Пожалуйста, выберите файл для конвертации");
            return;
        }

        if (savePath.isEmpty()) {
            showError("Пожалуйста, укажите папку для сохранения");
            return;
        }

        File saveDir = new File(savePath);
        if (!saveDir.exists() || !saveDir.isDirectory()) {
            showError("Указанная папка для сохранения не существует или недоступна");
            return;
        }

        // Запуск конвертации в отдельном потоке
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    convertButton.setEnabled(false);
                    log("Начало конвертации...");
                    log("Исходный файл: " + new File(filePath).getName());
                    log("Целевой формат: " + targetFormat);
                    log("Сохранение в: " + savePath);
                });

                File inputFile = new File(filePath);
                String outputPath = converterService.convertFile(inputFile, targetFormat, savePath);

                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    convertButton.setEnabled(true);

                    if (outputPath != null) {
                        log("Конвертация завершена успешно!");
                        log("Результат: " + new File(outputPath).getName());

                        int option = JOptionPane.showConfirmDialog(this,
                                "<html><b>Файл успешно сконвертирован!</b><br><br>" +
                                        "Файл: " + new File(outputPath).getName() + "<br>" +
                                        "Размер: " + getFileSize(new File(outputPath)) + "<br><br>" +
                                        "Открыть папку с файлом?</html>",
                                "Успех",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE);

                        if (option == JOptionPane.YES_OPTION) {
                            openFileDirectory(outputPath);
                        }
                    } else {
                        log("Ошибка конвертации");
                        showError("Не удалось сконвертировать файл. Проверьте поддерживаемые форматы и попробуйте снова.");
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    convertButton.setEnabled(true);
                    log("Ошибка: " + ex.getMessage());
                    showError("Ошибка при конвертации: " + ex.getMessage());
                });
            }
        }).start();
    }

    private String getFileSize(File file) {
        long bytes = file.length();
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }

    private void openFileDirectory(String filePath) {
        try {
            File file = new File(filePath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file.getParentFile());
            log("Открыта папка с результатом");
        } catch (Exception ex) {
            log("Не удалось открыть папку: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><b>Ошибка</b><br>" + message + "</html>",
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            logArea.append("[" + timestamp + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}