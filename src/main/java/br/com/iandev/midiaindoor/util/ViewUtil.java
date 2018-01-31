package br.com.iandev.midiaindoor.util;

import java.util.Date;
import java.util.TimeZone;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter.Change;

public class ViewUtil {

    public static String getString(Object value) {
        return value != null ? value.toString() : "";
    }

    public static String getString(TimeZone value) {
        return value != null ? value.getDisplayName() : "";
    }
    
    public static String getString(Date value) {
        return getString(value, null);
    }

    public static String getString(Date value, TimeZone timeZone) {
        return value != null ? DateUtil.fullFormat(value, timeZone != null ? timeZone : TimeZone.getDefault()) : "";
    }

    public static String getInterval(Long value) {
        return value != null ? IntervalUtil.format(value) : "";
    }

    public static class Filter {
        public static final UnaryOperator<Change> INTEGER = (Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };
    }
}
