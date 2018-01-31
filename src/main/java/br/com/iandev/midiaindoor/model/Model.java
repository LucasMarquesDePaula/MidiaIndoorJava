package br.com.iandev.midiaindoor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.log4j.Logger;

/**
 *
 * @author Lucas
 * @param <T> model
 * @param <I> id
 */
public abstract class Model<T, I extends Serializable> implements Serializable {

    private final static Logger logger = Logger.getLogger(Model.class);

    public abstract I getId();

    public abstract void setId(I id);

    public abstract T parse(JSONObject jsonObject);

    public final List<T> parseList(JSONArray jsonArray) {
        List<T> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(parse(jsonArray.getJSONObject(i)));
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return list;
    }

    public final List<T> parseList(String json) {
        List<T> list = new ArrayList<>();
        try {
            list = parseList(new JSONArray(json));
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return list;
    }

    public final T parse(String jsonObject) {
        T entity = null;
        try {
            entity = parse(new JSONObject(jsonObject));
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return entity;
    }
}
