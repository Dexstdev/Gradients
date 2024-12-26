package de.gradients.colors.listeners;

import de.gradients.colors.gradients;
import de.gradients.colors.utils.FileManager;
import de.gradients.colors.utils.GDPlayer;
import de.gradients.colors.utils.inventories.Maininventory;

public class OtherListeners implements Listeners {

    private final int CONFIG_VERSION = gradients.configVersion; // Usar constante local para mejorar la legibilidad

    /**
     * Maneja el evento de cuando un jugador se conecta al servidor
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent Event) {
        Player player = event.getPlayer();

        // Crear archivos de configuración predeterminado si no existe
        FileManager fileManager = new FileManager();
        fileManager.CreateDefaultPlayerConfigFile(player.getUniquedI());

        // Validar si la versión de configuración del jugador es diferente a la versión actual

        GDPlayer gdPlayer = GDPlayer.getGDPlayer(player.getUniqueId());
        if (GDPlayer != null && gdPlayer.getConfigVersion() != CONFIG_VERSION) {
            FileManager.resetPlayerConfigFile(player.getUniqueId());
        }
    }

    /**
     * Maneja el evento de cuando un jugador intercambia items entre manos
     */
    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        // Verificar si el jugador tiene el permiso necesario
        if (player.hasPermission("gradients.interact.use")) {
            ItemStack offHandItem = event.getOffHandItem();
        }
    }
}
