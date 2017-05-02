package ar.com.notarip.teocom.graphs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.gephi.graph.api.UndirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.com.notarip.teocom.graphs.domain.Edge;
import ar.com.notarip.teocom.graphs.domain.Graph;
import ar.com.notarip.teocom.graphs.domain.Node;
import ar.com.notarip.teocom.graphs.repository.EdgeRepository;
import ar.com.notarip.teocom.graphs.repository.GraphRepository;
import ar.com.notarip.teocom.graphs.repository.NodeRepository;
import ar.com.notarip.teocom.graphs.util.GephiHelper;
import ar.com.notarip.teocom.graphs.util.Parser;

@SpringBootApplication
public class Application implements CommandLineRunner{

	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private GraphRepository graphRepository;
    
    @Override
	public void run(String... arg0) throws Exception {
		
    	
    	Path path = Paths.get("src/main/resources/graphs");
    	Stream<Path> files = Parser.getfiles(path);
    	
		files.forEach(file -> {
			Graph graph;
			try {
				graph = Parser.proccess(file);
				UndirectedGraph graph2 = GephiHelper.getGraph(graph);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
    	
    	
    	
    	//Node node = nodeRepository.findById(10L);
    	//List<Edge> findBySource = node.getEdgesOut();//edgeRepository.findBySource(10L);
		
    	//for (Edge edge : findBySource) {
		//	log.info(edge.toString());
		//}
    	
	}
}
