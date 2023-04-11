package chart;

import java.awt.Color;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.KeyedObjects2D;
import org.jfree.data.KeyedValues2D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import ProductHolonGUI.WelcomeFrame;
import def.Main;
import jade.core.AID;

public class GanttChartFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private String chartTitle;
	private String tabTitle;
	
	
	TaskSeriesCollection dataset = new TaskSeriesCollection();
	public List<TaskSeries> taskSeriesList = new ArrayList<TaskSeries>();
	
	public GanttChartFrame(String tabTitle, String chartTitle) {
		
		this.tabTitle = tabTitle;
		this.chartTitle = chartTitle;

	}
	
	public void showFrame() {
		
		setTitle(tabTitle); // set tab title
		
		// based on the dataset, create the chart
		JFreeChart chart = ChartFactory.createGanttChart(chartTitle, "Agent name", "Timeline", dataset,
			 true, true, true);

		 CategoryPlot plot = (CategoryPlot) chart.getPlot();

		 MyGanttRenderer renderer = new MyGanttRenderer();
		 plot.setRenderer(renderer);

		 renderer.setDefaultItemLabelGenerator(new CategoryItemLabelGenerator() {

		      public String generateLabel(CategoryDataset dataSet, int series, int categories) {
		    	List keys = dataSet.getColumnKeys();
		    	
		    	  
		       String label = (String) keys.get(categories);
		       return label;
		      }

		      public String generateColumnLabel(CategoryDataset dataset, int categories) {
		          return dataset.getColumnKey(categories).toString();
		      }

		      public String generateRowLabel(CategoryDataset dataset, int series) {
		          return dataset.getRowKey(series).toString();
		      }
		 });

		 renderer.setDefaultItemLabelsVisible(true);
		 renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_LEFT));
		
		// Adding chart into a chart panel
		ChartPanel chartPanel = new ChartPanel(chart);

		// setting default size
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

		// add to contentPane
		setContentPane(chartPanel);
//		createChart(chartTitle);
		this.pack(); 
		this.setVisible(true);
	}
	
	public void addTaskSeries(String name) {
		TaskSeries x = new TaskSeries(name);
		x.setDescription(name);
		dataset.add(x);
		taskSeriesList.add(x);
	}

    // add a task to appropriate task series
    public void addTask(String ohName, String thName, int price, int duration) {
    	
    	long endTime = WelcomeFrame.startDate.getTime() + price*1000; // full endTime in ms
    	
    	// start and end time
    	Date endDate = new Date(endTime); //full estimated end time in ms
        Date startDate = new Date(endTime-duration*1000); // task start time
        
        // check which one of the task series has a matching description with the TH name. Add the task to this task series.
        for (int i = 0; i < taskSeriesList.size(); i++) {

        	if (taskSeriesList.get(i).getDescription().equals(thName)) {
            	System.out.println("Description:                                               " + taskSeriesList.get(i).getDescription());
            	System.out.println("TH name:                                                   " + thName);
        		taskSeriesList.get(i).add(new Task(ohName, startDate, endDate));
        		System.out.println("Task added to: " + thName+ " task series!");
        		System.out.println("Executor OH name: " + ohName);
        		System.out.println("Start time: " + startDate);
        		System.out.println("End time: " + endDate);
        		break;
        	}
		}
    }
    private Date date(Date now, final int minute, final int second) {

        final Calendar calendar = Calendar.getInstance();
        
        calendar.set(now.getYear(), now.getMonth(), now.getDay(), now.getHours(), minute, second);
        
        final Date result = calendar.getTime();
        System.out.println(result);
        return result;
    }
    
    private IntervalCategoryDataset createDataset() {

		TaskSeriesCollection dataset2 = new TaskSeriesCollection();
		
		Date now = new Date();
		        
        TaskSeries s1 = new TaskSeries("TH Agent_A1");
        
        Task t1 = new Task("first OH", date(now, 15, 30), date(now, 35, 00));
        
        t1.addSubtask(new Task("first OH", date(now, 15, 30), date(now, 16, 00)));        
        t1.addSubtask(new Task("first OH", date(now, 16, 30), date(now, 17, 00)));

        s1.add(t1);

        dataset2.add(s1);

        
		return dataset2;
	}
    
}
