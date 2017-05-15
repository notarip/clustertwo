package ar.com.notarip.teocom.graphs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.com.notarip.teocom.graphs.domain.Country;
import ar.com.notarip.teocom.graphs.repository.CountryRepository;
import ar.com.notarip.teocom.graphs.repository.GraphRepository;

@SpringBootApplication
public class Application implements CommandLineRunner{

	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private GraphRepository graphRepository;
    
    @Autowired
    private CountryRepository countryRepo;
    
    @Override
	public void run(String... arg0) throws Exception {
		
    	
    	List<Country> findAll = countryRepo.findAll();
    	
    	for (Country country : findAll) {
			System.out.println(country.getName());
			String name = country.getName().replaceAll(" ", "%20");
			String endpoint = "https://restcountries.eu/rest/v2/name/" + name;
			String region = null;
			try{
				region = RestHelper.resCall(endpoint);
			}catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			country.setRegion(region);
			countryRepo.save(country);
			
		}
    	
    	//Path path = Paths.get("src/main/resources/graphs");
    	//Stream<Path> files = Parser.getfiles(path);
    	
		/*
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
    	
    	*/
    	
    	//Node node = nodeRepository.findById(10L);
    	//List<Edge> findBySource = node.getEdgesOut();//edgeRepository.findBySource(10L);
		
    	//for (Edge edge : findBySource) {
		//	log.info(edge.toString());
		//}
    	
	}
}
