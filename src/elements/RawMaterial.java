package elements;

import jade.content.onto.annotations.Element;

@Element(name="RAW_MATERIAL")
public class RawMaterial {
	
	private String rmId;
	private String description;
	private String materialType;
//	private int X_dimension;
//	private int Y_dimension;
//	private int Z_dimension;

	
	
	
	public String getRmId() {
		return rmId;	
	}
	
	public void setRmId(String prodId) {
		this.rmId = rmId;		
	}
	
	public String getDescription() {
		return description;	
	}
	
	public void setDescription(String description) {
		this.description = description;		
	}
	
	public String getMaterialType() {
		return materialType;	
	}
	
	public void setMaterialType(String materialType) {
		this.materialType = materialType;		
	}
	
	

}
