package def;

import java.util.Arrays;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;

import ProductHolonGUI.WelcomeFrame;
import agents.ProductAgent;
import de.re.easymodbus.modbusclient.ModbusClient;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import def.Jade;
import data.Data;
import data.Data2;

import ProductHolonGUI.*;

public class Main {

	private static WelcomeFrame myGui;
	
	
	public static void main(String[] args) throws Exception {
	
//		// Get and set data in PLC
//		int [] dataFrom;
//		int [] dataTo = {1,3,5,7,9,11,13,15,17,19};
//		Data exchangeData = new Data();

//		// Receive data from PLC
//		dataFrom = exchangeData.getData();
//		System.out.println(Arrays.toString(dataFrom));
//		
//		// Send data to PLC
//		exchangeData.setData(dataTo);		
		
		
 		// Create Jade runtime instance
		Jade run_Jade = new Jade();
		
//		// Start a product choice GUI
		myGui = new WelcomeFrame();
//		ProductAgent PH_agent = new ProductAgent();


	}
	

}
