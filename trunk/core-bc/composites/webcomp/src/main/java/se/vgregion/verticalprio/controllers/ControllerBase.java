package se.vgregion.verticalprio.controllers;

import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.PrioRepository;

public class ControllerBase {

    @Resource(name = "prioRepository")
    protected PrioRepository prioRepository;

    private SortedMap<String, String> prioPropertyTexts;

    private List<Column> columns = Prioriteringsobjekt.getDefaultColumns();

    protected String columnTextsPropertiesFileName = "/column-texts.properties";

    /**
     * Getter for the prioPropertyTexts property. It initializes the map with the value in the property file
     * pointed to by the columnTextsPropertiesFileName attribute (/column-texts.properties).
     * 
     * @return Names of the {@link Prioriteringsobjekt} attributes mapped to their labels.
     */
    protected SortedMap<String, String> getPrioPropertyTexts() {
        if (prioPropertyTexts == null) {
            try {
                Properties namesTexts = new Properties();
                namesTexts.load(getClass().getResourceAsStream(columnTextsPropertiesFileName));
                SortedMap<String, String> map = new TreeMap<String, String>();

                for (Object key : namesTexts.keySet()) {
                    String sk = (String) key;
                    map.put(sk, namesTexts.getProperty(sk));
                }

                prioPropertyTexts = map;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return prioPropertyTexts;
    }

    /**
     * Getter för columns attribute. Initializes the list if not already ready.
     * 
     * @return List of columns described in the property file /column-texts.properties.
     */
    public List<Column> getColumns() {

        return columns;

        // if (columns == null) {
        // Map<String, String> ppt = getPrioPropertyTexts();
        // int count = 0;
        // List<Column> result = new ArrayList<Column>(ppt.size());
        // for (String key : new TreeSet<String>(ppt.keySet())) {
        // Column column = new Column();
        // column.setName(key.substring(4));
        // column.setLabel(ppt.get(key));
        // column.setId(count++);
        // result.add(column);
        // }
        // columns = result;
        // }
        // return columns;
    }

    protected <T> T getOrCreateSessionObj(HttpSession session, String name, Class<T> clazz) {
        try {
            T result = (T) session.getAttribute(name);

            if (result == null) {
                result = clazz.newInstance();
                session.setAttribute(name, result);
            }

            return result;
        } catch (InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }

}
