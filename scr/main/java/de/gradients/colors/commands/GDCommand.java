package de.gradients.colors.commands;

import de.gradients.colors.nextColors;
import de.gradients.colors.utils.Undo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GDCommand implements CommandExecutor {

    private static final String NO_PERMISSION_MESSAGE = gradients.getPrefix() + " §cYou do not have permission to perform this command!";
    private static final String USAGE_MESSAGE = gradients.getPrefix() + " §9Usage§8: §f/gd §7<§fundo§7>";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(nextColors.getPrefix() + " §cOnly players can use this command!");
            return false;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("gd")) {
            if (!player.hasPermission("gradients.command.gd")) {
                player.sendMessage(NO_PERMISSION_MESSAGE);
                return false;
            }

            if (args.length == 0) {
                player.sendMessage(USAGE_MESSAGE);
                return false;
            }

            if (args[0].equalsIgnoreCase("undo")) {
                handleUndoCommand(player, args);
            } else {
                player.sendMessage(USAGE_MESSAGE);
            }

        } else if (command.getName().equalsIgnoreCase("gdt")) {
            if (!player.hasPermission("gradients.command.gdt")) {
                player.sendMessage(NO_PERMISSION_MESSAGE);
                return false;
            }

            handleUndoCommand(player, args);
        }

        return true;
    }

    private void handleUndoCommand(Player player, String[] args) {
        if (args.length == 1) {
            if (Undo.undoPlayers.containsKey(player) && !Undo.undoPlayers.get(player).isEmpty()) {
                Undo undo = Undo.undoPlayers.get(player).get(Undo.undoPlayers.get(player).size() - 1);
                undo.performUndo();
                Undo.undoPlayers.get(player).remove(Undo.undoPlayers.get(player).size() - 1);

                player.sendMessage(nextColors.getPrefix() + " §9Undo performed!");
            } else {
                player.sendMessage(nextColors.getPrefix() + " §cThere is nothing left to undo!");
            }
        } else if (args.length == 2) {
            try {
                int undoInt = Integer.parseInt(args[1]);
                if (undoInt < 1 || undoInt > 100) {
                    player.sendMessage(nextColors.getPrefix() + " §9Usage§8: §f/gd undo §7<§f1§8-§f100§7>");
                    return;
                }

                if (Undo.undoPlayers.containsKey(player) && !Undo.undoPlayers.get(player).isEmpty()) {
                    player.sendMessage(nextColors.getPrefix() + " §9Performing " + undoInt + " undo actions...");
                    for (int i = 0; i < undoInt; i++) {
                        if (Undo.undoPlayers.get(player).isEmpty()) {
                            player.sendMessage(nextColors.getPrefix() + " §cThere is nothing left to undo!");
                            break;
                        }

                        Undo undo = Undo.undoPlayers.get(player).get(Undo.undoPlayers.get(player).size() - 1);
                        undo.performUndo();
                        Undo.undoPlayers.get(player).remove(Undo.undoPlayers.get(player).size() - 1);
                    }
                } else {
                    player.sendMessage(nextColors.getPrefix() + " §cThere is nothing left to undo!");
                }
            } catch (NumberFormatException e) {
                player.sendMessage(nextColors.getPrefix() + " §cInvalid number! Please use a valid integer.");
            }
        } else {
            player.sendMessage(nextColors.getPrefix() + " §9Usage§8: §f/gd undo §7<§f1§8-§f100§7>");
        }
    }
}