package ar.com.notarip.teocom.graphs.domain;

import javax.persistence.Id;


public class Data {
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Double getMin() {
		return min;
	}


	public void setMin(Double min) {
		this.min = min;
	}


	public Double getMax() {
		return max;
	}


	public void setMax(Double max) {
		this.max = max;
	}


	public Double getPercent() {
		return percent;
	}


	public void setPercent(Double percent) {
		this.percent = percent;
	}


	@Id
	private Long id;
	private String name;
	private Double min;
	private Double max;
	private Double percent;
	
	public Data() {
		// TODO Auto-generated constructor stub
	}
	
	public Data(Long id, String name, Double min, Double max, Double percent) {

		this.id = id;
		this.name = name;
		this.min = min;
		this.max = max;
		this.percent = percent;
	}
	

	
}
