package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.httprequests.APIRouteConstants;
import controller.httprequests.SmearRequests;
import controller.httprequests.StatfiRequests;
import model.EmissionModel;
import model.gsonObjects.StatfiDataArray;
import model.gsonObjects.Station;
import model.gsonObjects.StationDataList;
import model.gsonObjects.StationDate;
import view.EmissionView;

import view.components.SelectionPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class HeadController {


    private final StatfiRequests statfiRequests;
    private Gson gson;
    private EmissionModel model;
    private EmissionView view;
    private SmearRequests smearRequests;


    public EmissionModel getModel() {
        return model;
    }
    public EmissionView getView() {
        return view;
    }


    public HeadController(){
       gson = new Gson();
       this.model = new EmissionModel();
       smearRequests = new SmearRequests();
       statfiRequests = new StatfiRequests();

       //Fetch the statfi data as it is static and wont change per the user choices
       StatfiDataArray statfi = gson.fromJson(statfiRequests.getHistoricalData(), StatfiDataArray.class);
       model.setStatfiData(statfi.getStatfiDataArray());

       //Get the needed metadata to show the station objects to the user
       getStationData();
       getStationDates();

       this.view = new EmissionView(this.model.getSmearStations());

       //Bind required action listeners
       bindSelectorListener();
       bindEligibleStations();
    }


    /**
     * @param variable The attribute we are querying for
     * @param lowerBound The lower bound of the date
     * @param upperBound The upper bound of the date
     * @return The data json in a string representation
     */
    private String getSmearData(String variable, LocalDate lowerBound, LocalDate upperBound) {
        String lowerString = lowerBound.toString() + "T01:00:00.000";
        String upperString = upperBound.toString() + "T23:00:00.000";
        return (smearRequests.variableQuery("MAX", "60", lowerString, upperString, variable));
    }


    /**
     * Fetches the ending and starting dates for each data attribute for
     * each station
     *
     */
    private void getStationDates() {
        for(Station s : this.model.getSmearStations()){
            String[] attributes = APIRouteConstants.STATFI_GAS_ATTRIBUTES.get(s.getStationID());

            for(String attr : attributes){
                String dateJson = smearRequests.getStationDates(attr).replace("[", "");
                dateJson = dateJson.replace("]", "");
                StationDate dateObject = gson.fromJson(dateJson, StationDate.class);
                s.setStartingDate(dateObject.getPeriodStart());
                s.setEndingDate(dateObject.getPeriodEnd());

            }
        }

    }

    /**
     * Fetches all the available stations from SMEAR and
     * generates Gson objects from them.
     *
     */
    private void getStationData() {
        String stationJson = smearRequests.getAllStations();
        model.setSmearStations(gson.fromJson(stationJson, new TypeToken<List<Station>>(){}.getType()));
        model.bindUrlToStation();

    }


    /**
     * @param selectedIds All the ids of the selected gases
     * @param lowerBound Lower bound of the queried date
     * @param upperBound Upper bound of the queried date
     */
    private void getSelectedStationGasData(List<Integer> selectedIds, LocalDate lowerBound, LocalDate upperBound){

        for(Station s : model.getActiveStations()){
            for(int i : selectedIds){
                StationDataList stationDataObject;
                String json = getSmearData(s.getAttributeBasedOnID(i), lowerBound, upperBound);
                if(i == 1){
                    stationDataObject = gson.fromJson(json, StationDataList.class);
                    s.setCo2StationData(stationDataObject.getStationData());
                }
                if(i == 2){
                    stationDataObject = gson.fromJson(json, StationDataList.class);
                    s.setSo2StationData(stationDataObject.getStationData());
                }
                if(i == 3){
                    stationDataObject = gson.fromJson(json, StationDataList.class);
                    s.setNoxStationData(stationDataObject.getStationData());
                }

            }
        }
    }

    /**
     * Binds an action listener to the button that is responsible
     * for generating the date selectors based on the selected gases and stations
     */
    private void bindSelectorListener(){
        this.view.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getModel().setActiveStations(getModel().defineActiveStations(getView().getStationPanel().getSelectedIds()));

                view.createDateSelectors(getSmearLowerBoundDate(), getSmearUpperBoundDate());
                bindDrawGraphListener();

            }
        });
    }

    /** Binds an action listener to the button that is responsible
     * for calling the draw functions of the graph panels
     *
     */
    private void bindDrawGraphListener(){
        this.view.getDrawGraphButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //If the selected dates are over 10 years apart from each other, alert the user.
                //This is because the SMEAR api doesn't allow queries were the dates are more than 10 years apart

                if(ChronoUnit.YEARS.between(view.getSmearLowerBound().getDate(), view.getSmearUpperBound().getDate()) >= 10){
                    JOptionPane.showMessageDialog(null, "The maximum difference between the SMEAR dates is 10 years");
                    return;
                }

                getSelectedStationGasData(getView().getGasPanel().getSelectedIds(), view.getSmearLowerBound().getDate(), view.getSmearUpperBound().getDate());
                view.drawSmearGraph(model.getEligibleAndActiveStations(), view.getGasPanel().getSelectedIds());
                view.drawStatfiGraph(model.getStatfiData(), view.getStatfiLowerBound().getDate().getYear(), view.getStatfiUpperBound().getDate().getYear());
            }
        });
    }

    /**
     * Bind the actionslistener for the gas checkboxes
     * Whenever a gas checkbox is chosen, it checks
     * which stations have that gas available and disable/enable
     * the boxes accordingly
     */
    private void bindEligibleStations(){
        SelectionPanel tempPanel = this.view.getGasPanel();
        for(Object j : tempPanel.getCheckBoxes()){
            JCheckBox temp = (JCheckBox) j;
            temp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        model.deductEligibleStations(tempPanel.getSelectedIds());
                        for(Station s : model.getSmearStations()){
                            for(Object box : view.getStationPanel().getCheckBoxes()){

                                JCheckBox checkBox = (JCheckBox) box;
                                int id = Integer.parseInt(checkBox.getName());

                                if(id == s.getStationID() && !s.isEligible()){
                                    checkBox.setEnabled(false);
                                    checkBox.setSelected(false);
                                }
                                else if(id == s.getStationID()){
                                    checkBox.setEnabled((true));
                                }

                            }
                        }

                }
            });
        }
    }

    /**
     * Finds highest starting date of the selected stations
     * For example dates 2011, 2008, 2005 returns 2011
     * Bit bugged atm
     * @return The highest LocalDateTime object
     */
    private LocalDateTime getSmearLowerBoundDate(){

        ArrayList<LocalDateTime> dates = new ArrayList<>();

        for(Station s : this.model.getEligibleAndActiveStations()){
            for(int i : this.view.getGasPanel().getSelectedIds()){
                dates.add(s.getStationStartDates().get(i - 1));
            }

        }

        LocalDateTime currentUpper = dates.remove(0);
        for(LocalDateTime date : dates){
            if(date.isAfter(currentUpper)){
                currentUpper = date;
            }
        }

        return currentUpper;
    }


    /**
     * Finds lowest starting date of the selected stations
     * For example dates 2011, 2008, 2005 returns 2005
     * Bit bugged atm
     * @return The lowest LocalDateTime object
     */
    private LocalDateTime getSmearUpperBoundDate(){

        ArrayList<LocalDateTime> dates = new ArrayList<>();

        for(Station s : this.model.getEligibleAndActiveStations()){
            for(int i : this.view.getGasPanel().getSelectedIds()){
                dates.add(s.getStationEndDates().get(i - 1));
            }
        }

        LocalDateTime currentUpper = dates.remove(0);
        for(LocalDateTime date : dates){
            if(date.isBefore(currentUpper)){
                currentUpper = date;
            }
        }

        return currentUpper;
    }


}
