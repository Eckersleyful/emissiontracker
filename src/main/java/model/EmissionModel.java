package model;

import com.google.gson.Gson;
import model.gsonObjects.Gas;
import model.gsonObjects.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static controller.httprequests.APIRouteConstants.*;




public class EmissionModel{

    public EmissionModel(){
    }


    public static final List<Gas> GASES = Arrays.asList(
            new Gas("CO2", 1),
            new Gas("SO2", 2),
            new Gas("NO", 3));



    List<Station> smearStations;
    private List<Station> activeStations;
    private Float[] statfiData;

    public void setActiveStations(List<Station> activeStations) {
        this.activeStations = activeStations;
    }
    public List<Station> getActiveStations() {
        return activeStations;
    }


    public void setStatfiData(Float[] statfiData) {
        this.statfiData = statfiData;
    }
    public Float[] getStatfiData() {
        return statfiData;
    }

    public void setSmearStations(List<Station> smearStations){
        this.smearStations = smearStations;
    }
    public List<Station> getSmearStations(){
        return this.smearStations;
    }

    /**
     * Goes through the static map to bind a variable attribute name to a certain station
     * This is required as the SMEAR API has varying attribute names for each station. :))
     */
    public void bindUrlToStation(){

        List<Station> tempStations = new ArrayList<>();
        for(Station s : this.getSmearStations()){
            boolean validStation = false;
            for(Map.Entry<Integer, String[]> entry : STATFI_GAS_ATTRIBUTES.entrySet()){
                if(s.getStationID() == entry.getKey()){
                    bindAttributes(s, entry.getValue());
                    validStation = true;
                }
            }
            if(validStation){
                tempStations.add(s);
            }
        }
        this.setSmearStations(tempStations);
    }


    /** Helper function to place the strings in their correct object attributes
     * @param s Station object that the attribute is being bound to
     * @param attributes Array of string, where indexes 0-2 are CO2, SO2, NOx, in that order
     */
    private void bindAttributes(Station s, String[] attributes) {
        if(attributes.length >= 1){
            s.setCo2Attribute(attributes[0]);
        }
        if(attributes.length >= 2){
            s.setSo2Attribute(attributes[1]);
        }
        if(attributes.length >= 3){
            s.setNoxAttribute(attributes[2]);
        }
    }

    /** Returns all active stations. An active station is an station
     * that the user has selected with the checkboxes
     * @param selectedIds The ids of the selected checkboxes
     * @return A list of stations that matched
     */
    public List<Station> defineActiveStations(List<Integer> selectedIds) {
        List<Station> activeStations = new ArrayList<>();
        for(Integer i : selectedIds){
            for(Station s : getSmearStations()){
                if(s.getStationID() == i){
                    activeStations.add(s);
                }
            }
        }
        return activeStations;
    }

    /** Compares the selected gases to the stations available gases
     * @param ids The list of selected gas ids
     */
    public void deductEligibleStations(List<Integer> ids){

        for(Station s : this.getSmearStations()) {
            for(int id : ids) {
                s.setEligible(false);
                if (id == 1 && s.getCo2Attribute() == null) {
                    break;
                }
                else{
                    s.getActiveGasIds().add(1);
                }
                if (id == 2 && s.getSo2Attribute() == null) {
                    break;
                }
                else{
                    s.getActiveGasIds().add(2);
                }
                if (id == 3 && s.getNoxAttribute() == null) {
                    break;
                }
                else{
                    s.getActiveGasIds().add(3);
                }
                s.setEligible(true);
            }
        }
    }


    /** Finds all stations that are both eligible (selected gases
     * match available gases) and active (user has selected them)
     * @return List of station objects
     */
    public List<Station> getEligibleAndActiveStations(){
        if(this.getActiveStations() == null){
            return null;
        }
        List<Station> resultingStations = new ArrayList<>();
        for(Station s : this.getActiveStations()){
            if(s.isEligible()){
                resultingStations.add(s);
            }
        }
        return resultingStations;
    }


}
