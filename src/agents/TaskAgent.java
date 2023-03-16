package agents;

import java.util.Arrays;
import java.util.List;

import behaviours.WorkRequestPerformer;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.behaviours.*;

public class TaskAgent extends Agent {

	// The list of known operational agents
	private AID[] availableAgents;
	private AID supervisorAgent;
//	private AID[] moverAgents;
//	private AID[] wrapperAgents;

	
	private String serviceType;
	private String requiredSkill;
	private int requestInterval = 5000;

	protected void setup() {

		// 1. Get agent arguments passed from the PH agent
		Object[] args = getArguments();
		String type = args[0].toString(); // this returns the type of product this PH agent represents
		String[] productInfo = (String[]) args[1];// info about the product

//		String arg3 = args[2].toString(); 

		
		
		// 2. Printout a welcome message
		System.out.println("Task agent for product \"" + type + "\": \"" + getAID().getName() + "\" is ready.");

		for (int i = 0; i < productInfo.length; i++) {
			System.out.println(i + "th list member is: "+ productInfo[i]);
		}
		
		// 3. Get supervisor agent
		// returns one agent with the specified service
		supervisorAgent = getService("supervisor");
//		System.out.println("\nSupervisor: " + (supervisorAgent == null ? "not Found" : supervisorAgent.getName()));

		// 4. Set the type of service to look for(stacker, mover, wrapper)
		serviceType = "stacker";
		requiredSkill = "pA";

		// 5. Add TH agent behaviour
//		addBehaviour(new WorkRequestPerformer());
		// run the behaviour periodically
		addBehaviour(new TickerBehaviour(this, requestInterval) {
			protected void onTick() {
				System.out.println("\n" + getAID().getLocalName() + " looking for service: " + serviceType);
				// get all the agents that offer the service
				availableAgents = searchDF(serviceType);
				System.out.print(serviceType + " agents available for " + getAID().getLocalName() + ": ");
				for (int i = 0; i < availableAgents.length; i++)
					System.out.print(availableAgents[i].getLocalName() + ",  ");
				System.out.println();
				// request price offers from OH Agents
				addBehaviour(new WorkRequestPerformer(availableAgents, requiredSkill));
			}
		});

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
	AID[] searchDF(String service) {
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

}
