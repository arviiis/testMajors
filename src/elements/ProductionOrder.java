package elements;

import jade.content.onto.annotations.Element;

@Element(name="PRODUCTION_ORDER")
public class ProductionOrder {
	
	private String prodOrderId;
	private String clientOrderId;
	private int quantity;
	private int dueTime;
	
	
	public String getProdOrderId() {
		return prodOrderId;	
	}
	
	public void setProdOrderId(String prodOrderId) {
		this.prodOrderId = prodOrderId;		
	}
	
	public String getClientOrderId() {
		return clientOrderId;	
	}
	
	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;		
	}
	
	public int getQuantity() {
		return quantity;	
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;		
	}
	
	public int getDueTime() {
		return dueTime;	
	}
	
	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;		
	}


}
