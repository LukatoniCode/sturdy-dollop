import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class BarChart {

    public BarChart(DefaultCategoryDataset dataset2, JPanel panel, String title, String Xos, String Yos) {
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                Xos,
                Yos,
                dataset2,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        panel.add(chartPanel);
    }
}
