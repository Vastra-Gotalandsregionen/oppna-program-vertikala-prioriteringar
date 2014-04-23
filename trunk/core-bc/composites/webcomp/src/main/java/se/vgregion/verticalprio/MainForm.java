package se.vgregion.verticalprio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.SektorRaad;

public class MainForm implements Serializable {

    private static final long serialVersionUID = 3428105711985037172L;

    /**
     *  
     */
    public MainForm() {
        allSektorsRaad.setId(-1l);
        allSektorsRaad.setKod("Alla sektorsråd");
    }

    private List<SektorRaad> sectors = new ArrayList<SektorRaad>();

    private SektorRaad allSektorsRaad = new SektorRaad();

    private List<Column> columns = new ArrayList<Column>();

    private String command;

    public List<SektorRaad> getSectors() {
        return sectors;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setAllSektorsRaad(SektorRaad allSektorsRaad) {
        this.allSektorsRaad = allSektorsRaad;
    }

    public SektorRaad getAllSektorsRaad() {
        return allSektorsRaad;
    }

}
