package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Sector;

public class MainForm {

    private List<Sector> sectors = new ArrayList<Sector>();

    private List<Column> columns = new ArrayList<Column>();

    private String command;

    private void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public List<Sector> getSectors() {
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
