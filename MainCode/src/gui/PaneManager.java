package gui;

import javafx.scene.layout.GridPane;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PaneManager {

    private EnumMap<Table, PaneObject> paneMap = new EnumMap<>(Table.class);

    public PaneManager() {
        initializeMap();
    }

    private void initializeMap() {
        paneMap.put(Table.COMPANY, new PaneObject("comp_id", "name", "hq_location", "tier", "industry", "num_employees", "revenue"));
        paneMap.put(Table.JOBS, new PaneObject("job_id", "comp_ID", "type", "role", "description", "total_compesantion", "required_yoe", "location", "cycle", "date_opened", "deadline"));
    }

    public GridPane gridPane(Table key) {
        return paneMap.get(key).gridPane();
    }
}
