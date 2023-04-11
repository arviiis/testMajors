package def;

import java.util.List;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Jade {

	static Runtime rt = Runtime.instance();
	static Profile p = new ProfileImpl();
	static ContainerController cc;
	static AgentController ac;

	int shAgent_qty = 1;
	

	public Jade() {
		runJade();
	}

	public void runJade() {
		// create Jade runtime instance
		// set profile parameters
		// create and start agents
		p.setParameter(Profile.MAIN_HOST, "localhost");
		p.setParameter(Profile.GUI, "true");
		p.setParameter(Profile.PLATFORM_ID, "Platform1");
		cc = rt.createMainContainer(p);

		// Adds SH agent to the runtime instance
		addShAgent(shAgent_qty);
		// Adds OH agents to the runtime instance
		addOhAgents();

	}

	// Create Product holon agent
	public static void addPhAgent(int id, String type) {

		String agentName = "PH Agent_" + type;

		Object[] args = new Object[3];
		args[0] = type; // one of 3 options: A, B, C.
		
		
		try {
			args[1] = (id); 
			ac = cc.createNewAgent(agentName + id, "agents.ProductAgent", args);
			ac.start();
		} catch (Exception spe) {
			spe.printStackTrace();
			System.out.println(spe.getMessage());
		}
	}

	// Create Task holon agent
	public static void addThAgent(int qty, String type, int phId, String[] productInfo) {

		String agentName = "TH Agent_" + type;

		Object[] args = new Object[3];
		args[0] = type; // one of 3 options: A, B, C.
		args[1] = productInfo;

		for (int i = 0; i < qty; i++) {
			try {
				// Add SH agent to the platform
				ac = cc.createNewAgent(agentName + phId, "agents.TaskAgent", args);
				// Start the agents
				ac.start();
			} catch (StaleProxyException spe) {
				// TODO: handle exception
				spe.printStackTrace();
				System.out.println(spe.getMessage());
			}
		}
	}

	// Create Supervisor holon agent
	static void addShAgent(int qty) {

		for (int i = 0; i < qty; i++) {
			try {
				// Add SH agent to the platform
				ac = cc.createNewAgent("SH agent_" + (i + 1), "agents.SupervisorAgent", null);
				// Start the agents
				ac.start();
			} catch (StaleProxyException spe) {
				// TODO: handle exception
				spe.printStackTrace();
				System.out.println(spe.getMessage());
			}
		}
	}

	// Create Operational holon agent
	static void addOhAgent(int id, String type, String[] products, int[] mfgTime) {

		Object[] args = new Object[3];
		args[0] = type;
		args[1] = products;
		args[2] = mfgTime;

		try {
			// Add SH agent to the platform
			ac = cc.createNewAgent(type + id, "agents.OperationalAgent", args);
			// Start the agents
			ac.start();
		} catch (StaleProxyException spe) {
			// TODO: handle exception
			spe.printStackTrace();
			System.out.println(spe.getMessage());
		}
	}

	// define the type of OH agents to add
	static void addOhAgents() {

		// products the OH agent has skills to work with
		String[] skills = { "none" };
		String[] skills1 = { "pA" };
		String[] skills2 = { "pB" };
		String[] skills3 = { "pC" };
		String[] skills4 = { "pA", "pB" };
		String[] skills5 = { "pA", "pB" };
		String[] skills6 = { "pC" };

		// how long it takes to complete each skill in seconds
		int[] mfgTime = { 0 };
		int[] mfgTime1 = { 20 };
		int[] mfgTime2 = { 30 };
		int[] mfgTime3 = { 40 };
		int[] mfgTime4 = { 30, 35 };
		int[] mfgTime5 = { 10, 20 };
		int[] mfgTime6 = { 15};

		addOhAgent(1, "stacker", skills1, mfgTime1);
		addOhAgent(2, "stacker", skills2, mfgTime2);
		addOhAgent(3, "stacker", skills3, mfgTime3);
		addOhAgent(4, "stacker", skills4, mfgTime4);
//		addOhAgent(7, "stacker", skills6, mfgTime6);

		addOhAgent(5, "wrapper", skills5, mfgTime5);
		addOhAgent(6, "wrapper", skills3, mfgTime5);
//		addOhAgent(6, "mover", skills, mfgTime);
		
//		System.out.println("");
	}
}
