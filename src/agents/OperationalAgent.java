package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.lang.reflect.Array;
import java.util.*;

import behaviours.OperationalAgentBehaviour;


public class OperationalAgent extends Agent {

//	// The catalogue of books for sale (maps the title of a book to its price)
//	private Hashtable catalogue;
	
	// The list of known task agents
	private AID[] taskAgents;
	
	private String type;
	private String[] products;
	private int[] mfgTime;

	// Put agent initializations here
	protected void setup() {
		
		// 1. Printout a welcome message
		System.out.println("Operational agent: \""+getAID().getName()+"\" is ready.");
		
		// 2. get agent arguments
		Object[] args = getArguments();

		if (args != null && args.length > 0) {
			type = (String) args[0]; // OH type
			products = (String[]) args[1]; // type of products it can stack		
			mfgTime = (int[]) args[2]; // how long it takes to finish stacking
		}	
//		System.out.println(type);
//		System.out.println(Arrays.toString(products));
//		System.out.println(mfgTime);
		
		// 3. Register the OH agent services in the yellow pages
		register(type);
		
		// 4. Add the OH agent behaviour
		
		addBehaviour(new OperationalAgentBehaviour(this));



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
		System.out.println("Operational agent: \""+getAID().getName()+"\" terminating.");
	}

	// Registers agents services to the DF
	void register(String type)
	{
		DFAgentDescription dfd = new DFAgentDescription();
		// set service parameters
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName(getLocalName());
		dfd.setName(getAID());
//		// set service properties
//		Property p = new Property();
//		p.setName("product");
//		p.setValue(products);
//		sd.addProperties(p);

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
            System.out.println("\n\n\t"+"The msg from the DF is"+fe.getACLMessage().getContent()+"and the cause is "+fe.getCause());
		}
	}
	
	
}
