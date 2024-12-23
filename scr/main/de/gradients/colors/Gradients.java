package de.gradients.colors;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;
import de.gradients.colors.commands.GDCommand;
import de.gradients.colors.listeners.OtherListeners;
import de.gradients.colors.listeners.click.CustomGradientInventoryClickListeners;
import de.gradients.colors.listeners.click.MainInventoryClickListeners;
import de.gradients.colors.listeners.interact.DefaultInteractListener;
import de.gradients.colors.utils.ColorChains;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Gradients extends JavaPlugin {

    /* Player config version */
    public static int configVersion = 1;

    /* APIs */
    public static PlotSquared plotSquared;
    public static boolean plotSquaredEnabled;

    /* Enable */
    @Override
    public void onEnable() {

        registerListeners();
        registerCommands();

        /* Loading PlotSquared API */
        if (loadPlotAPI()) {
            plotSquaredEnabled = true;
        } else {
            plotSquaredEnabled = false;
        }

        /* Creates all ArrayLists for the color-chains */
        ColorChains.setBlockLists();

        /* Console enable message */
        Bukkit.broadcastMessage(getPrefix() + " §cPlease rejoin to load all features of gradients!");

        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §3The plugin is now §aEnabled");
        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §3Coded by: §edexstdev24, nextround");
        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §3Version: §e" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §3Thank you for downloading gradients remember that the original plugin is on spigotmc!");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §c(c) Copyright Nicole S. 2020/21");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §3Plugin created by Planet Builders!");
    }

    /* Register Commands */
    public void registerCommands() {
        getCommand("gd").setExecutor(new NCCommand());
        getCommand("gdu").setExecutor(new NCCommand());
    }

    /* Register all listeners */
    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new OtherListeners(), this);
        Bukkit.getPluginManager().registerEvents(new MainInventoryClickListeners(), this);
        Bukkit.getPluginManager().registerEvents(new CustomGradientInventoryClickListeners(), this);
        Bukkit.getPluginManager().registerEvents(new DefaultInteractListener(), this);
    }

    /* Get the plugin message prefix */
    public static String getPrefix() {
        return "§8[§3Gradients§8]";
    }

    /* Loads PlotAPI if PlotSquared is enabled on server */
    public boolean loadPlotAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") == null || 
            !Bukkit.getPluginManager().getPlugin("PlotSquared").isEnabled()) {
            return false;
        }

        plotSquared = PlotSquared.get();

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(getPrefix() + " §9PlotSquared loaded successfully!");
        Bukkit.getConsoleSender().sendMessage(" ");

        return true;
    }

    /* If PlotSquared is enabled: checks if block can be placed (owner, trusted, or permission) */
    public static boolean canBlockBePlaced(Player player, org.bukkit.Location loc) {
        if (plotSquaredEnabled) {

            // Verify if the location is outside of any plot area
            if (!PlotSquared.get().getPlotAreaManager().hasPlotArea(loc.getWorld().getName()) &&
                player.hasPermission("gra.coloroutside")) {
                return true;
            }

            // Convert Bukkit Location to PlotSquared Location
            Location plotSquaredLocation = Location.at(
                    loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
            );

            Plot plot = plotSquaredLocation.getOwnedPlot();

            if (plot == null) {
                return false;
            }

            PlotPlayer<?> plotPlayer = PlotPlayer.wrap(player);

            // Check if the player is the owner or trusted in the plot
            if (plot.getOwners().contains(plotPlayer.getUUID()) ||
                plot.getTrusted().contains(plotPlayer.getUUID())) {
                return true;
            }
        }
        return false;
    }
}