package view.components;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.text.SimpleDateFormat;

import java.util.HashMap;

public class StatfiGraphPanel {


    private JFreeChart statfiChart;
    private ChartPanel chartPanel;
    private final int STATFI_START_YEAR = 1990;

    public StatfiGraphPanel(Float[] statfiValues, int startYear, int endYear){


        HashMap<Integer, Float> averageMap = new HashMap<>();

        //As we always fetch the same amount of data, we deduct the starting index from
        //the year 1990 (start year)
        //For example if the user wants data from year 1999 - n
        //we know that we must start from the index 9 abs(1990 - start)
        int startingIndex = Math.abs(startYear - STATFI_START_YEAR);


        //Iterate through the statfi data and put the values in a map
        //Where we use the year as a key, the value is already a mean value
        for(int i = startYear; i <= endYear; i++){
            averageMap.put(i, statfiValues[startingIndex]);
            startingIndex++;
        }


        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries tempSeries = new TimeSeries("CO2 intensity");


        averageMap.entrySet().forEach(entry -> {
            tempSeries.add(new Day(1, 1, entry.getKey()), entry.getValue());

        });


        dataSet.addSeries(tempSeries);
        statfiChart = ChartFactory.createTimeSeriesChart("The historic values of CO2 intensity", "Year", "Gas intensity", dataSet);
        String dateFormat = "yyyy";
        XYPlot plot = statfiChart.getXYPlot();
        DateAxis axis = (DateAxis)plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
        chartPanel = new ChartPanel(statfiChart);
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }
}
