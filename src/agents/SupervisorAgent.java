package agents;

import jade.core.Agent;

public class SupervisorAgent extends Agent{
	
	   protected void setup(){
			
			// Printout a welcome message
			System.out.println("Supervisor agent: \""+getAID().getName()+"\" is ready.");

	    }

}
