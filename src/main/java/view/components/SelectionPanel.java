package view.components;


import model.gsonObjects.Gas;
import model.gsonObjects.GsonObject;
import model.gsonObjects.Station;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SelectionPanel<T extends GsonObject> extends JPanel  {
    /**
     * If the amount of check boxes varies, they can be stored in this list (and removed or read if needed)
     */
    private final List<JCheckBox> checkBoxes = new LinkedList<>();

    public List<JCheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    /**
     * Constructor that creates a check box for every selection name it is given
     * @param selections a list of selection items, not null
     */
    public SelectionPanel(@NotNull List<T> selections) {
        // Go through all gases
        int row = 1;
        for (T selection : selections) {
            // Create checkbox
            JCheckBox checkBox = new JCheckBox();


            // Store the ID in the name
            if(selection instanceof Station){
                Station station = (Station) selection;
                checkBox.setText(station.getStationName());
                // Store the ID in the name field for later use.
                checkBox.setName("" + station.getStationID());
            }
            else if(selection instanceof Gas){
                Gas gas = (Gas) selection;
                checkBox.setText(gas.getName());
                checkBox.setName("" + gas.getId());
                // Store the ID in the name field for later use.
            }

            // add it to the list of checkboxes
            checkBoxes.add(checkBox);
            // add it to the panel
            this.add(checkBox, new GridLayout(row, 0, 0, 0));
            // move to the next row
            row++;
        }
    }

    /**
     * Method that goes through all checkboxes and returns the SelectionModel::id for every select one
     * @return all selected ids
     */
    public List<Integer> getSelectedIds() {
        List<Integer> checkedStations = new ArrayList<Integer>();
        for(JCheckBox box : checkBoxes){
            if(box.isSelected() && box.isEnabled()){
                checkedStations.add(Integer.parseInt(box.getName()));
            }
        }
        return checkedStations;
    }
    public List<Integer> getAllIds() {
        List<Integer> checkedStations = new ArrayList<Integer>();
        for(JCheckBox box : checkBoxes){
            checkedStations.add(Integer.parseInt(box.getName()));

        }
        return checkedStations;
    }
}

