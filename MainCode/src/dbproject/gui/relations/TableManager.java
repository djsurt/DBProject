package dbproject.gui.relations;

import dbproject.gui.Relation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.EnumMap;

public final class TableManager {

    private final EnumMap<Relation, TableView> tableMap = new EnumMap<>(Relation.class);

    public TableManager() {
        initializeMap();
    }

    /**
     * Fills the paneMap with predesignated keys and values (should be called in this class' constructor)
     */
    private void initializeMap() {
        tableMap.put(Relation.LOCATION, locationTable());
    }

    public TableView getTable(Relation key) {
        return tableMap.get(key);
    }

    private <T> TableView<T> baseTable(TableView<T> table) {
        table.setTableMenuButtonVisible(true);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);

        return table;
    }

    public TableView locationTable() {
        TableView<Location> table = baseTable(new TableView<>());

        TableColumn<Location, String> col1 = new TableColumn<>("country");
        TableColumn<Location, String> col2 = new TableColumn<>("city");
        TableColumn<Location, String> col3 = new TableColumn<>("state");
        TableColumn<Location, Integer> col4 = new TableColumn<>("location_id");

        col1.setCellValueFactory(new PropertyValueFactory<>("country"));
        col2.setCellValueFactory(new PropertyValueFactory<>("city"));
        col3.setCellValueFactory(new PropertyValueFactory<>("state"));
        col4.setCellValueFactory(new PropertyValueFactory<>("location_id"));

        table.getColumns().addAll(col1, col2, col3, col4);

        return table;
    }
}
