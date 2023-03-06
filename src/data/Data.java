package data;

import de.re.easymodbus.modbusclient.ModbusClient;

public class Data {
	
	public int [] getData() throws Exception {
		
		// Connect to Modbus client
		ModbusClient modbusClient = new ModbusClient("192.168.125.1", 502);
		modbusClient.Connect();
		boolean d=modbusClient.isConnected();
		if (d) {
			System.out.println("Connection successful");
			int [] dataFromPLC = modbusClient.ReadHoldingRegisters(0,11);
//			for (int i = 0; i <dataFromPLC.length; i++) {
//			System.out.println("Input register #40000"+ (i+1) +": "+dataFromPLC[i]);
//			}
			return dataFromPLC;		
		}
		else {
			return null;
		}
	}
	
	public String setData(int [] dataToPLC) throws Exception {
		
		// Connect to Modbus client
		ModbusClient modbusClient = new ModbusClient("192.168.125.1", 502);
		modbusClient.Connect();
		boolean d=modbusClient.isConnected();
		if (d) {
			System.out.println("Connection successful");
			//Send data via Modbus TCP/IP
			modbusClient.WriteMultipleRegisters(12, dataToPLC);
			return "Data sent!";		
		}
		else {
			return "Connection failed";
		}
	}
	
}
