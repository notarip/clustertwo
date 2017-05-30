package ar.com.notarip.teocom.graphs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="dataset")
public class DataSet {

	@Id
	private Long id;
	private String name;
	private double min;
	private double max;
	private double percent;

	public DataSet() {
	
	}
	
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
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	
	
	
	
}
