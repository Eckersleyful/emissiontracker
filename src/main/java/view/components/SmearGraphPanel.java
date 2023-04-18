package view.components;

import model.gsonObjects.Station;
import model.gsonObjects.StationData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmearGraphPanel {


    private JFreeChart smearChart;
    private ChartPanel chartPanel;


    /** A bloated function that is responsible for generating the
     * XY-graph of the Smear data
     * @param stations
     * @param i
     */
    public SmearGraphPanel(List<Station> stations, int i){


        //Holds the average value of one date
        HashMap<LocalDate, Float> averageMap = new HashMap<LocalDate, Float>();

        //Holds all the maps above
        HashMap<String, HashMap> allAverages = new HashMap<String, HashMap>();

        //How many of each samples there were for each date, required for calculating the mean
        HashMap<LocalDate, Integer> amountOfDates = new HashMap<>();

        String chosenGas = "";
        int nullValues = 0;
        LocalDate timeKey = null;

        for(Station s : stations){


                List<StationData> chosenData;

                switch(i) {
                    case 1:
                        chosenData = s.getCo2StationData();
                        chosenGas = "CO2";
                        break;
                    case 2:
                        chosenData = s.getSo2StationData();
                        chosenGas = "SO2";
                        break;
                    case 3:
                        chosenData = s.getNoxStationData();
                        chosenGas = "NOX";
                        break;
                    default:
                        chosenData = null;
                        break;
                }

                //Go though each data node of the selected gas type
                for(StationData data : chosenData){

                    //The api sometimes returns null values (sensor issues?)
                    //so we omit zero values
                    if(data.getData() == 0){
                        nullValues++;
                        continue;
                    }

                    //Use the sample time as a key
                    LocalDateTime sampleTime = data.getSampleTimeAsDate();
                    timeKey = sampleTime.toLocalDate();

                    //If the key is already in the map, add the value to the existing value
                    if(averageMap.containsKey(timeKey)){
                        averageMap.put(timeKey, averageMap.get(timeKey) + data.getData());
                        amountOfDates.put(timeKey, amountOfDates.get(timeKey) + 1);
                    }

                    //Otherwise we create a new key with the starting value
                    else{
                        averageMap.put(timeKey, data.getData());
                        amountOfDates.put(timeKey, 1);
                    }
                }
        }

        //When the stations of the wanted gas are iterated through, we
        //copy the map and place it into the parent map and empty the inner map
        HashMap<LocalDate, Float> averageCopy = new HashMap();
        averageCopy.putAll(averageMap);
        allAverages.put(chosenGas, averageCopy);
        averageMap.clear();

    TimeSeriesCollection dataSet = new TimeSeriesCollection();

    allAverages.entrySet().forEach(entry -> {

        //Iterate through the map inside the map and create a new TimeSeries (XY graph piece)
        TimeSeries tempSeries = new TimeSeries(entry.getKey());
        Map temp = entry.getValue();


        for(Object entrySet : temp.entrySet()){

            //Use the key (date) as the X axis and the average as the Y
            //We get the average by dividing the sum with the amount of dates present
            Map.Entry<LocalDate, Float> entries = (Map.Entry<LocalDate, Float>) entrySet;
            LocalDate tempDate = entries.getKey();
            float value = entries.getValue() / amountOfDates.get(tempDate);
            tempSeries.add(new Day(tempDate.getDayOfMonth(), tempDate.getMonthValue(), tempDate.getYear()), value);

        }
        //Add each individual gas series into the dataset
        dataSet.addSeries(tempSeries);
    });

    //Let JFreeChart make the time chart for us from the gathered data with only the "YYYY" time format
    smearChart = ChartFactory.createTimeSeriesChart("Selected gases during the SMEAR period", "Date", "Gas intensity", dataSet);
    String dateFormat = "yyyy";
    XYPlot plot = smearChart.getXYPlot();
    DateAxis axis = (DateAxis)plot.getDomainAxis();
    axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
    chartPanel = new ChartPanel(smearChart);

    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }
}
