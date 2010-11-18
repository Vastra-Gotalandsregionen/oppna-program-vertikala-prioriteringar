package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Prio;

public class ControllerBase {

    private Map<String, String> prioPropertyTexts;

    private List<Column> columns;

    protected String columnTextsPropertiesFileName = "/column-texts.properties";

    /**
     * Getter for the prioPropertyTexts property. It initializes the map with the value in the property file
     * pointed to by the columnTextsPropertiesFileName attribute (/column-texts.properties).
     * 
     * @return Names of the {@link Prio} attributes mapped to their labels.
     */
    protected Map<String, String> getPrioPropertyTexts() {
        if (prioPropertyTexts == null) {
            try {
                Properties namesTexts = new Properties();
                namesTexts.load(getClass().getResourceAsStream(columnTextsPropertiesFileName));
                Map<String, String> map = new HashMap<String, String>();

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
        if (columns == null) {
            Map<String, String> ppt = getPrioPropertyTexts();
            int count = 0;
            List<Column> result = new ArrayList<Column>(ppt.size());
            for (String key : ppt.keySet()) {
                Column column = new Column();
                column.setName(key);
                column.setLabel(ppt.get(key));
                column.setId(count++);
                result.add(column);
            }
            columns = result;
        }
        return columns;
    }

}
