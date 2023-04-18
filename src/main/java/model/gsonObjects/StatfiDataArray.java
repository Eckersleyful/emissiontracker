package model.gsonObjects;

import com.google.gson.annotations.SerializedName;

public class StatfiDataArray extends GsonObject{

    @SerializedName(value = "value")
    Float[] statfiDataArray;

    public Float[] getStatfiDataArray() {
        return statfiDataArray;
    }
}
