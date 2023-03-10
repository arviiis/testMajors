package agents;

import def.Jade;
import jade.core.Agent;

public class ProductAgent extends Agent {

//	public ProductAgent() {
//		
//		// Gives the info about the product it represents to TH
//		// product type
//		// op_ids
//		// op name
//		// operaciju seciba
//		// requirements
//		// 	-type of OH
//		// 	-what type of boxes they can move
//		// time it takes to complete each operation

//		setup();
//		
// 	}

	public void setup() {
		
		// get agent arguments passed from the GUI
		Object[] args = getArguments();
		String arg1 = args[0].toString(); // this returns the type of product this PH agent represents
//		String arg2 = args[1].toString(); // optional arguments
//		String arg3 = args[2].toString(); 
		
		// Printout a welcome message
		System.out.println("Product \"" + arg1 + "\" agent: \"" + getAID().getName() + "\" is ready.");

//		Jade.addThAgent(1);

	}
}
