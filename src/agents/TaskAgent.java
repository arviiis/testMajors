package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class TaskAgent extends Agent {
	
	// The list of known operational agents
	private AID[] stackerAgents;
	private AID[] moverAgents;
	private AID[] wrapperAgents;

	protected void setup() {

		// Printout a welcome message
		System.out.println("Task agent: \"" + getAID().getName() + "\" is ready.");
		
		// returns one agent with the specified service
		AID agent = getService("supervisor");
		System.out.println("\nSupervisor: " + (agent == null ? "not Found" : agent.getName()));

		agent = getService("stacker");
		System.out.println("\nStacker: " + (agent == null ? "not Found" : agent.getName()));
		
		// returns all the agents with the specified service
		AID[] buyers = searchDF("stacker");
		System.out.print("\nSTACKERS: ");
		for (int i = 0; i < buyers.length; i++)
			System.out.print(buyers[i].getLocalName() + ",  ");
		System.out.println();

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
		System.out.println("Task agent: \"" + getAID().getName() + "\" terminating.");
	}

	// returns one search result
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
