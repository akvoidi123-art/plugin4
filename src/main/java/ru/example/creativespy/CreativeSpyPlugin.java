package ru.example.creativespy;

import org.bukkit.plugin.java.JavaPlugin;

public class CreativeSpyPlugin extends JavaPlugin {

    private LogManager logManager;

    @Override
    public void onEnable() {
        logManager = new LogManager(this);
        getServer().getPluginManager().registerEvents(new CreativeListener(logManager), this);

        CCommand ccommand = new CCommand(logManager);
        getCommand("c").setExecutor(ccommand);
        getCommand("c").setTabCompleter(ccommand);

        getLogger().info("CreativeSpy включён!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CreativeSpy выключен!");
    }
}
