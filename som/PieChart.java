import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

public class PieChart {

    public PieChart(DefaultPieDataset dataset, JPanel panel, String title) {
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));  // Set size for the chart panel
        panel.add(chartPanel);
    }
}
