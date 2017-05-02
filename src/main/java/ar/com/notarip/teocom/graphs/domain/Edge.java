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
	
	@ManyToOne
	@JoinColumn(name="source")
	private Node source;
	
	@ManyToOne
	@JoinColumn(name="target")
	private Node target;
	
	@OneToOne
	@JoinColumn(name="dataset_id")
	private Data data;
	
	private String type;
	
	@Transient
	private String interval;
	
	public Edge() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
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

	public Edge (Node source, Node target, Data data, String type, String interval){
		this.source = source;
		this.target = target;
		this.data = data;
		this.type = type;
		this.interval = interval;
	}
	
	@Override
	public String toString() {
		return source + " -> " + target;
	}
	
	
}
