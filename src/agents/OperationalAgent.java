package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import schedule.MyTimerTask;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.lang.reflect.Array;
import java.util.*;

import behaviours.InfoAboutWinningOH;
import behaviours.OperationalAgentBehaviour;
import behaviours.WorkAcceptenceHandler;
import behaviours.WorkAnnouncementHandler;

public class OperationalAgent extends Agent {

	public Hashtable catalogue; // catalogue of agents capabilities (product: mfgTime). How long it takes to complete each task
	public Hashtable priceCatalogue;// catalogue with the prices offered for each capability
	public Hashtable bestPriceCatalogue;// catalogue with the best prices (winning prices) for each capability
	
	public Hashtable operationSequence; // tA name: skill, time
	public Timer timer; // timer that keeps track of OH schedule

//	// The list of known task agents
//	private AID[] taskAgents;
	private String serviceType;
	private String[] skills; // products that this service type OH agent has skills to work with
	private int[] mfgTime;
	
	public static int priceA;
	public String requiredSkill;
	
	// Put agent initialisations here
	protected void setup() {
		
		// initialise hashtables
		catalogue = new Hashtable(); 
		priceCatalogue = new Hashtable(); 
		bestPriceCatalogue = new Hashtable();
		
		// initialise timer as deamon thread
		timer = new Timer(true);

		// 1. get agent arguments (set when creating the agent)
		Object[] args = getArguments();

		if (args != null && args.length > 0) {
			serviceType = (String) args[0]; // OH type
			skills = (String[]) args[1]; // type of products it can work with
			mfgTime = (int[]) args[2]; // how long it takes to finish the task
		}
		
		// 2. Update agents catalogue with products it can work with and time it takes to finish the task
		for (int i = 0; i < skills.length; i++) {
			catalogue.put((String) skills[i],new Integer (mfgTime[i]));
		}
		
		// 3. Printout a welcome message
		System.out.println("Operational  agent: \"" + getAID().getName() + "\" is ready."
				+ "\nOffered service: \"" + serviceType + "\"\n"
						+ "Skills: " +catalogue);
		
		// 4. Register the OH agent services in the yellow pages
		register(serviceType);

		// 5. Add the OH agent behaviour
//		addBehaviour(new OperationalAgentBehaviour(this));
		
		// Handle the work offers issued by TH Agent
		addBehaviour(new WorkAnnouncementHandler(this, skills));
		// What to do once an accept proposal from TH agent has been received
		addBehaviour(new WorkAcceptenceHandler(this));
		// receive inform messages about the offer that won from other OH agents
		addBehaviour(new InfoAboutWinningOH(this));
		
		
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
		System.out.println("Operational agent: \"" + getAID().getName() + "\" terminating.");
		
		System.out.println("Price catalogue: "+ priceCatalogue);
		System.out.println("Best price catalogue: "+ bestPriceCatalogue);
	}

	// Registers agents services to the DF
	void register(String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
		// set service parameters
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
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
			System.out.println("\n\n\t" + "The msg from the DF is" + fe.getACLMessage().getContent()
					+ "and the cause is " + fe.getCause());
		}
	}

}
