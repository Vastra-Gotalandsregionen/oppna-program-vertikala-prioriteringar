package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class VerticalPrioControllerTest {

    @Resource(name = "testVerticalPrioController")
    private VerticalPrioController vpc;

    private MockHttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() {
        /*
         * vpc = new VerticalPrioController() {
         * 
         * @Override public SortedMap<String, String> getPrioPropertyTexts() { columnTextsPropertiesFileName =
         * "/column-texts-test.properties"; return super.getPrioPropertyTexts(); } };
         */
        request = new MockHttpServletRequest();
        session = request.getSession();
        MainForm form = new MainForm();
        session.setAttribute("form", form);
    }

    @Test
    public void main() {
        String defaultResult = vpc.main(session);
        Assert.assertEquals("main", defaultResult);
    }

    // @Test
    // public void result() {
    // vpc = new VerticalPrioController();
    // List<Prioriteringsobjekt> prios = vpc.result(session);
    // Assert.assertNotSame(0, prios.size());
    // }

    private List<Column> getVisibleColumns() {
        List<Column> result = new ArrayList<Column>(Prioriteringsobjekt.getDefaultColumns());

        for (Column column : Prioriteringsobjekt.getDefaultColumns()) {
            if (!column.isVisible()) {
                result.remove(column);
            }
        }

        return result;
    }

    private List<String> toNameList(List<Column> columns) {
        List<String> result = new ArrayList<String>();
        for (Column column : columns) {
            result.add(column.getName());
        }
        return result;
    }

    @Test
    public void confColumns() {
        List<Column> visible = getVisibleColumns();

        List<String> hiddenColumns = toNameList(vpc.getColumns());
        List<String> visibleColumns = toNameList(vpc.getColumns());

        vpc.initConfColumns(session);

        ConfColumnsForm columnForm = (ConfColumnsForm) session.getAttribute("confCols");
        Assert.assertEquals(0, columnForm.getHiddenColumns().size());

        String result = vpc.confColumns(session, "hide", visibleColumns, hiddenColumns);
        Assert.assertEquals("conf-columns", result);

        result = vpc.confColumns(session, "show", visibleColumns, hiddenColumns);
        Assert.assertEquals("conf-columns", result);

        result = vpc.confColumns(session, "save", visibleColumns, hiddenColumns);
        Assert.assertEquals("main", result);
    }

    @Test
    public void initConfColumns() {
        String result = vpc.initConfColumns(session);
        Assert.assertEquals("conf-columns", result);
    }

    @Test
    public void check() {
        String result = vpc.check(session, 1);
        Assert.assertEquals("main", result);
    }

    @Test
    public void getMarkedLeafs() {
        SektorRaad root = new SektorRaad(0l);
        List<SektorRaad> roots = new ArrayList<SektorRaad>();
        roots.add(new SektorRaad(1l));
        roots.add(root);
        roots.add(new SektorRaad(2l));

        root.setSelected(true);
        root.setChildren(new ArrayList<SektorRaad>());
        root.getChildren().add(new SektorRaad(1l));
        List<SektorRaad> result = vpc.getMarkedLeafs(roots);
        System.out.println(result);
    }

}
