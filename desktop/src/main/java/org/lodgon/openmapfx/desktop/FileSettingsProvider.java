/**
 * Copyright (c) 2014, OpenMapFX All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of LodgON, the website lodgon.com,
 * nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL LODGON BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lodgon.openmapfx.desktop;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lodgon.openmapfx.core.SettingsProvider;

/**
 *
 * @author Joeri
 */
public class FileSettingsProvider implements SettingsProvider {

    private static final Logger LOG = Logger.getLogger(FileSettingsProvider.class.getName());

    private final Properties settings = new Properties();

    public FileSettingsProvider() {
        Path settingsFile = getOrCreateSettingsFile();

        try {
            settings.load(Files.newInputStream(settingsFile, StandardOpenOption.READ));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not read settings.properties file.", ex);
        }

        LOG.log(Level.CONFIG, "The following settings were successfully read from file: {0}", settings);
    }

    @Override
    public void storeSetting(String key, String value) {
        settings.setProperty(key, value);
        storeSettings();

        LOG.log(Level.INFO, "Updated setting {0} = \"{1}\"", new Object[]{key,value});
    }

    @Override
    public void removeSetting(String key) {
        Object value = settings.remove(key);
        storeSettings();

        LOG.log(Level.INFO, "Removed setting {0} = \"{1}\"", new Object[]{key,value});
    }

    @Override
    public String getSetting(String key) {
        return settings.getProperty(key);
    }

    private void storeSettings() {
        Path settingsFile = getOrCreateSettingsFile();
        try (BufferedWriter writer = Files.newBufferedWriter(settingsFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            settings.store(writer, null);

            LOG.log(Level.FINE, "The settings were successfully written to file.");
        } catch (IOException ex) {
            LOG.log(Level.CONFIG, "Read settings.properties: {0}", settings);
        }
    }

    private Path getOrCreateSettingsFile() {
        String userHome = System.getProperty("user.home");
        Path projectDirectory = Paths.get(userHome, ".openmapfx");

        try {
            Files.createDirectories(projectDirectory);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not create project directory.", ex);
        }

        Path settingsFile = Paths.get(projectDirectory.toString(), "settings.properties");
        if (!Files.exists(settingsFile)) {
            try {
                Files.createFile(settingsFile);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Could not create settings.properties file.", ex);
            }
        }

        LOG.log(Level.INFO, "settings.properties location is {0}", settingsFile);

        return settingsFile;
    }
}
