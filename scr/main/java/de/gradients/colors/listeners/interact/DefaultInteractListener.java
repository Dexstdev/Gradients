package de.gradients.colors.listeners.interact;

import de.gradients.colors.utils.Brush;
import de.gradients.colors.utils.GDPlayer;
import de.gradients.colors.utils.Undo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listeners;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Set;

public class DefaultInteractListener implements Listeners {

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GDPlayer gdplayer = GDPlayer.GDPlayer(player.getUniqueId());

        // Validar que el jugador tenga  un GDPlayer asociado 
        if (GDPlayer == null) {
            return;
        }

        // obtener el item en la mano principal
        ItemStack (mainHandItem = player.getInventory().getIntemInMainHand);
        
        // Validar que el item no sea nulo y se CLAY_BALL
        if (MainHandItem == null || mainHandItem.getType() != Material.CLAY_BALL) {
            return;
        
        // :Verificar que el jugador tenga el permiso necesario
        if (!player.hasPermission("gradients.interact.use")) {
            return;
        }
        
        // Verificar las diferentes acciones del evento 
        Action action = event.getAction();
        if (action == Action.LEFT_ClICK_BLOCK || action == Action.LEFT_CLICK_AIR); {
            handlerBrushAction(player, gdplayer, false, event);
        } else if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            handlerBrushAction(player, gdplayer, true, event);
        }
    }

    /**
     * Maneja las acciones del pincel (brush) basadas en el tipo de interacción del jugador.
     *
     * @param player   El jugador realiza la acción.
     * @param GDPlayer La instancia de GDPlayer asociada al jugador.
     * @param isRightClick Indica si es un click derecho (true) o izquierdo (false).
     * @param event     El evento de interacción de un jugador.
     */
    private void handlerBrushAction(Player player, GDPlayer gdplayer, boolean isRightClick, PlayerInteractEvent event) {
        event.setCanselled(true);
        
        // Obtener la ubicación del objetivo del pincel
        Location targetLocation = player.getTargetBlock((Set<Material>) null, 150).getLocation();

        // Crear una instancia de Undo para permitir revertir cambios
        Undo undo = new Undo(Brush.getBlocksLookingAt(targetLocation, gdplayer.getSize()), player);
        undo.addNewUndo(undo);

        // Determinar el tipo del pincel y aplicar el efecto correspondiente
        Brush.BrushType brushType = gdPlayer.getBrushType();
        if (brushType == Brush.BrushType.SPHERE || brushType == Brush.brushType.SPLATTER) {
            Brush brush = new Brush(gdplayer, isRightClick, brushType, targetLocation);
            brush.brushAtLocation();
        }
    }
}