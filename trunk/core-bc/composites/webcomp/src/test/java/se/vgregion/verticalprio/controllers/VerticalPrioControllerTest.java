package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Prio;

public class VerticalPrioControllerTest {

    private VerticalPrioController vpc;

    private MockHttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() {
        vpc = new VerticalPrioController() {
            @Override
            public java.util.Map<String, String> getPrioPropertyTexts() {
                columnTextsPropertiesFileName = "/column-texts-test.properties";
                return super.getPrioPropertyTexts();
            }
        };
        request = new MockHttpServletRequest();
        session = request.getSession();
    }

    @Test
    public void main() {
        String defaultResult = vpc.main(session);
        Assert.assertEquals("main", defaultResult);
    }

    @Test
    public void result() {
        vpc = new VerticalPrioController();
        List<Prio> prios = vpc.result(session);
        Assert.assertNotSame(0, prios.size());
    }

    @Test
    public void confColumns() {
        List<String> visibleColumns = new ArrayList<String>();
        visibleColumns.addAll(Arrays.asList(new String[] { "col1", "col2", "col3" }));
        visibleColumns.addAll(Arrays.asList(new String[] { "col4", "col5", "col6" }));

        List<String> hiddenColumns = new ArrayList<String>();

        vpc.initConfColumns(session);

        ConfColumnsForm columnForm = (ConfColumnsForm) session.getAttribute("confCols");
        Assert.assertEquals(0, columnForm.getHiddenColumns().size());
        Assert.assertEquals(6, columnForm.getVisibleColumns().size());

        String result = vpc.confColumns(session, "hide", visibleColumns, hiddenColumns);
        columnForm = (ConfColumnsForm) session.getAttribute("confCols");
        Assert.assertEquals("conf-columns", result);

        Assert.assertEquals(6, columnForm.getHiddenColumns().size());
        Assert.assertEquals(0, columnForm.getVisibleColumns().size());

        result = vpc.confColumns(session, "show", hiddenColumns, visibleColumns);
        Assert.assertEquals("conf-columns", result);
        Assert.assertEquals(0, columnForm.getHiddenColumns().size());
        Assert.assertEquals(6, columnForm.getVisibleColumns().size());

        result = vpc.confColumns(session, "save", visibleColumns, hiddenColumns);
        Assert.assertEquals("main", result);
        for (Column column : columnForm.getVisibleColumns()) {
            Assert.assertEquals(true, column.isVisible());
        }
        for (Column column : columnForm.getHiddenColumns()) {
            Assert.assertEquals(false, column.isVisible());
        }

        result = vpc.confColumns(session, "cancel", visibleColumns, hiddenColumns);
        Assert.assertEquals("main", result);
    }

    @Test
    public void initConfColumns() {
        String result = vpc.initConfColumns(session);
        Assert.assertEquals("conf-columns", result);
    }

    @Test
    public void check() {
        String result = vpc.check(session, 10);
        Assert.assertEquals("main", result);
    }
}
