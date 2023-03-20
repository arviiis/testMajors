package behaviours;

import java.util.Date;
import java.util.Hashtable;
import java.util.TimerTask;

import agents.OperationalAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import schedule.MyTimerTask;

public class WorkAcceptenceHandler extends CyclicBehaviour {
	
	OperationalAgent opA;

	public WorkAcceptenceHandler(OperationalAgent opA) {
		this.opA = opA;
	}

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
		ACLMessage msg = myAgent.receive(mt); // receive accept proposal msg from TH agent
		
		if (msg != null) {
			// ACCEPT_PROPOSAL Message received. Process it
			System.out.println("Accept proposal message received from: " + msg.getSender().getLocalName());		
			String requiredSkill = msg.getContent();
			ACLMessage reply = msg.createReply(); // create a reply to confirm to TH agent that the item is still available and will be sold
			Integer price = (Integer) opA.priceCatalogue.get(requiredSkill); // get the price that was offered 
			System.out.println("Accepted proposal price: " + price);
			// Accept-proposal message means that it is the best price
			// check if the received required skill is still in OH agents catalogue, and it has a price
			if (price != null) {
				reply.setPerformative(ACLMessage.INFORM); // send an inform message to TH, to inform that the deal is done
				System.out.println("Work sold to agent " + msg.getSender().getLocalName());
				
//		        TimerTask timerTask1 = new MyTimerTask((Integer) opA.catalogue.get(requiredSkill));
//		        opA.timer.schedule(timerTask1, 20*1000);
//		        System.out.println("TimerTask1 scheduled placed in the schedule at "+ new Date());
				
				
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
