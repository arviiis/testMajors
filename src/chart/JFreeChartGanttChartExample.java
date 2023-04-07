package chart;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

public class JFreeChartGanttChartExample extends JFrame {

    private static final long serialVersionUID = 1L;

    public JFreeChartGanttChartExample(String applicationTitle, String chartTitle) {
	super(applicationTitle);

	// based on the dataset we create the chart
	JFreeChart chart = ChartFactory.createGanttChart(chartTitle, "Development", "Time", createDataset(),
		 true, true, true);

	// Adding chart into a chart panel
	ChartPanel chartPanel = new ChartPanel(chart);

	// settind default size
	chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

	// add to contentPane
	setContentPane(chartPanel);
	
//	main();
    }

    private IntervalCategoryDataset createDataset() {

	TaskSeriesCollection dataset = new TaskSeriesCollection();
	
	
	TaskSeries expected = new TaskSeries("Expected Date");
	expected.add(new Task("Analysis", Date.from(LocalDate.of(2018, 9, 5).atStartOfDay().toInstant(ZoneOffset.UTC)),
		Date.from(LocalDate.of(2018, 9, 8).atStartOfDay().toInstant(ZoneOffset.UTC))));

	expected.add(new Task("Design", Date.from(LocalDate.of(2018, 9, 12).atStartOfDay().toInstant(ZoneOffset.UTC)),
		Date.from(LocalDate.of(2018, 9, 16).atStartOfDay().toInstant(ZoneOffset.UTC))));

	expected.add(
		new Task("Development", Date.from(LocalDate.of(2018, 9, 19).atStartOfDay().toInstant(ZoneOffset.UTC)),
			Date.from(LocalDate.of(2018, 9, 23).atStartOfDay().toInstant(ZoneOffset.UTC))));

	expected.add(new Task("Testing", Date.from(LocalDate.of(2018, 9, 26).atStartOfDay().toInstant(ZoneOffset.UTC)),
		Date.from(LocalDate.of(2018, 9, 29).atStartOfDay().toInstant(ZoneOffset.UTC))));

	dataset.add(expected);
	
	TaskSeries actual = new TaskSeries("Actual Date");
	actual.add(
		new Task("Analysis", Date.from(LocalDate.of(2018, 9, 5).atStartOfDay().toInstant(ZoneOffset.UTC)),
			Date.from(LocalDate.of(2018, 9, 7).atStartOfDay().toInstant(ZoneOffset.UTC))));

	actual.add(new Task("Design", Date.from(LocalDate.of(2018, 9, 8).atStartOfDay().toInstant(ZoneOffset.UTC)),
		Date.from(LocalDate.of(2018, 9, 19).atStartOfDay().toInstant(ZoneOffset.UTC))));

	actual.add(new Task("Development", Date.from(LocalDate.of(2018, 9, 20).atStartOfDay().toInstant(ZoneOffset.UTC)),
		Date.from(LocalDate.of(2018, 9, 28).atStartOfDay().toInstant(ZoneOffset.UTC))));

	actual.add(new Task("Testing", Date.from(LocalDate.of(2018, 9, 28).atStartOfDay().toInstant(ZoneOffset.UTC)),
		Date.from(LocalDate.of(2018, 10, 3).atStartOfDay().toInstant(ZoneOffset.UTC))));
	
	
	
        dataset.add(actual);


	return dataset;

    }

    public static void main(String[] args) {
	JFreeChartGanttChartExample chart = new JFreeChartGanttChartExample("Gantt Chart Example",
		"Software Development Phase?");
	chart.pack();
	chart.setVisible(true);
    }
}
