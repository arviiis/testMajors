package agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SupervisorAgent extends Agent{
	
	   protected void setup(){
			
			// Printout a welcome message
			System.out.println("Supervisor agent: \""+getAID().getName()+"\" is ready.");
			
			// set service parameters and register with DF
			ServiceDescription sd = new ServiceDescription();
			
//			PropertiesDescription pd = new Properties
			sd.setType("supervisor");
			sd.setName(getLocalName());
			register(sd);
			
	    }
	   
		// Put agent clean-up operations here
		protected void takeDown() {
			// Deregister from the yellow pages
			try {
				DFService.deregister(this);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
			// Printout a dismissal message
			System.out.println("Supervisor agent: \""+getAID().getName()+"\" terminating.");
		}
	   
		// registers agents services to the DF
		void register(ServiceDescription sd)
		{
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());

			try {
				// tests if there are old duplicate DF entries before adding a new one
				DFAgentDescription list[] = DFService.search(this, dfd);
				if (list.length > 0)
					DFService.deregister(this);
				// add the new service
				dfd.addServices(sd);
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}

}
