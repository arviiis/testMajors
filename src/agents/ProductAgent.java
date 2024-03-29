package agents;

import java.util.ArrayList;
import java.util.List;

import def.Jade;
import def.Main;
import jade.core.AID;
import jade.core.Agent;

public class ProductAgent extends Agent {
	// Gives the info about the product it represents to TH
	// - Service type
	// - skills to work with specific products
	// - sequence in which the services need to be executed (determined by place in list)
	
	// info about the product A (service, type of skill, sequence)
//	private String[] productAInfo = {"stacker", "pA"};
	private String[] productAInfo = {"stacker", "pA", "stacker", "pB", "wrapper", "pA"};
	private String[] productBInfo = {"stacker", "pB", "wrapper", "pB"};
	private String[] productCInfo = {"stacker", "pC", "wrapper", "pC"};
	

	public void setup() {

		// get agent arguments passed from the GUI
		Object[] args = getArguments();
		String pType = args[0].toString(); // this returns the type of product this PH agent represents
		int phId = (int) args[1]; // to avoid name clash
//		String arg3 = args[2].toString(); 
		
		// Printout a welcome message
		System.out.println("\nProduct \"" + pType + "\" agent: \"" + getAID().getName() + "\" is ready.");
		
		// Give TH agent info about the product it represents
		if (pType == "A") {
			Jade.addThAgent(1, pType, phId, productAInfo);
		}
		else if (pType == "B") {
			Jade.addThAgent(1, pType, phId, productBInfo);
		}
		else if (pType == "C") {
			Jade.addThAgent(1, pType, phId, productCInfo);
		}



	}
}
