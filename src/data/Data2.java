package data;

import de.re.easymodbus.modbusclient.ModbusClient;

public class Data2 {
	// define instance variables
	public int [] dataFromPLC;
	public int [] dataToPLC = new int[5];
	
	// Class constructor/ palaižas izsaucot klases instanci
//	public Data() throws Exception {
//		
//	}
	
	
//	public void dataExchange() throws Exception {
//		
//		// Connect to Modbus client
//		ModbusClient modbusClient = new ModbusClient("192.168.125.1", 502);
//		modbusClient.Connect();
//		// Check if the connection is successful
//		boolean d=modbusClient.isConnected();
//		if (d) {
//				System.out.println("Connection successful");
////				while (true) {
//										
//					try {
//					  // Receive data via Modbus TCP/IP
//					  // Read multiple registers with address 4000xx
//						int[] inputRegisters = modbusClient.ReadHoldingRegisters(0,6);
////						for (int i = 0; i <inputRegisters.length; i++) {
////							System.out.println("Input register #40000"+ (i+1) +": "+inputRegisters[i]);	
////						}
//					  // Read multiple coils
//						dataFromPLC = modbusClient.ReadCoils(0, 2);
////						for (int i = 0; i <ieejas.length; i++) {
////							System.out.println("Input register #00000"+ (i+1) +": "+ieejas[i]);	
////						}
//					  //Send data via Modbus TCP/IP
//					  //Write multiple registers 
////						dataToPLC = new int[5];
//						dataToPLC[0] = 2;
//						dataToPLC[1] = 164;
//						dataToPLC[2] = 0;
//						dataToPLC[3] = 57;
//						dataToPLC[4] = 6;
//						modbusClient.WriteMultipleRegisters(9, dataToPLC);
//					  //Write single coil
//						modbusClient.WriteSingleCoil(1, false);
//					}
//		
//					catch (Exception e){
//							System.out.println("error"+ e);
//					}  
////				}
//		}
//		
//
//	}
	

	
	
}
