# Vault-Updated - Abstraction Library for Bukkit

## About This Fork
This is an updated version of the original [Vault by MilkBowl](https://github.com/MilkBowl/Vault). The original Vault hasn't been maintained and contained hooks for many abandoned plugins. This version:

* Merges VaultAPI directly into Vault — one plugin, one download
* Removes all dead/abandoned plugin hooks
* Adds full support for modern plugins (LuckPerms, EssentialsX)
* Updated for Minecraft 1.20 - 1.21+
* Works on both Spigot and Paper servers
* Cleaner, lighter codebase

## For Developers
VaultAPI is now included in Vault. No separate dependency needed.

Use the same API classes you always have:
* `net.milkbowl.vault.economy.Economy`
* `net.milkbowl.vault.permission.Permission`
* `net.milkbowl.vault.chat.Chat`

Just add Vault as a dependency in your project.

## Installing
Copy "Vault-2.0.0.jar" to your `plugins` folder and restart your server. That's it!

## Why Vault?
Vault provides a bridge between plugins that need economy, permissions, or chat features and the plugins that provide those features. Instead of supporting every economy plugin individually, developers just support Vault.

**Benefits:**
* No need to include Vault source code in your plugin
* One API for multiple backend plugins
* Freedom to choose which economy/permission plugin you use

## Commands
* `/vault-info` - Shows Vault version and hooked plugins

## Permissions
* `vault.admin` - Access to Vault admin commands and update notices

## Supported Plugins

### Permissions
* LuckPerms (https://luckperms.net)
* SuperPerms (Bukkit's built-in fallback)

### Economy
* EssentialsX (https://essentialsx.net)

### Chat
* LuckPerms (https://luckperms.net)

Other plugins may have built-in Vault support. Check with the plugin developer.

## Removed Plugins
The following abandoned plugins were removed from this version:
* **Permissions:** bPermissions, DroxPerms, Group Manager, OverPermissions, Permissions 3, PermissionsBukkit, PermissionsEx (PEX), Privileges, rscPermissions, SimplyPerms, TotalPermissions, XPerms, zPermissions
* **Economy:** iConomy, BOSEconomy, 3Co, MultiCurrency, MineConomy, eWallet, EconXP, CurrencyCore, CraftConomy, AEco, Gringotts
* **Chat:** iChat, mChat, mChatSuite, Herochat, bPermissions chat, PEX chat, DroxPerms chat

## Credits
**Original Vault by:**
* Sleaker (Morgan Humes)
* cereal
* mung3r
* Kainzo
* MilkBowl Team

**Updated by:**
* roydogman

## License
Copyright (C) 2011-2018 Morgan Humes <morgan@lanaddict.com>
Updated 2024 by roydogman

Vault is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Vault is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
with Vault. If not, see <http://www.gnu.org/licenses/>.

## Links
* **This Fork:** https://github.com/roydogman/Vault-Updated
* **Original Vault:** https://github.com/MilkBowl/Vault
* **Original VaultAPI:** https://github.com/MilkBowl/VaultAPI