package model.gsonObjects;

import com.google.gson.annotations.SerializedName;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Station extends GsonObject {

    @SerializedName("id")
    private int stationID;

    @SerializedName("name")
    private String stationName;

    private boolean eligible;

    private transient ArrayList<LocalDateTime> stationStartDates = new ArrayList<>();
    private transient ArrayList<LocalDateTime> stationEndDates = new ArrayList<>();

    private List<StationData> co2StationData;
    private List<StationData> so2StationData;
    private List<StationData> noxStationData;

    private List<Integer> activeGasIds = new ArrayList<>();

    public String getCo2Attribute() {
        return co2Attribute;
    }

    public void setCo2Attribute(String co2Attribute) {
        this.co2Attribute = co2Attribute;
    }

    public String getSo2Attribute() {
        return so2Attribute;
    }

    public void setSo2Attribute(String so2Attribute) {
        this.so2Attribute = so2Attribute;
    }

    public String getNoxAttribute() {
        return noxAttribute;
    }

    public void setNoxAttribute(String noxAttribute) {
        this.noxAttribute = noxAttribute;
    }

    private String co2Attribute;

    private String so2Attribute;

    private String noxAttribute;

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }


    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getAttributeBasedOnID(int id){
        switch(id){
            case 1:
                return this.getCo2Attribute();
            case 2:
                return this.getSo2Attribute();
            case 3:
                return this.getNoxAttribute();
        }
        return null;
    }

    public List<StationData> getNoxStationData() {
        return noxStationData;
    }

    public void setNoxStationData(List<StationData> noxStationData) {
        this.noxStationData = noxStationData;
    }

    public List<StationData> getSo2StationData() {
        return so2StationData;
    }

    public void setSo2StationData(List<StationData> so2StationData) {
        this.so2StationData = so2StationData;
    }

    public List<StationData> getCo2StationData() {
        return co2StationData;
    }

    public void setCo2StationData(List<StationData> co2StationData) {
        this.co2StationData = co2StationData;
    }

    public List<Integer> getActiveGasIds() {
        return activeGasIds;
    }

    public ArrayList<LocalDateTime> getStationStartDates() {
        return stationStartDates;
    }

    public ArrayList<LocalDateTime> getStationEndDates() {
        return stationEndDates;
    }

    public void setActiveGasIds(List<Integer> activeGasIds) {
        this.activeGasIds = activeGasIds;
    }

    public void setStartingDate(String newDateString){

        LocalDateTime newDate = LocalDateTime.parse(newDateString);
        this.stationStartDates.add(newDate);
    }

    public void setEndingDate(String newDateString){


        /* The ending date can be null which means
         * that the gas is still being recorded and doesn't have an ending date
         * In this situation, we set the end date as today
         */
        if(newDateString == null){
            this.stationEndDates.add(LocalDateTime.now());
            return;
        }

        LocalDateTime newDate = LocalDateTime.parse(newDateString);
        this.stationEndDates.add(newDate);


    }
}
