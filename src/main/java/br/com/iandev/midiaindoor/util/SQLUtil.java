package br.com.iandev.midiaindoor.util;

import java.util.Date;
import java.util.TimeZone;

import br.com.iandev.midiaindoor.model.Model;
import java.io.Serializable;
import java.sql.ResultSet;

/**
 * Created by Lucas on 21/03/2017.
 */
public class SQLUtil {

    public static Date getDate(ResultSet rs, String field) {
        Long time = null;
        try {
            time = rs.getLong(field);
        } catch (Exception ignored) {
        }
        return time != null ? new Date(time) : null;
    }

    public static Long getDate(Date date) {
        return date != null ? date.getTime() : null;
    }

    public static Long getLong(ResultSet rs, String field) {
        Long value = null;
        try {
            value = rs.getLong(field);
        } catch (Exception ignored) {
        }
        return value;
    }

    public static Long getLong(Long value) {
        return value;
    }

    public static Integer getInteger(ResultSet rs, String field) {
        Integer value = null;
        try {
            value = rs.getInt(field);
        } catch (Exception ignored) {
        }
        return value;
    }

    public static String getString(ResultSet rs, String field) {
        String value = null;
        try {
            value = rs.getString(field);
        } catch (Exception ignored) {
        }
        return value;
    }

    public static String getCharacter(Character character) {
        if (character == null) {
            return null;
        }
        return String.valueOf(character);
    }

    public static Character getCharacter(ResultSet rs, String field) {
        Character value = null;
        try {
            value = getString(rs, field).charAt(0);
        } catch (Exception ignored) {

        }
        return value;
    }

    public static Serializable getModel(Model model) {
        return model != null ? model.getId() : null;
    }

    public static String getString(String value) {
        return value;
    }

    public static Integer getInteger(Integer value) {
        return value;
    }

    public static String getTimeZone(TimeZone timeZone) {
        return timeZone != null ? timeZone.getID() : null;
    }

    public static TimeZone getTimeZone(ResultSet rs, String field) {
        String timeZone = getString(rs, field);
        return timeZone != null ? TimeZone.getTimeZone(timeZone) : null;
    }

}
