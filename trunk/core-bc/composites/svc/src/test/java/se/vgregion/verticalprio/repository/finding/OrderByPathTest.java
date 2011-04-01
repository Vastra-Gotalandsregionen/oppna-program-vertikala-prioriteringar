package se.vgregion.verticalprio.repository.finding;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class OrderByPathTest {

    OrderByPath obp;
    String testPath = "prop1/prop2/prop3/attribute";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        obp = new OrderByPath();
        obp.setPath(testPath);
    }

    // @Test
    // public void toJpqlJoinPart() {
    // List<String> result = obp.toJpqlJoinPart("startAlias");
    // System.out.println(result);
    // Assert.assertEquals("startAlias.prop1 ob0c0 startAlias.prop2 ob0c1 startAlias.prop3 ob0c2", result.get(0));
    // }

    @Test
    public void toJpqlSelectPart() {
        String result = obp.toJpqlSelectPart("alias");
        System.out.println(result);
        Assert.assertTrue(result.endsWith(".attribute"));
    }

    @Test
    public void toJpqlOrderByPart() {
        String result = obp.toJpqlOrderByPart("alias");
        System.out.println(result);
        Assert.assertTrue(result.endsWith(".attribute asc"));

        obp.setAscending(false);
        result = obp.toJpqlOrderByPart("alias");
        Assert.assertTrue(result.endsWith(".attribute desc"));
    }

}
