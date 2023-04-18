package controller.httprequests;

public class StatfiRequests extends HTTPRequest{


    //The Statfi url
    private final String STATFI_URL = "https://pxnet2.stat.fi/PXWeb/api/v1/en/ymp/taulukot/Kokodata.px";

    //This monster is the string which fetches all the yearly data of CO2 intensity from the years 1990-2016 from Statfi
    final String statfiJsonString = "{\n        \"query\": [\n            {\n                \"code\": \"Tiedot\",\n                \"selection\": {\n                    \"filter\": \"item\",\n                    \"values\": [\"Khk_yht_las\"]\n                }\n            },\n            {\n                \"code\": \"Vuosi\",\n                \"selection\": {\n                    \"filter\": \"item\",\n                    \"values\": [\"1990\", \"1991\", \"1992\", \"1993\", \"1994\", \"1995\", \"1996\", \"1997\", \"1998\", \"1999\", \"2000\", \"2001\", \"2002\", \"2003\", \"2004\", \"2005\", \"2006\", \"2007\", \"2008\", \"2009\", \"2010\", \"2011\", \"2012\", \"2013\", \"2014\", \"2015\", \"2016\"]\n                }\n            }\n        ]\n    }";


    /** Makes a post request with the statfi url and post body
     * @return the statfi response json as a string
     */
    public String getHistoricalData(){
        return genericJsonPostQuery(STATFI_URL, statfiJsonString);
    }
}

