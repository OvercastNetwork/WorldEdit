/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.sponge;

import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.worldedit.util.YAMLConfiguration;

import java.io.File;

/**
 * YAMLConfiguration but with setting for no op permissions and plugin root data folder
 */
public class SpongeConfiguration extends YAMLConfiguration {
    private final WorldEditPlugin plugin;

    public SpongeConfiguration(YAMLProcessor config, WorldEditPlugin plugin) {
        super(config, plugin.getLogger());
        this.plugin = plugin;
    }

    @Override
    public void load() {
        super.load();
        migrateLegacyFolders();
    }

    private void migrateLegacyFolders() {
        migrate(scriptsDir, "craftscripts");
        migrate(saveDir, "schematics");
        migrate("drawings", "draw.js images");
    }

    private void migrate(String file, String name) {
        File fromDir = new File(".", file);
        File toDir = new File(getWorkingDirectory(), file);
        if (fromDir.exists() & !toDir.exists()) {
            if (fromDir.renameTo(toDir)) {
                plugin.getLogger().info("Migrated " + name + " folder '" + file +
                        "' from game root to plugin data folder.");
            } else {
                plugin.getLogger().warning("Error while migrating " + name + " folder!");
            }
        }
    }

    @Override
    public File getWorkingDirectory() {
        return plugin.getConfigDirectory();
    }
}