package ru.example.creativespy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CCommand implements CommandExecutor, TabCompleter {

    private final LogManager logManager;
    private static final int MAX_LINES = 50;

    public CCommand(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Использование: /c spec <ник>");
            return true;
        }

        if (!args[0].equalsIgnoreCase("spec")) {
            sender.sendMessage(ChatColor.RED + "Неизвестная подкоманда. Использование: /c spec <ник>");
            return true;
        }

        if (!sender.hasPermission("creativespy.spec")) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав на использование этой команды.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Укажите ник игрока: /c spec <ник>");
            return true;
        }

        String targetName = args[1];
        @SuppressWarnings("deprecation")
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Игрок с таким ником не найден на сервере.");
            return true;
        }

        List<String> logs = logManager.readLogs(target.getUniqueId());

        if (logs.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Для игрока " + targetName + " нет записей о креативе.");
            return true;
        }

        int from = Math.max(0, logs.size() - MAX_LINES);
        List<String> lastLogs = logs.subList(from, logs.size());

        sender.sendMessage(ChatColor.GOLD + "===== История креатива: " + targetName + " =====");
        for (String line : lastLogs) {
            sender.sendMessage(ChatColor.GRAY + line);
        }
        sender.sendMessage(ChatColor.GOLD + "==============================");
        if (logs.size() > MAX_LINES) {
            sender.sendMessage(ChatColor.YELLOW + "Показаны последние " + MAX_LINES + " записей из " + logs.size() + ".");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("spec");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("spec")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
        }
        return completions;
    }
}
