package ar.com.notarip.teocom.graphs.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingoldBuilder;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.notarip.teocom.graphs.domain.Country;
import ar.com.notarip.teocom.graphs.domain.Edge;
import ar.com.notarip.teocom.graphs.repository.CountryRepository;
import ar.com.notarip.teocom.graphs.repository.EdgeRepository;

@Service
public class GephiHelper {

	@Autowired
	CountryRepository countryRepo;

	@Autowired
	EdgeRepository edgeRepo;

	public  UndirectedGraph getGraph() {

		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		// Get a graph model - it exists because we have a workspace
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
		graphModel.getNodeTable().addColumn("region", String.class);
		//graphModel.
		
		
		List<Edge> edges = edgeRepo.findAll();

		UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

		for (Edge edge : edges) {

			Long sourceId = edge.getSource();
			Long targetId = edge.getTarget();

			Country sourceCountry = countryRepo.findById(sourceId);
			Country targetCountry = countryRepo.findById(targetId);
			
			Node source = buildNode(graphModel, sourceCountry);
			Node target = buildNode(graphModel, targetCountry);
			undirectedGraph.getNode(targetId);
			try {
				if (undirectedGraph.getNode(sourceCountry.getId().toString()) == null)
					undirectedGraph.addNode(source);
				else
					source = undirectedGraph.getNode(sourceCountry.getId().toString());

				if (undirectedGraph.getNode(targetCountry.getId().toString()) == null)
					undirectedGraph.addNode(target);
				else
					target = undirectedGraph.getNode(targetCountry.getId().toString());

			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}


			undirectedGraph.addEdge(graphModel.factory().newEdge(source, target, 1, false));

		}

		// Count nodes and edges
		System.out.println("Nodes: " + undirectedGraph.getNodeCount() + " Edges: " + undirectedGraph.getEdgeCount());

//		for (Node n : undirectedGraph.getNodes()) {
//			Node[] neighbors = undirectedGraph.getNeighbors(n).toArray();
//			System.out.println(n.getLabel() + " has " + neighbors.length + " neighbors");
//		}

		// Iterate over edges
//		for (org.gephi.graph.api.Edge e : undirectedGraph.getEdges()) {
//			System.out.println(e.getSource().getId() + " -> " + e.getTarget().getId());
//		}

		// Find node by id
		Node node2 = undirectedGraph.getNode("1");

		// Get degree
		System.out.println("Node2 degree: " + undirectedGraph.getDegree(node2));

		
		FruchtermanReingold layout = new FruchtermanReingold(new FruchtermanReingoldBuilder());
		AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
		autoLayout.setGraphModel(graphModel);
		autoLayout.addLayout(layout, 1.0f);
		autoLayout.execute();
		
		
		return undirectedGraph;

	}

	private Node buildNode(GraphModel graphModel, Country country) {
		Node node = graphModel.factory().newNode(country.getId().toString());
		node.setLabel(country.getName());
		node.setAttribute("region", country.getRegion());
		
		return node;
	}
	
	
	/*
	 * /**
     * "gexf" 
     * "graphml" 
     * @param filename 
     */ 
	public void save(String filename, String format){
	    File out = new File(filename);
	    if(!out.getParentFile().exists())
	        out.getParentFile().mkdirs();
	    ExportController ec = Lookup.getDefault().lookup(ExportController.class);
	    GraphExporter exporter = (GraphExporter) ec.getExporter(format);     //Get GraphML exporter
	    exporter.setWorkspace(getWorkSpace());
	 // exporter.setExportVisible(true);  //Only exports the visible (filtered) graph
	    try {
	        ec.exportFile(out, exporter);
	    //    ec.exportFile(new File(filename));
	        
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return;
	    }
	}
	
	
	
	private Workspace getWorkSpace(){
		
	      	ProjectController pc = Lookup.getDefault().lookup(ProjectController.class); 
	        pc.newProject(); 
	        Workspace workspace = pc.getCurrentWorkspace(); 
	        pc.openWorkspace(workspace); 
	        return workspace; 
	}
}
