package elements;

import jade.content.onto.annotations.Element;

@Element(name="WORK_ORDER")
public class WorkOrder {

	private String woId;
	private int quantity;
	private String state;
	private int priority;
	
	////////////////////////////////////////
	public String getWoId() {
		return woId;	
	}
	
	public void setWoId(String woId) {
		this.woId = woId;		
	}
	///////////////////////////////////////
	public int getQuantity() {
		return quantity;	
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;		
	}
	/////////////////////////////////////////
	public String getState() {
		return state;	
	}
	
	public void setState(String state) {
		this.state = state;		
	}
	////////////////////////////////////////

	public int getPriority() {
		return priority;	
	}
	
	public void setPriority(int priority) {
		this.priority = priority;		
	}

}
