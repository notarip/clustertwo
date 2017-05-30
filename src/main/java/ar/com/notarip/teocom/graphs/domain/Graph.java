package ar.com.notarip.teocom.graphs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Graph {

	@Id
	private Long id;
	private String name;
	private String type;
	private Double avgDegree;
	private Double avgClustering;
	private Double avgPathLength;
	private Long cNodes;
	private Long cEdges;
	
//	@Transient
//	private List<EdgeDTO> edges;
	

//	public Graph() {
//		// TODO Auto-generated constructor stub
//	}
//
//	
//	 public Long getId() {
//		return id;
//	}
//
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//
//	public String getName() {
//		return name;
//	}
//
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//
//	public Double getAvgDegree() {
//		return avgDegree;
//	}
//
//
//	public void setAvgDegree(Double avgDegree) {
//		this.avgDegree = avgDegree;
//	}
//
//
//	public Double getAvgClustering() {
//		return avgClustering;
//	}
//
//
//	public void setAvgClustering(Double avgClustering) {
//		this.avgClustering = avgClustering;
//	}
//
//
//	public Double getAvgPathLength() {
//		return avgPathLength;
//	}
//
//
//	public void setAvgPathLength(Double avgPathLength) {
//		this.avgPathLength = avgPathLength;
//	}
//
//
//	public Long getcNodes() {
//		return cNodes;
//	}
//
//
//	public void setcNodes(Long cNodes) {
//		this.cNodes = cNodes;
//	}
//
//
//	public Long getcEdges() {
//		return cEdges;
//	}
//
//
//	public void setcEdges(Long cEdges) {
//		this.cEdges = cEdges;
//	}
//
//
//	public List<EdgeDTO> getEdges() {
//		return edges;
//	}
//
//
//	public void setEdges(List<EdgeDTO> edges) {
//		this.edges = edges;
//	}
//
//
//	public String toString() {
//		   return ToStringBuilder.reflectionToString(this);
//		 }
//
//
//	public String getType() {
//		return type;
//	}
//
//
//	public void setType(String type) {
//		this.type = type;
//	}
//	
//	public List<String> getUniqNodes(){
//	
//		Set<String> nodes = new HashSet<String>();
//		for (EdgeDTO edge : edges) {
//			nodes.add(String.valueOf(edge.getSource()));
//			nodes.add(String.valueOf(edge.getTarget()));
//		}
//		
//		return Lists.newArrayList(nodes.iterator());
//		
//	}

}
