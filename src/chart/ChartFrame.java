package chart;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;

//import jade.tools.gui.ACLTextArea.InputHandler.end;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;

public class ChartFrame extends JFrame {

	private JPanel contentPane;
	private JButton btnCreateSchedule;
	private GanttChartCustom ganttChartCustom1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	       /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChartFrame().setVisible(true);
            }
        });
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ChartFrame frame = new ChartFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the frame.
	 */
	public ChartFrame() {
		initComponents();
		createEvents();

	}
	private void initComponents() {
		
		
		setTitle("Operation holon task schedule");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1255, 850);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnCreateSchedule = new JButton("Show schedule");
		btnCreateSchedule.setBounds(10, 11, 113, 23);
		contentPane.add(btnCreateSchedule);
		
		ganttChartCustom1 = new GanttChartCustom();
		ganttChartCustom1.setBounds(91, 544, 543, -420);
		contentPane.add(ganttChartCustom1);
		ganttChartCustom1.setLayout(null);

	}
	
	private void createEvents() {
		btnCreateSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("performing action");
				ganttChartCustom1.setDataset(createDataset(), "randomTitle", "OH agent names", "Timeline");
			}
		});
	}
	
	public IntervalCategoryDataset createDataset() {
		
		TaskSeriesCollection dataset = new TaskSeriesCollection();
		
        TaskSeries s1 = new TaskSeries("Scheduled");

        s1.add(new Task("first OH", date(9, Calendar.APRIL, 2001), date(10, Calendar.APRIL, 2001)));
        s1.add(new Task("second OH", date(6, Calendar.APRIL, 2001), date(15, Calendar.APRIL, 2001)));
        s1.add(new Task("third OH", date(10, Calendar.APRIL, 2001), date(12, Calendar.APRIL, 2001)));
        
        dataset.add(s1);
        return dataset;      
//        s1.add(new Task("stacker 1",
//               new SimpleTimePeriod(date(9, Calendar.APRIL, 2001),
//                                    date(10, Calendar.APRIL, 2001))));
//        s1.add(new Task("Requirements Analysis",
//               new SimpleTimePeriod(date(10, Calendar.APRIL, 2001),
//                                    date(5, Calendar.MAY, 2001))));
//        s1.add(new Task("Design Phase",
//               new SimpleTimePeriod(date(6, Calendar.MAY, 2001),
//                                    date(30, Calendar.MAY, 2001))));
//        s1.add(new Task("Design Signoff",
//               new SimpleTimePeriod(date(2, Calendar.JUNE, 2001),
//                                    date(2, Calendar.JUNE, 2001))));
//        s1.add(new Task("Alpha Implementation",
//               new SimpleTimePeriod(date(3, Calendar.JUNE, 2001),
//                                    date(31, Calendar.JULY, 2001))));
//        s1.add(new Task("Design Review",
//               new SimpleTimePeriod(date(1, Calendar.AUGUST, 2001),
//                                    date(8, Calendar.AUGUST, 2001))));
//        s1.add(new Task("Revised Design Signoff",
//               new SimpleTimePeriod(date(10, Calendar.AUGUST, 2001),
//                                    date(10, Calendar.AUGUST, 2001))));
//        s1.add(new Task("Beta Implementation",
//               new SimpleTimePeriod(date(12, Calendar.AUGUST, 2001),
//                                    date(12, Calendar.SEPTEMBER, 2001))));
//        s1.add(new Task("Testing",
//               new SimpleTimePeriod(date(13, Calendar.SEPTEMBER, 2001),
//                                    date(31, Calendar.OCTOBER, 2001))));
//        s1.add(new Task("Final Implementation",
//               new SimpleTimePeriod(date(1, Calendar.NOVEMBER, 2001),
//                                    date(15, Calendar.NOVEMBER, 2001))));
//        s1.add(new Task("Signoff",
//               new SimpleTimePeriod(date(28, Calendar.NOVEMBER, 2001),
//                                    date(30, Calendar.NOVEMBER, 2001))));


	}
	
    private Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        System.out.println(result);
        return result;
    }
}
