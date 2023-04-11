package ProductHolonGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chart.GanttChartFrame;
import def.Jade;
import def.Main;
import jade.core.AID;
import jade.wrapper.StaleProxyException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WelcomeFrame extends JFrame {

	private JPanel contentPane;
	
	public static Date startDate;
	private JButton btnShowChart;
	
	public int qtyA;
	public int qtyB;
	public int qtyC;
	private JButton btnAddpA;
	private JButton btnAddpB;
	private JButton btnAddpC;
	private JLabel lblQtyA;
	private JLabel lblQtyB;
	private JLabel lblQtyC;

//	public static GanttChartFrame myChart;
	
	// Create the frame.
	public WelcomeFrame() {
		
		// Initialise and create components
		initComponents();
		// Handle events
		createEvents();
		// show the GUI
		showGui();
		
		startDate = new Date();
		
	}

	// create and initialize components
	private void initComponents() {

		setTitle("Product Holon GUI");
		setIconImage(Toolkit.getDefaultToolkit().getImage(WelcomeFrame.class.getResource("/ProductHolonGUI/home.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 418, 521);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		btnShowChart = new JButton("Show chart");
		
		btnAddpA = new JButton("Add A");
		
		
		btnAddpB = new JButton("Add B");

		
		btnAddpC = new JButton("Add C");
		
		lblQtyA = new JLabel("0");
		
		lblQtyB = new JLabel("0");
		
		lblQtyC = new JLabel("0");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(btnAddpA)
							.addGap(18)
							.addComponent(lblQtyA))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(btnAddpB)
							.addGap(18)
							.addComponent(lblQtyB))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(btnAddpC)
							.addGap(18)
							.addComponent(lblQtyC))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnShowChart)))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddpA)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(lblQtyA)))
					.addGap(20)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddpB)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(lblQtyB)))
					.addGap(20)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddpC)
						.addComponent(lblQtyC))
					.addGap(28)
					.addComponent(btnShowChart)
					.addContainerGap(337, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);

	}

	// All the code for creating events
	private void createEvents() {
		
		btnAddpA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qtyA +=1;
				Jade.addPhAgent(qtyA, "A");
				lblQtyA.setText(Integer.toString(qtyA));
			}
		});
		
		btnAddpB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qtyB +=1;
				Jade.addPhAgent(qtyB, "B");
				lblQtyB.setText(Integer.toString(qtyB));
			}
		});
		
		btnAddpC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qtyC +=1;
				Jade.addPhAgent(qtyC, "C");
				lblQtyC.setText(Integer.toString(qtyC));
			}
		});
		
		btnShowChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.myChart.showFrame();
			}
		});
	}

	// Checks if the value input is Integer
	public boolean isInteger(String txt) {
		try {
			int intValue = Integer.parseInt(txt);
			return true;
		} catch (NumberFormatException nfe) {
			System.out.println("Please insert integer value!");
			return false;
		}
	}

	// Displays the created GUI
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
}
