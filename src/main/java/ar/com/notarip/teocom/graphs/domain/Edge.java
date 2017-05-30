package ar.com.notarip.teocom.graphs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity(name="edge")
public class Edge {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private Long source;
	
	private Long target;
	
	
	private String type;
	
	@Transient
	private String interval;

	


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

	public Edge() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	
	public Edge(Long countryId, Long countryId2) {
	
		this.source = countryId;
		this.target = countryId2;
		
	}

	@Override
	public String toString() {
		return source + " -> " + target;
	}
	
	
}
