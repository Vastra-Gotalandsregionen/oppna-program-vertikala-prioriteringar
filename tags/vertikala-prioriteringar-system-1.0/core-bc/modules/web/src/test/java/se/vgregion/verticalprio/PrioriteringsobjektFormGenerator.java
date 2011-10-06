package se.vgregion.verticalprio;

import java.util.List;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class PrioriteringsobjektFormGenerator {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Prioriteringsobjekt prioriteringsobjekt = new Prioriteringsobjekt();
        BeanMap bm = new BeanMap(prioriteringsobjekt);
        StringBuilder sb = new StringBuilder();

        for (Object key : bm.keySet()) {
            if (List.class.isAssignableFrom(bm.getType(key.toString()))) {
                sb.append("private List<String> " + key + ";\n");
            } else {
                sb.append("private String " + key + ";\n");
            }
        }

        System.out.println(sb);
    }
}
