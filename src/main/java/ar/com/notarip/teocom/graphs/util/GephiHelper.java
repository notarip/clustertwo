package ar.com.notarip.teocom.graphs.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.api.Partition;
import org.gephi.appearance.api.PartitionFunction;
import org.gephi.appearance.plugin.PartitionElementColorTransformer;
import org.gephi.appearance.plugin.palette.Palette;
import org.gephi.appearance.plugin.palette.PaletteManager;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.generator.plugin.RandomGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingoldBuilder;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.notarip.teocom.graphs.domain.Country;
import ar.com.notarip.teocom.graphs.domain.Edge;
import ar.com.notarip.teocom.graphs.repository.CountryRepository;
import ar.com.notarip.teocom.graphs.repository.EdgeRepository;

@Service
public class GephiHelper {

	private static final String REGION = "region";

	@Autowired
	CountryRepository countryRepo;

	@Autowired
	EdgeRepository edgeRepo;

	public UndirectedGraph getGraph() {

		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		// Get a graph model - it exists because we have a workspace
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		graphModel.getNodeTable().addColumn(REGION, String.class);


		Container container = Lookup.getDefault().lookup(Container.Factory.class).newContainer();
		List<Edge> edges = edgeRepo.findAll();

		UndirectedGraph undirectedGraph = loadGraph(graphModel, edges);

		appearance(graphModel, undirectedGraph);
		
		 PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
		model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		//model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
		model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.1f));
		model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT,
		model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));

		FruchtermanReingold layout = new FruchtermanReingold(new FruchtermanReingoldBuilder());

		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		importController.process(container, new DefaultProcessor(), workspace);

		AutoLayout autoLayout = new AutoLayout(60, TimeUnit.SECONDS);
		autoLayout.setGraphModel(graphModel);

		autoLayout.addLayout(layout, 1f);
		autoLayout.execute();

		export((new Date()).toString() + "-graph1.pdf", workspace, pc);

		return undirectedGraph;
	}

	private void appearance(GraphModel graphModel, UndirectedGraph undirectedGraph) {
		
		
		AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
		AppearanceModel appearanceModel = appearanceController.getModel();
		Column column = graphModel.getNodeTable().getColumn(REGION);
		Function func = appearanceModel.getNodeFunction(undirectedGraph, column,
				PartitionElementColorTransformer.class);
		Partition partition = ((PartitionFunction) func).getPartition();
		Palette palette = PaletteManager.getInstance().generatePalette(partition.size());
		partition.setColors(palette.getColors());
		appearanceController.transform(func);
	}

	private void export2() {

		ExportController ec = Lookup.getDefault().lookup(ExportController.class);

		try {
			ec.exportFile(new File("test-full.gexf"));
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		// Export only visible graph
		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");
		// Get GEXF exporter
		exporter.setExportVisible(true);
		try {
			ec.exportFile(new File("test-visible.gexf"), exporter);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

	}

	private UndirectedGraph loadGraph(GraphModel graphModel, List<Edge> edges) {
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
		return undirectedGraph;
	}

	public void getGraphTest() {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Generate a new random graph into a container
		Container container = Lookup.getDefault().lookup(Container.Factory.class).newContainer();
		RandomGraph randomGraph = new RandomGraph();
		randomGraph.setNumberOfNodes(500);
		randomGraph.setWiringProbability(0.005);
		randomGraph.generate(container.getLoader());

		// Append container to graph structure
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		importController.process(container, new DefaultProcessor(), workspace);

		// See if graph is well imported
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
		DirectedGraph graph = graphModel.getDirectedGraph();
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());

		// Layout for 1 minute
		AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
		autoLayout.setGraphModel(graphModel);
		YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));
		ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
		AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
				.createDynamicProperty("forceAtlas.adjustSizes.name", Boolean.TRUE, 0.1f);// True
																							// after
																							// 10%
																							// of
																							// layout
																							// time
		AutoLayout.DynamicProperty repulsionProperty = AutoLayout
				.createDynamicProperty("forceAtlas.repulsionStrength.name", new Double(500.), 0f);// 500
																									// for
																									// the
																									// complete
																									// period
		autoLayout.addLayout(firstLayout, 0.5f);
		autoLayout.addLayout(secondLayout, 0.5f,
				new AutoLayout.DynamicProperty[] { adjustBySizeProperty, repulsionProperty });
		autoLayout.execute();

		export("export1.pdf", workspace, pc);

	}

	private void export(String name, Workspace workspace, ProjectController pc) {

		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		try {
			pc.openWorkspace(workspace);
			ec.exportFile(new File(name));

		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
			return;
		}

	}

	private Node buildNode(GraphModel graphModel, Country country) {
		Node node = graphModel.factory().newNode(country.getId().toString());
		node.setLabel(country.getName());
		node.setAttribute(REGION, country.getRegion());

		node.setSize(10);
		node.setX((float) ((0.01 + Math.random()) * 1000) - 500);
		node.setY((float) ((0.01 + Math.random()) * 1000) - 500);

		return node;
	}

	/*
	 * /** "gexf" "graphml"
	 * 
	 * @param filename
	 */
	public void save(String filename, String format) {
		File out = new File(filename);
		if (!out.getParentFile().exists())
			out.getParentFile().mkdirs();
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		GraphExporter exporter = (GraphExporter) ec.getExporter(format); // Get
																			// GraphML
																			// exporter
		exporter.setWorkspace(getWorkSpace());
		// exporter.setExportVisible(true); //Only exports the visible
		// (filtered) graph
		try {
			ec.exportFile(out, exporter);
			// ec.exportFile(new File(filename));

		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	private Workspace getWorkSpace() {

		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		pc.openWorkspace(workspace);
		return workspace;
	}
}
