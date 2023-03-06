package elements;

import FIPA.stringsHelper;
import jade.content.Concept;
import jade.content.onto.annotations.Element;

@Element(name="PRODUCT")
public class Product implements Concept {
	
	private String prodId;
	private String name;
	private String description;
//	private Product subProd;
//	private ProcessPlan procPlan;
	
	
	
	public String getProdId() {
		return prodId;	
	}
	
	public void setProdId(String prodId) {
		this.prodId = prodId;		
	}
	
	public String getName() {
		return name;	
	}
	
	public void setName(String name) {
		this.name = name;		
	}
	
	public String getDescription() {
		return description;	
	}
	
	public void setDescription(String description) {
		this.description = description;		
	}
	
	

}
