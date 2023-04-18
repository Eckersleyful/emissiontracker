package view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import model.EmissionModel;
import model.gsonObjects.Gas;
import model.gsonObjects.Station;
import org.jfree.chart.ChartPanel;
import view.components.SelectionPanel;
import view.components.SmearGraphPanel;
import view.components.StatfiGraphPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EmissionView {



    private SelectionPanel<Station> stationPanel;
    private SelectionPanel<Gas> gasPanel;
    private ChartPanel smearChartPanel;
    private SmearGraphPanel smearGraphPanel;
    private StatfiGraphPanel statfiGraphPanel;
    private ChartPanel statfiChartPanel;

    public JButton getOkButton() {
        return okButton;
    }
    public JButton getDrawGraphButton(){
        return drawGraphButton;
    }
    private JFrame mainFrame;
    private JButton okButton;
    private JButton drawGraphButton;

    private JPanel mainDatePanel;
    private JPanel smearDatePanel;


    public DatePicker getSmearLowerBound() {
        return smearLowerBound;
    }

    public DatePicker getSmearUpperBound() {
        return smearUpperBound;
    }
    private DatePicker smearLowerBound;
    private DatePicker smearUpperBound;
    private DatePicker statfiUpperBound;

    public DatePicker getStatfiUpperBound() {
        return statfiUpperBound;
    }

    public DatePicker getStatfiLowerBound() {
        return statfiLowerBound;
    }

    private DatePicker statfiLowerBound;



    public SelectionPanel<Station> getStationPanel() {
        return stationPanel;
    }

    public SelectionPanel<Gas> getGasPanel() {
        return gasPanel;
    }



    /**
     * Initial dimensions of this frame
     */
    private final static Dimension DIMENSION = new Dimension(1000, 1400);

    public EmissionView(List<Station> smearStations) {


        mainFrame = new JFrame("Emissions graph");
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.setSize(DIMENSION);



        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.LINE_AXIS)); // Horizontal

        stationPanel = new SelectionPanel<>(smearStations);
        stationPanel.setLayout(new BoxLayout(stationPanel, BoxLayout.PAGE_AXIS)); // Vertical

        gasPanel = new SelectionPanel<>(EmissionModel.GASES);
        gasPanel.setLayout(new BoxLayout(gasPanel, BoxLayout.PAGE_AXIS)); // Vertical

        choicePanel.add(gasPanel);
        choicePanel.add(stationPanel);

        okButton = new JButton("Ok");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(okButton);

        JPanel selectionContainer = new JPanel();
        selectionContainer.add(choicePanel, BorderLayout.CENTER);
        selectionContainer.add(buttonPanel, BorderLayout.PAGE_END);
        mainFrame.add(selectionContainer);

        mainDatePanel = new JPanel(new GridLayout(3, 1));
        this.mainFrame.add(mainDatePanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /** Creates the JFreeChart graphes from smear data
     * @param eligibleStations The stations from which the data is pulled from
     * @param selectedIds The wanted gases
     */
    public void drawSmearGraph(List<Station> eligibleStations, List<Integer> selectedIds) {
        if(smearChartPanel != null){
            this.mainFrame.remove(smearChartPanel);
        }
        for(int i : selectedIds){
            smearGraphPanel = new SmearGraphPanel(eligibleStations, i);
            smearChartPanel = smearGraphPanel.getChartPanel();
            this.mainFrame.add(smearChartPanel);
        }


        this.mainFrame.revalidate();
    }

    /** Creates the JFreeChart graphes from the Statfi data
     * @param values
     * @param startYear
     * @param endYear
     */
    public void drawStatfiGraph(Float[] values, int startYear, int endYear) {

        if(statfiGraphPanel != null){
            this.mainFrame.remove(statfiChartPanel);
        }
        statfiGraphPanel = new StatfiGraphPanel(values, startYear, endYear);
        statfiChartPanel = statfiGraphPanel.getChartPanel();
        this.mainFrame.add(statfiChartPanel);
        this.mainFrame.revalidate();
    }

    /** Creates the date selectors for the statfi and smear data
     * @param smearLowerBoundDate The metadata of the smear time range starting date
     * @param smearUpperBoundDate The metadata of the smear time range ending date
     */
    public void createDateSelectors(LocalDateTime smearLowerBoundDate, LocalDateTime smearUpperBoundDate){



        //Need to check and remove the selectors if user wants to pick new dates
        //as the library im using can only use the settings when creating the objects
        if(this.mainDatePanel != null){
            this.mainDatePanel.removeAll();
        }


        smearDatePanel = new JPanel();
        JLabel infoLabel = new JLabel("Choose the date range for the SMEAR data");

        smearLowerBoundDate = smearLowerBoundDate.plusDays(1);

        DatePickerSettings smearLowerDateSettings = new DatePickerSettings();
        DatePickerSettings smearUpperDateSettings = new DatePickerSettings();

        smearLowerBound = new DatePicker(smearLowerDateSettings);
        smearUpperBound = new DatePicker(smearUpperDateSettings);

        smearLowerDateSettings.setDateRangeLimits(smearLowerBoundDate.toLocalDate(), smearUpperBoundDate.toLocalDate());
        smearUpperDateSettings.setDateRangeLimits(smearLowerBoundDate.toLocalDate(), smearUpperBoundDate.toLocalDate());

        smearLowerBound.setDate(smearLowerBoundDate.toLocalDate());
        smearUpperBound.setDate(smearUpperBoundDate.toLocalDate());

        smearDatePanel.add(infoLabel);
        smearDatePanel.add(smearLowerBound);
        smearDatePanel.add(smearUpperBound);

        mainDatePanel.add(smearDatePanel);

        JPanel statfiDatePanel = new JPanel();
        infoLabel = new JLabel("Choose the date range for the STATFI data");
        DatePickerSettings stafiLowerDateSettings = new DatePickerSettings();
        DatePickerSettings statfiUpperDateSettings = new DatePickerSettings();

        statfiLowerBound = new DatePicker(stafiLowerDateSettings);
        statfiUpperBound = new DatePicker(statfiUpperDateSettings);

        LocalDate lowerBound = LocalDate.of(1990,1,1);
        LocalDate upperBound = LocalDate.of(2016,1,1);
        stafiLowerDateSettings.setDateRangeLimits(lowerBound, upperBound);
        statfiUpperDateSettings.setDateRangeLimits(lowerBound, upperBound);

        statfiLowerBound.setDate(lowerBound);
        statfiUpperBound.setDate(upperBound);

        statfiDatePanel.add(infoLabel);
        statfiDatePanel.add(statfiLowerBound);
        statfiDatePanel.add(statfiUpperBound);

        mainDatePanel.add(statfiDatePanel);

        JPanel buttonPanel = new JPanel();
        drawGraphButton = new JButton("Draw graphs");
        buttonPanel.add(drawGraphButton);
        mainDatePanel.add(buttonPanel);

        this.mainFrame.revalidate();

    }

}