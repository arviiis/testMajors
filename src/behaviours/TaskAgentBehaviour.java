package behaviours;

import java.lang.reflect.Constructor;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.introspection.AddedBehaviour;

public class TaskAgentBehaviour extends CyclicBehaviour{
	
	Agent a;
	
	public TaskAgentBehaviour(Agent a) {
		a = this.a;
		System.out.println(a);
	}
	
	public void action() {

	}


}
