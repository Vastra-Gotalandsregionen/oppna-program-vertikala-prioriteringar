package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.PrioRepository;

public class ControllerBase {

    @Resource(name = "prioRepository")
    protected PrioRepository prioRepository;

    private SortedMap<String, String> prioPropertyTexts;

    private List<Column> columns;

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
        if (columns == null) {
            Map<String, String> ppt = getPrioPropertyTexts();
            int count = 0;
            List<Column> result = new ArrayList<Column>(ppt.size());
            for (String key : new TreeSet<String>(ppt.keySet())) {
                Column column = new Column();
                column.setName(key.substring(4));
                column.setLabel(ppt.get(key));
                column.setId(count++);
                result.add(column);
            }
            columns = result;
        }
        return columns;
    }

    @ModelAttribute("rows")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Prioriteringsobjekt> result(HttpSession session) {
        List<Prioriteringsobjekt> prios = new ArrayList<Prioriteringsobjekt>(prioRepository.findAll());

        for (Prioriteringsobjekt prio : prios) {
            BeanMap bm = new BeanMap(prio);
            Map<String, Object> values = new HashMap<String, Object>(bm);
            // Completely insane... but has to be done because otherwise
            // a lack of transaction will occur when rendering the referred child objects.
            // TODO: don't use lazy loading on collection or objects inside the Prioriteringsobjekt class.
            for (String key : values.keySet()) {
                Object value = values.get(key);
                if (value instanceof Collection) {
                    Collection<?> collection = (Collection<?>) value;
                    new ArrayList<Object>(collection);
                }
            }
        }

        return prios;
    }

}
