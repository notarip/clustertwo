package ar.com.notarip.teocom.graphs.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class Node {

	@Id
	private Long id;
	private String name;
	
	@OneToMany(fetch = FetchType.EAGER,  mappedBy="target", cascade=CascadeType.ALL)
	private List<Edge> edgesIn;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="source", cascade=CascadeType.ALL)
	private List<Edge> edgesOut;
	
    
	/**
	 * 
	 */
	public Node() {
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

	public List<Edge> getEdgesIn() {
		return edgesIn;
	}

	public void setEdgesIn(List<Edge> edgesIn) {
		this.edgesIn = edgesIn;
	}

	public List<Edge> getEdgesOut() {
		return edgesOut;
	}

	public void setEdgesOut(List<Edge> edgesOut) {
		this.edgesOut = edgesOut;
	}

	public Node(Long id, String name) {
		this.id = id;
		this.name= name;
		
	}
	
	@Override
	public String toString() {
		return id.toString() + " " + name;
	}

}
