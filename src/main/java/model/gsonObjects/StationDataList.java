package model.gsonObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StationDataList {
    @SerializedName("data")
    List<StationData> stationData;

    public List<StationData> getStationData(){
        return stationData;
    }
}
