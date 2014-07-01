/**
 * Copyright (c) 2014, OpenMapFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of LodgON, the website lodgon.com, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL LODGON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lodgon.openmapfx.android;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxports.android.FXActivity;
import org.lodgon.openmapfx.core.SettingsProvider;

/**
 *
 * @author joeri
 */
public class AndroidSettingsProvider implements SettingsProvider {

    private static final Logger LOG = Logger.getLogger(AndroidSettingsProvider.class.getName());

    private final SharedPreferences settings;

    public AndroidSettingsProvider() {
        settings = FXActivity.getInstance().getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Override
    public void storeSetting(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();

        LOG.log(Level.INFO, "Updated setting {0} = \"{1}\"", new Object[]{key,value});
    }

    @Override
    public void removeSetting(String key) {
        String value = getSetting(key);

        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();

        LOG.log(Level.INFO, "Removed setting {0} = \"{1}\"", new Object[]{key,value});
    }

    @Override
    public String getSetting(String key) {
        return settings.getString(key, null);
    }

}
