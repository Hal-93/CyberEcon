package jp.cyberhub.cyberecon;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconCommand implements CommandExecutor {
    private final EconManager econManager;

    public EconCommand(EconManager econManager) {
        this.econManager = econManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("balance")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                double balance = econManager.getBalance(player.getUniqueId().toString());
                player.sendMessage("Your balance: $" + balance);
            }
            return true;
        }

        if (label.equalsIgnoreCase("addmoney")) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    double amount;
                    try {
                        amount = Double.parseDouble(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid amount!");
                        return true;
                    }

                    econManager.addBalance(target.getUniqueId().toString(), amount);
                    sender.sendMessage("Added $" + amount + " to " + target.getName());
                } else {
                    sender.sendMessage("Player not found!");
                }
            }
            return true;
        }

        return false;
    }
}