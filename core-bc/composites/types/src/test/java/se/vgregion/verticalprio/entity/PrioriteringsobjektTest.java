package se.vgregion.verticalprio.entity;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import net.sf.cglib.beans.BeanMap;

import org.junit.Test;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class PrioriteringsobjektTest {

    /**
     * Test method for {@link se.vgregion.verticalprio.entity.Prioriteringsobjekt#getColumns()}.
     */
    @Test
    public final void getColumns() {
        Prioriteringsobjekt prio = new Prioriteringsobjekt();
        BeanMap bm = BeanMap.create(prio);
        Set<String> propertyKeys = new HashSet<String>(bm.keySet());
        Set<String> columnKeys = new HashSet<String>();

        for (Column column : Prioriteringsobjekt.getColumns()) {
            if (!propertyKeys.contains(column.getName())) {
                Assert.fail("The key " + column.getName()
                        + " was present in the columns list but not in the Prioriteringsobjekt class itself.");
            }
            Assert.assertNotNull(column.getLabel());
            Assert.assertFalse("The Column with name " + column.getName() + " does not have a label.",
                    "".equals(column.getLabel()));
            columnKeys.add(column.getName());
        }

        propertyKeys.removeAll(columnKeys);
        for (String key : propertyKeys) {
            System.out.println("Property " + key
                    + " in Prioriteringsobjekt did not have a corresponding Column object "
                    + "(might be perfectly allright). See " + getClass().getName()
                    + ".getColumns() for this warning.");
        }
    }
}
