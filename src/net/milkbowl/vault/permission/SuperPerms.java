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
package net.milkbowl.vault.permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * SuperPerms - Fallback permission implementation using Bukkit's native permission system.
 * Provides basic permission checking via player.hasPermission().
 * Does not support group management or permission modification.
 */
@SuppressWarnings("deprecation")
public class SuperPerms extends Permission {

    private static final String NAME = "SuperPerms";

    public SuperPerms(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return true;
    }

    @Override
    public boolean hasGroupSupport() {
        return false;
    }

    @Override
    public boolean playerHas(String world, String playerName, String permission) {
        Player player = Bukkit.getPlayerExact(playerName);
        return player != null && player.hasPermission(permission);
    }

    @Override
    public boolean playerAdd(String world, String player, String permission) {
        return false;
    }

    @Override
    public boolean playerRemove(String world, String player, String permission) {
        return false;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {
        return playerHas(world, player, "groups." + group);
    }

    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public String getPrimaryGroup(String world, String player) {
        throw new UnsupportedOperationException(NAME + " does not support groups.");
    }

    @Override
    public String[] getGroups() {
        return new String[0];
    }
}
