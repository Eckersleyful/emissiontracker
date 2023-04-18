package model.gsonObjects;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class StationData extends GsonObject{


    @SerializedName(value = "CO2", alternate = {"VAR_META.CO206", "HYY_META.CO284", "KUM_EDDY.av_c", "TOR_EDDY.av_c", "SII1_EDDY.AV_c", "SII2_EDDY.AV_c", "KVJ_EDDY.AV_c_LI72", "VII_EDDY.AV_c", "HAL_EDDY.AV_c", "VAR_META.SO2_0", "HYY_META.SO284", "VAR_META.NOX_1", "HYY_META.NOx84"})
    private float data;
    
    @SerializedName("samptime")
    private String sampleTime;


    public float getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }



    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    public LocalDateTime getSampleTimeAsDate(){
        LocalDateTime date = LocalDateTime.parse(sampleTime);
        return date;
    }
}
