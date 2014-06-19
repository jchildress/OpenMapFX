package org.lodgon.openmapfx.service.miataru;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;
import org.lodgon.openmapfx.core.SettingsService;

/**
 *
 * @author joeri
 */
public class SettingChangeListener<T> implements ChangeListener<T> {

    private final String settingKey;
    private StringConverter converter;

    public SettingChangeListener(String settingKey) {
        this.settingKey = settingKey;
    }

    public SettingChangeListener(String settingKey, StringConverter converter) {
        this.settingKey = settingKey;
        this.converter = converter;
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        SettingsService.getInstance().storeSetting(settingKey, converter == null ? newValue.toString() : converter.toString(newValue));
    }

}
