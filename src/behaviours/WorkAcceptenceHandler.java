package behaviours;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TimerTask;

import agents.OperationalAgent;
import def.Main;
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
//			System.out.println("Accept proposal message received from: " + msg.getSender().getLocalName());		
			String requiredSkill = msg.getContent();
			ACLMessage reply = msg.createReply(); // create a reply to confirm to TH agent that the item is still available and will be sold
			Integer price = (Integer) opA.priceCatalogue.get(requiredSkill); // get the price that was offered 
//			System.out.println("Accepted proposal price: " + price);
					
			// Accept-proposal message means that it is the best price
			// check if the received required skill is still in OH agents catalogue, and it has a price
			if (price != null) {
				reply.setPerformative(ACLMessage.INFORM); // send an inform message to TH, to inform that the deal is done
//				System.out.println("Work sold to agent " + msg.getSender().getLocalName());
			
				List<Object> list = new ArrayList<Object>();// create a list {requiredSkill, estimatedEndTime}
				list.add(requiredSkill);
				list.add(price);
				
				opA.operationSequence.put(msg.getSender().getLocalName(), requiredSkill); // place the task in OH task list (th_name: requiredSkill)
				opA.hm.put(msg.getSender().getLocalName(), list);// place the task in OH task list (th_name: {requiredSkill, estimatedEndTime})
				
				// create a new task
				Main.myChart.addTask(myAgent.getLocalName(),msg.getSender().getLocalName(), price, (Integer) opA.catalogue.get(requiredSkill));
				
				System.out.println(myAgent.getLocalName() + " operation sequence: " + opA.operationSequence);
				
				opA.OHTime = price; // add to OH time, how long it takes to do the requiredSkill
				
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
