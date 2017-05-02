package ar.com.notarip.teocom.graphs.domain;

public class EdgeDTO {

	
	private Long source;
	private Long target;
	private Double weight;
	private String type;
	
	
	public EdgeDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public EdgeDTO(Long source, Long target, Double weight, String type) {
		
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.type = type;
	
	}
	
	public Long getSource() {
		return source;
	}
	public void setSource(Long source) {
		this.source = source;
	}
	public Long getTarget() {
		return target;
	}
	public void setTarget(Long target) {
		this.target = target;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
