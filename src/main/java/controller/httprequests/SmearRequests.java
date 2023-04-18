package controller.httprequests;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;

public class SmearRequests extends HTTPRequest{
    private final String SMEAR_URL = "https://smear-backend.rahtiapp.fi/";



    public SmearRequests(){

    }

    /**
     * Fetches all the station metadata from SMEAR
     * @return The response json as a string
     */
    public String getAllStations(){
        return this.genericJsonQuery(SMEAR_URL, "search/station");
    }


    /** Make a parameterized query to SMEAR
     * @param aggregation Aggregation type
     * @param interval Interval of the samples, 15-60 minutes
     * @param startTime Starting date
     * @param endTime Ending date
     * @param variableName The queried variable
     * @return The response json as a string
     */
    public String variableQuery(String aggregation, String interval, String startTime, String endTime, String variableName){

        String queryString = String.join("&", new String[]{ "aggregation=" + aggregation, "interval=" + interval, "from=" + startTime, "to=" + endTime, "tablevariable=" + variableName });
        return this.genericJsonQuery(SMEAR_URL + "search/timeseries?", queryString);
    }


    /** Get the starting and ending date of a variable
     *
     * @param tableVariable The name of the variable that is being queried
     * @return the resulting date json as a String
     */
    public String getStationDates(String tableVariable){
        String result = this.genericJsonQuery(SMEAR_URL, "search/variable?tablevariable=" + tableVariable);
        return result;
    }
}
