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
	private JButton btnOrderButton;
	private JTextField txtProdA;
	private JLabel lblProdB;
	private JTextField txtProdB;
	private JLabel lblProdC;
	private JTextField txtProdC;
	
	public static Date startDate;
	private JButton btnShowChart;
	
	public int qtyA;
	public int qtyB;
	public int qtyC;

//	public static GanttChartFrame myChart;
	
	// Create the frame.
	public WelcomeFrame() {
		
		// Initialise and create components
		initComponents();
		// Handle events
		createEvents();
		// show the GUI
		showGui();
		
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

		btnOrderButton = new JButton("Order");

		JLabel lblProdA = new JLabel("Product A:");
		lblProdA.setFont(new Font("Tahoma", Font.BOLD, 12));

		txtProdA = new JTextField();

		txtProdA.setColumns(10);

		lblProdB = new JLabel("Product B:");
		lblProdB.setFont(new Font("Tahoma", Font.BOLD, 12));

		txtProdB = new JTextField();
		txtProdB.setColumns(10);

		lblProdC = new JLabel("Product C:");
		lblProdC.setFont(new Font("Tahoma", Font.BOLD, 12));

		txtProdC = new JTextField();
		txtProdC.setColumns(10);
		
		btnShowChart = new JButton("Show chart");


		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblProdC, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblProdB, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblProdA))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(txtProdC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtProdB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtProdA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(42)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnShowChart)
								.addComponent(btnOrderButton))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProdA)
						.addComponent(txtProdA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProdB, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtProdB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProdC, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtProdC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnOrderButton)
					.addPreferredGap(ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
					.addComponent(btnShowChart)
					.addGap(49))
		);
		contentPane.setLayout(gl_contentPane);

	}

	// All the code for creating events
	private void createEvents() {

		btnOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String qtyProdA = txtProdA.getText();
				String qtyProdB = txtProdB.getText();
				String qtyProdC = txtProdC.getText();

				if (!isInteger(qtyProdA) || !isInteger(qtyProdB) || !isInteger(qtyProdC)) {
					JOptionPane.showMessageDialog(null, "Please insert integer value!");
				} else {
					qtyA = Integer.parseInt(qtyProdA);
					qtyB = Integer.parseInt(qtyProdB);
					qtyC = Integer.parseInt(qtyProdC);

					// What to do with Prod A value
					String displayString = "Prod A qty: " + qtyA + "\nProd B qty: " + qtyB + "\nProd C qty: " + qtyC;
					// shows what qty has been chosen
					JOptionPane.showMessageDialog(null, displayString);
					
					// create a time reference for OH agents
					startDate = new Date();

					// Create PH agents
					Jade.addPhAgent(qtyA, "A");
					Jade.addPhAgent(qtyB, "B");
					Jade.addPhAgent(qtyC, "C");
					
				}
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
