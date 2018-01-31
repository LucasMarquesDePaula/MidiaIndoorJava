package br.com.iandev.midiaindoor.settings;

import java.util.prefs.Preferences;

public class Settings {

    private final Preferences preferences;

    protected Settings(Class<?> clazz) {
        preferences = Preferences.userRoot().node(clazz.getCanonicalName());
    }

    protected String getValue(String key) {
        return getValue(key, "");
    }

    protected String getValue(String key, String defaultValue) {
        return preferences.get(key, defaultValue);
    }

    Settings setValue(String key, String value) {
        if (value == null || value.isEmpty()) {
            preferences.remove(key);
        } else {
            preferences.put(key, value);
        }
        return this;
    }

}
