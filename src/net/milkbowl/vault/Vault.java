/* This file is part of Vault.

    Vault is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Vault is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Vault.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.milkbowl.vault;

import java.util.Collection;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.milkbowl.vault.permission.SuperPerms;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Vault - An abstraction library for Bukkit providing unified APIs for
 * Economy, Permission, and Chat systems.
 */
@SuppressWarnings("deprecation")
public class Vault extends JavaPlugin {

    private ServicesManager sm;

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {
        sm = getServer().getServicesManager();

        // Register SuperPerms as fallback permission system
        Permission superPerms = new SuperPerms(this);
        sm.register(Permission.class, superPerms, this, ServicePriority.Lowest);
        getLogger().info("[Permission] SuperPerms loaded as backup permission system.");

        // Initialize bStats metrics
        Metrics metrics = new Metrics(this, 887);
        registerMetrics(metrics);

        getLogger().info("Enabled Version " + getDescription().getVersion());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!sender.hasPermission("vault.admin")) {
            sender.sendMessage("You do not have permission to use that command!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("vault-info")) {
            infoCommand(sender);
            return true;
        } else if (command.getName().equalsIgnoreCase("vault-convert")) {
            convertCommand(sender, args);
            return true;
        }

        sender.sendMessage("Vault Commands:");
        sender.sendMessage("  /vault-info - Displays information about Vault");
        sender.sendMessage("  /vault-convert [economy1] [economy2] - Converts economy data");
        return true;
    }

    private void convertCommand(CommandSender sender, String[] args) {
        Collection<RegisteredServiceProvider<Economy>> econs = sm.getRegistrations(Economy.class);
        if (econs == null || econs.size() < 2) {
            sender.sendMessage("You must have at least 2 economies loaded to convert.");
            return;
        }
        if (args.length != 2) {
            sender.sendMessage("Usage: /vault-convert [economy1] [economy2]");
            return;
        }

        Economy econ1 = null;
        Economy econ2 = null;
        StringBuilder economies = new StringBuilder();

        for (RegisteredServiceProvider<Economy> econ : econs) {
            String econName = econ.getProvider().getName().replace(" ", "");
            if (econName.equalsIgnoreCase(args[0])) {
                econ1 = econ.getProvider();
            } else if (econName.equalsIgnoreCase(args[1])) {
                econ2 = econ.getProvider();
            }
            if (economies.length() > 0) {
                economies.append(", ");
            }
            economies.append(econName);
        }

        if (econ1 == null) {
            sender.sendMessage("Could not find " + args[0] + " loaded on the server.");
            sender.sendMessage("Valid economies are: " + economies);
            return;
        }
        if (econ2 == null) {
            sender.sendMessage("Could not find " + args[1] + " loaded on the server.");
            sender.sendMessage("Valid economies are: " + economies);
            return;
        }

        sender.sendMessage("Converting economy data. This may cause lag...");
        int converted = 0;
        for (OfflinePlayer op : Bukkit.getServer().getOfflinePlayers()) {
            if (econ1.hasAccount(op)) {
                if (!econ2.hasAccount(op)) {
                    econ2.createPlayerAccount(op);
                }
                double diff = econ1.getBalance(op) - econ2.getBalance(op);
                if (diff > 0) {
                    econ2.depositPlayer(op, diff);
                    converted++;
                } else if (diff < 0) {
                    econ2.withdrawPlayer(op, -diff);
                    converted++;
                }
            }
        }
        sender.sendMessage("Conversion complete. " + converted + " accounts transferred.");
    }

    private void infoCommand(CommandSender sender) {
        String registeredEcons = getRegisteredServices(Economy.class);
        String registeredPerms = getRegisteredServices(Permission.class);
        String registeredChats = getRegisteredServices(Chat.class);

        Economy econ = getProvider(Economy.class);
        Permission perm = getProvider(Permission.class);
        Chat chat = getProvider(Chat.class);

        sender.sendMessage("[Vault] Version " + getDescription().getVersion());
        sender.sendMessage("[Vault] Economy: " + (econ == null ? "None" : econ.getName()) + " [" + registeredEcons + "]");
        sender.sendMessage("[Vault] Permission: " + (perm == null ? "None" : perm.getName()) + " [" + registeredPerms + "]");
        sender.sendMessage("[Vault] Chat: " + (chat == null ? "None" : chat.getName()) + " [" + registeredChats + "]");
    }

    private <T> String getRegisteredServices(Class<T> clazz) {
        Collection<RegisteredServiceProvider<T>> providers = sm.getRegistrations(clazz);
        if (providers.isEmpty()) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        for (RegisteredServiceProvider<T> provider : providers) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            if (provider.getProvider() instanceof Economy) {
                sb.append(((Economy) provider.getProvider()).getName());
            } else if (provider.getProvider() instanceof Permission) {
                sb.append(((Permission) provider.getProvider()).getName());
            } else if (provider.getProvider() instanceof Chat) {
                sb.append(((Chat) provider.getProvider()).getName());
            }
        }
        return sb.toString();
    }

    private <T> T getProvider(Class<T> clazz) {
        RegisteredServiceProvider<T> rsp = sm.getRegistration(clazz);
        return rsp != null ? rsp.getProvider() : null;
    }

    private void registerMetrics(Metrics metrics) {
        metrics.addCustomChart(new SimplePie("economy", () -> {
            Economy econ = getProvider(Economy.class);
            return econ != null ? econ.getName() : "No Economy";
        }));

        metrics.addCustomChart(new SimplePie("permission", () -> {
            Permission perm = getProvider(Permission.class);
            return perm != null ? perm.getName() : "No Permission";
        }));

        metrics.addCustomChart(new SimplePie("chat", () -> {
            Chat chat = getProvider(Chat.class);
            return chat != null ? chat.getName() : "No Chat";
        }));
    }
}
