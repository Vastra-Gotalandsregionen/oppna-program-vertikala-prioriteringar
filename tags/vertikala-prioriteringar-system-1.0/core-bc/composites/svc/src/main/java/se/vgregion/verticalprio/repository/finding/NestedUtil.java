package se.vgregion.verticalprio.repository.finding;

import se.vgregion.verticalprio.entity.AbstractKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedUtil {

    public static String toString(HaveNestedEntities<? extends AbstractKod> ak) {
        StringBuilder sb = new StringBuilder();
        for (AbstractKod sr : ak.content()) {
            if (sr.getKod() != null) {
                sb.append(sr.getKod() + ", ");
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString().trim();
    }

}
