package behaviours;

import java.util.Hashtable;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WorkAcceptenceHandler extends CyclicBehaviour {

	private Hashtable catalogue;

	public WorkAcceptenceHandler(Hashtable catalogue) {
		this.catalogue = catalogue;
	}

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
		ACLMessage msg = myAgent.receive(mt); // receive accept proposal msg from TH agent
		
		if (msg != null) {
			// ACCEPT_PROPOSAL Message received. Process it
			System.out.println("Accept proposal message received from " + msg.getSender().getLocalName());
			String bestPrice = msg.getContent();
			ACLMessage reply = msg.createReply(); // create a reply to confirm to TH agent that the item is still available and will be sold

//			Integer price = (Integer) catalogue.remove(title);
			Integer price = (Integer) Integer.parseInt(bestPrice);
			if (price != null) {
				reply.setPerformative(ACLMessage.INFORM);
				System.out.println("Work sold to agent " + msg.getSender().getLocalName());
			} else {
				// The requested book has been sold to another buyer in the meanwhile .
				reply.setPerformative(ACLMessage.FAILURE);
				reply.setContent("not-available");
			}
			myAgent.send(reply);
//			myAgent.send(inform);
		} else {
			block();
		}
	}

}
