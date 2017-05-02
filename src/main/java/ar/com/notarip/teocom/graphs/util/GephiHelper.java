package ar.com.notarip.teocom.graphs.util;

import java.util.List;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import ar.com.notarip.teocom.graphs.domain.EdgeDTO;
import ar.com.notarip.teocom.graphs.domain.Graph;

public class GephiHelper {

	public static UndirectedGraph getGraph(Graph graph) {

		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		// Get a graph model - it exists because we have a workspace
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();

		List<String> nodesNames = graph.getUniqNodes();

		UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

		for (EdgeDTO edge : graph.getEdges()) {
			Node source = graphModel.factory().newNode(edge.getSource().toString());
			Node target = graphModel.factory().newNode(edge.getTarget().toString());
			
			try{
				if (undirectedGraph.getNode(edge.getSource().toString()) == null)
					undirectedGraph.addNode(source);
				else
					source = undirectedGraph.getNode(edge.getSource().toString());
				
				if (undirectedGraph.getNode(edge.getTarget().toString()) == null)
					undirectedGraph.addNode(target);
				else
					target = undirectedGraph.getNode(edge.getTarget().toString());
			
			
			}catch(Exception e){
				System.out.println(e.getStackTrace());
			}
			
			Edge newEdge = graphModel.factory().newEdge(source,	target, 1, false);

			undirectedGraph.addEdge(newEdge);
		}

		// Count nodes and edges
		System.out.println("Nodes: " + undirectedGraph.getNodeCount() + " Edges: " + undirectedGraph.getEdgeCount());

		for (Node n : undirectedGraph.getNodes()) {
			Node[] neighbors = undirectedGraph.getNeighbors(n).toArray();
			System.out.println(n.getLabel() + " has " + neighbors.length + " neighbors");
		}

		// Iterate over edges
		for (Edge e : undirectedGraph.getEdges()) {
			System.out.println(e.getSource().getId() + " -> " + e.getTarget().getId());
		}

		// Find node by id
		Node node2 = undirectedGraph.getNode(1L);

		// Get degree
		System.out.println("Node2 degree: " + undirectedGraph.getDegree(node2));
		
		return undirectedGraph;

	}

}
