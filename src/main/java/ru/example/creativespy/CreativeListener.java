package ru.example.creativespy;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;

public class CreativeListener implements Listener {

    private final LogManager logManager;

    public CreativeListener(LogManager logManager) {
        this.logManager = logManager;
    }

    // Фиксируем вход/выход из креатива
    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GameMode oldMode = player.getGameMode(); // на момент события - это ещё старый режим
        GameMode newMode = event.getNewGameMode();

        if (newMode == GameMode.CREATIVE && oldMode != GameMode.CREATIVE) {
            logManager.log(player.getUniqueId(), "ЗАШЁЛ в креатив (" + player.getName() + ")");
        } else if (oldMode == GameMode.CREATIVE && newMode != GameMode.CREATIVE) {
            logManager.log(player.getUniqueId(), "ВЫШЕЛ из креатива (" + player.getName() + ")");
        }
    }

    // Фиксируем взятие предметов из креативного инвентаря
    @EventHandler
    public void onCreativeInventory(InventoryCreativeEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType() == Material.AIR) {
            return;
        }
        if (cursor.getAmount() <= 0) {
            return;
        }

        String itemName = cursor.getType().name();
        int amount = cursor.getAmount();

        logManager.log(player.getUniqueId(),
                "ВЗЯЛ ИЗ КРЕАТИВА: " + amount + "x " + itemName);
    }
}
