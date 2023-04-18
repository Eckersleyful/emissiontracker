package model.gsonObjects;

import com.google.gson.annotations.SerializedName;

public class StationDate extends GsonObject{

    @SerializedName(value = "periodStart")
    private String periodStart;

    @SerializedName(value = "periodEnd")
    private String periodEnd;


    public String getPeriodStart() {
        return this.periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return this.periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }
}
