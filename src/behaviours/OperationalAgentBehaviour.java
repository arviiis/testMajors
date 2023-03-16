package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;

public class OperationalAgentBehaviour extends CyclicBehaviour{
	
	Agent a;
	
	public OperationalAgentBehaviour(Agent a) {
		a = this.a;
	}
	
	
	public void action() {
		// We decide to stop the paraBehav as soon as one of its children is done.
		ParallelBehaviour OHbehaviour = new ParallelBehaviour(a,ParallelBehaviour.WHEN_ANY);
//		OHbehaviour.addSubBehaviour(new WorkAnnouncementHandler());
	}
}