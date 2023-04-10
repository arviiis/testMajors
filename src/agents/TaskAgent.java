package agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ProductHolonGUI.WelcomeFrame;
import behaviours.WorkRequestPerformer;
import def.Main;
import jade.content.lang.sl.ExtendedSLParserConstants;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.tools.DummyAgent.DummyAgent;
import jade.core.behaviours.*;

public class TaskAgent extends Agent {

	// The list of known operational agents
	private AID[] availableAgents;
	private AID supervisorAgent;

	
	public List<Object> operationList = new ArrayList<>(); // list of task executors
	
	private String serviceType;
	private String requiredSkill;
	
	public int taTime = 0; // total time it takes to execute all the accepted tasks
//	private int requestInterval = 5000;

	protected void setup() {
		
		// 1. Get agent arguments passed from the PH agent
		Object[] args = getArguments();
		String type = args[0].toString(); // this returns the type of product this PH agent represents
		String[] productInfo = (String[]) args[1];// info about the product
		
		// 2. Printout a welcome message
		System.out.println("\nTask agent for product \"" + type + "\": \"" + getAID().getName() + "\" is ready.");

//		for (int i = 0; i < productInfo.length; i++) {
//			System.out.println(i + "th list member is: "+ productInfo[i]);
//		}
		
		// 3. Register the OH agent services in the yellow pages and create a task series for the TH agent
		register("task agent"); 
		Main.myChart.addTaskSeries(getAID().getLocalName()); // create Task Series with the TH name as description
		
		// 4. Get supervisor agent
		// returns one agent with the specified service
		supervisorAgent = getService("supervisor");
//		System.out.println("\nSupervisor: " + (supervisorAgent == null ? "not Found" : supervisorAgent.getName()));

		// 5. Add TH agent behaviour
		// Iterate over services and skills necessary and find appropriate sellers
		SequentialBehaviour THbehaviour = new SequentialBehaviour(this);
		System.out.println("");

		// add sequential behaviours so that the list of operations is made in the correct order
		for (int i = 0; i < productInfo.length; i=i+2) {

			// Set the type of service to look for(stacker, mover, wrapper)
			serviceType = productInfo[i]; // 0th, 2nd, 4th, etc.
			requiredSkill = productInfo[i+1]; // 1st, 3rd, 5th etc.
	
//			System.out.println("\n" + getAID().getLocalName() + " looking for service: " + serviceType);
//			System.out.println(getAID().getLocalName() + " negotiation start time: " + WelcomeFrame.startDate);
			
			// get all the agents that offer the service
			availableAgents = searchDF(serviceType);
			System.out.print(serviceType + " agents available for " + getAID().getLocalName() + ": ");
			for (int j = 0; j < availableAgents.length; j++)
				System.out.print(availableAgents[j].getLocalName() + ",  ");
			System.out.println();
			
			// request price offers from OH Agents
			THbehaviour.addSubBehaviour(new WorkRequestPerformer(this, availableAgents, requiredSkill));
			
//			addBehaviour(new WorkRequestPerformer(this, availableAgents, requiredSkill));
		}

		addBehaviour(THbehaviour);

		// run the behaviour periodically
//		addBehaviour(new TickerBehaviour(this, requestInterval) {
//			protected void onTick() {
//				System.out.println("\n" + getAID().getLocalName() + " looking for service: " + serviceType);
//				// get all the agents that offer the service
//				availableAgents = searchDF(serviceType);
//				System.out.print(serviceType + " agents available for " + getAID().getLocalName() + ": ");
//				for (int i = 0; i < availableAgents.length; i++)
//					System.out.print(availableAgents[i].getLocalName() + ",  ");
//				System.out.println();
//				// request price offers from OH Agents
//				addBehaviour(new WorkRequestPerformer(availableAgents, requiredSkill));
//			}
//		});

		
	}

	// what to do when agent dies
	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("Task agent: \"" + getAID().getName() + "\" terminating.");
		System.out.println(getAID().getLocalName() + " operational agents that execute the tasks: "+ operationList);
	}

	// return one agent with the specified service
	AID getService(String service) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		dfd.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			if (result.length > 0)
				return result[0].getName();
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return null;
	}

	// returns all the agents that provide the service
	public AID[] searchDF(String service) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		dfd.addServices(sd);

		SearchConstraints ALL = new SearchConstraints();
		ALL.setMaxResults(new Long(-1));

		try {
			DFAgentDescription[] result = DFService.search(this, dfd, ALL);
			AID[] agents = new AID[result.length];
			for (int i = 0; i < result.length; i++)
				agents[i] = result[i].getName();
			return agents;

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		return null;
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
