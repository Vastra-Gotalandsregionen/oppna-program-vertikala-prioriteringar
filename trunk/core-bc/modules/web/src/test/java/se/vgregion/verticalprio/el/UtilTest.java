package se.vgregion.verticalprio.el;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.beanutils.BeanMap;
import org.junit.Test;

import se.vgregion.verticalprio.controllers.EditDirective;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class UtilTest {

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#concat(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testConcat() {
        String first = "first ", second = "second";
        Assert.assertEquals(first + second, Util.concat(first, second));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#contains(java.util.Collection, java.lang.Object)}.
     */
    @Test
    public final void testContains() {
        Set<Integer> ints = new HashSet<Integer>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        Assert.assertTrue(Util.contains(ints, 3));
        Assert.assertFalse(Util.contains(ints, 5));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#isCollection(java.lang.Object)}.
     */
    @Test
    public final void testIsCollection() {
        List<String> list = new ArrayList<String>();
        Assert.assertTrue(Util.isCollection(list));
        Set<Integer> ints = new HashSet<Integer>();
        Assert.assertTrue(Util.isCollection(ints));
        Map<String, Integer> keydInts = new HashMap<String, Integer>();
        Assert.assertFalse(Util.isCollection(keydInts));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#cellToString(java.lang.Object)}.
     */
    @Test
    public final void testCellToString() {
        String string = "string";
        Util.cellToString(string);
        // TODO: Refine this for each type that this method transforms.
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#toCollection(java.lang.Object)}.
     */
    @Test
    public final void testToCollection() {
        boolean isCollection = Util.toCollection(new HashSet<String>()) instanceof Collection;
        Assert.assertTrue(isCollection);
        try {
            Util.toCollection(1);
            Assert.assertTrue(false);
        } catch (ClassCastException e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#labelFor(java.lang.Integer, java.util.Collection)}.
     */
    @Test
    public final void testLabelFor() {
        AatgaerdsKod ak = new AatgaerdsKod() {
            /**
             * @inheritDoc
             */
            @Override
            public String getLabel() {
                return "label";
            }
        };
        ak.setId(120l);
        Set<AatgaerdsKod> codes = new HashSet<AatgaerdsKod>();
        codes.add(ak);

        Assert.assertEquals("label", Util.labelFor(120, codes));
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.el.Util#canEdit(se.vgregion.verticalprio.entity.User, se.vgregion.verticalprio.controllers.EditDirective)}
     * .
     */
    @Test
    public final void testCanEdit() {
        EditDirective dir = null;
        User user = new User();
        Assert.assertFalse(Util.canEdit(user, dir));

        dir = new EditDirective();
        dir.setOverride(true);
        Assert.assertTrue(Util.canEdit(user, dir));

        user = null;
        dir.setOverride(null);
        Assert.assertFalse(Util.canEdit(user, dir));

        dir.setEditable(true);
        user = new User();
        user.setEditor(true);
        Assert.assertTrue(Util.canEdit(user, dir));

        user.setEditor(false);
        Assert.assertFalse(Util.canEdit(user, dir));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#toOptions(java.lang.Long, java.util.List)}.
     */
    @Test
    public final void testToOptions() {
        Util.toOptions(new Long(4), (List<AbstractKod>) getRaads());
    }

    private List<? extends AbstractKod> getRaads() {
        List<SektorRaad> raads = new ArrayList<SektorRaad>();
        for (long i = 0; i < 10; i++) {
            SektorRaad raad = new SektorRaad();
            raad.setId(i);
            raad.setKod("" + i);
        }
        return raads;
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.el.Util#toRaadOptions(java.lang.Long, java.util.List, java.util.List)}.
     */
    @Test
    public final void testToRaadOptions() {
        Util.toRaadOptions(4l, getRaads(), (List<SektorRaad>) getRaads());
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#toString(java.lang.Object)}.
     */
    @Test
    public final void testToStringObject() {
        List<Integer> ints = new ArrayList();
        ints.add(1);
        ints.add(2);
        Assert.assertEquals("* 1 \n* 2 \n", Util.toString(ints));

        Assert.assertEquals("Carl Marx", Util.toString("Carl Marx"));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#toStringDate(java.util.Date)}.
     */
    @Test
    public final void testToStringDate() {
        Assert.assertEquals("", Util.toStringDate(null));
        Date date = new Date(0l);
        Assert.assertEquals("1970-01-01", Util.toStringDate(date));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#toUpperCase(java.lang.String)}.
     */
    @Test
    public final void testToUpperCase() {
        Assert.assertEquals("ABC", Util.toUpperCase("AbC"));
        Assert.assertEquals("", Util.toUpperCase(null));
    }

    /**
     * Test method for {@link se.vgregion.verticalprio.el.Util#toCellText(java.lang.Object)}.
     */
    @Test
    public final void testToCellText() {
        Assert.assertEquals("", Util.toCellText(null));

        Date date = new Date(0l);
        Assert.assertEquals("1970-01-01", Util.toCellText(date));

        double d = 1.12345;
        Assert.assertEquals("1,12", Util.toCellText(d));

        SektorRaad raad = new SektorRaad();
        raad.setKortBeskrivning("kortBeskrivning");
        Assert.assertEquals("kortBeskrivning", Util.toCellText(raad));

        List<Integer> ints = new ArrayList();
        ints.add(1);
        ints.add(2);
        String expexted = "<div>1</div><div>2</div>";
        Assert.assertEquals(expexted, Util.toCellText(ints));

        AatgaerdsKod ak = new AatgaerdsKod();
        ak.setKod("ak");
        Assert.assertEquals("ak", Util.toCellText(ak));

        Assert.assertEquals("foo", Util.toCellText("foo"));
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.el.Util#isPriosDifferent(se.vgregion.verticalprio.entity.Prioriteringsobjekt, se.vgregion.verticalprio.entity.Prioriteringsobjekt)}
     * .
     */
    @Test
    public final void testIsPriosDifferent() {
        Prioriteringsobjekt one = mkPrio(), two = mkPrio();
        Assert.assertFalse(Util.isPriosDifferent(one, two));
        one.setKommentar("Foo");
        Assert.assertTrue(Util.isPriosDifferent(one, two));
    }

    private Prioriteringsobjekt mkPrio() {
        Prioriteringsobjekt result = new Prioriteringsobjekt();

        BeanMap bm = new BeanMap(result);
        for (Object key : bm.keySet()) {
            String name = (String) key;
            if (bm.getWriteMethod(name) == null) {
                continue;
            }
            if (String.class.isAssignableFrom(bm.getType(name))) {
                bm.put(name, name);
            }
            if (Integer.class.isAssignableFrom(bm.getType(name))) {
                bm.put(name, 1);
            }
        }
        AatgaerdsKod ak = new AatgaerdsKod();
        ak.setId(1l);
        result.getAatgaerdskoder().add(ak);

        DiagnosKod dk = new DiagnosKod();
        dk.setId(1l);
        result.getDiagnoser().add(dk);

        AtcKod atc = new AtcKod();
        atc.setId(1l);
        result.getAtcKoder().add(atc);

        return result;
    }
}
