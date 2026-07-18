package ru.example.creativespy;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Отвечает за запись и чтение истории действий игрока в креативе.
 * Каждый игрок хранится в отдельном файле logs/<uuid>.log
 */
public class LogManager {

    private final File logsFolder;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public LogManager(JavaPlugin plugin) {
        this.logsFolder = new File(plugin.getDataFolder(), "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
    }

    private File getFile(UUID uuid) {
        return new File(logsFolder, uuid.toString() + ".log");
    }

    public void log(UUID uuid, String message) {
        File file = getFile(uuid);
        String line = LocalDateTime.now().format(FORMAT) + " | " + message + System.lineSeparator();
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readLogs(UUID uuid) {
        File file = getFile(uuid);
        if (!file.exists()) {
            return Collections.emptyList();
        }
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
