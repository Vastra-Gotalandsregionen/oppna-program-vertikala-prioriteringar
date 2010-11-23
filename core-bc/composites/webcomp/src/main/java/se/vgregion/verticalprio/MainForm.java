package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.SektorRaad;

public class MainForm {

    private List<SektorRaad> sectors = new ArrayList<SektorRaad>();

    private List<Column> columns = new ArrayList<Column>();

    private String command;

    private void setSectors(List<SektorRaad> sectors) {
        this.sectors = sectors;
    }

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
}
