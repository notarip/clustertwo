package ar.com.notarip.teocom.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.com.notarip.teocom.graphs.domain.Country;
import ar.com.notarip.teocom.graphs.domain.DataSet;
import ar.com.notarip.teocom.graphs.domain.Edge;
import ar.com.notarip.teocom.graphs.domain.Score;
import ar.com.notarip.teocom.graphs.repository.CountryRepository;
import ar.com.notarip.teocom.graphs.repository.DataSetRepository;
import ar.com.notarip.teocom.graphs.repository.EdgeRepository;
import ar.com.notarip.teocom.graphs.repository.GraphRepository;
import ar.com.notarip.teocom.graphs.repository.ScoreRepository;
import ar.com.notarip.teocom.graphs.util.GephiHelper;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private GraphRepository graphRepository;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private ScoreRepository scoreRepo;
	
	@Autowired
	private DataSetRepository datasetRepo;
	
	@Autowired
	private EdgeRepository edgeRepo;
	
	@Autowired
	private GephiHelper gephi;
	
	@Override
	public void run(String... arg0) throws Exception {
    	
    	List<String> datasets = new ArrayList<String>();
    	String year = null;
    	int i = 0;
    	try{
	    	for (String param : arg0) {
				
	    		if (param.equals("-d")){
	    			String datas = arg0[i+1];
	    			String[] split = datas.split(",");
	    			datasets.addAll(Arrays.asList(split));
	    		}
	    		if(param.equals("-y")){
	    			year = arg0[i+1];
	    			
	    		}
	    		i++;
	    	}
    	}catch (Exception e) {
			System.out.println("error en los parametros de entrada -d [datasets by comma] -y [year]");
		}
    	
    	
    	//createAdyansencyMatrix(datasets, year);
    	gephi.getGraph();
    	
    	
	}

	private void createAdyansencyMatrix(List<String> datasets, String year) {

		String msg = "creating adyancense matrix with datasets %s and year %s";
		msg = String.format(msg, datasets.toString(), year);
		log.info(msg);
		
		edgeRepo.deleteAll();
		
		for (String datasetId : datasets) {

			Long dataSetId = Long.valueOf(datasetId);
			Integer iYear = Integer.valueOf(year);
			DataSet dataSet = datasetRepo.findById(dataSetId);
			double delta = (dataSet.getMax() - dataSet.getMin())/100*dataSet.getPercent();
			
			List<Score> scores = scoreRepo.findByDatasetIdAndYear(dataSetId, iYear);
			for (Score score : scores) {
				double from = score.getScore()- delta;
				double to = score.getScore() + delta;
				List<Score> scoresRelated = scoreRepo.findByScoreBetweenFromToAndDatasourceIdAndYear(from, to, dataSetId, iYear);
				createEdges(score, scoresRelated);
				
			}
		}
		
		log.info("adyancense matrix created");

	}

	private void createEdges(Score score, List<Score> scoresRelated) {
		
		for (Score score2 : scoresRelated) {
			
			if(score.getCountryId() != score2.getCountryId()){
			
				Edge edge = new Edge(score.getCountryId(), score2.getCountryId());
			
				edgeRepo.save(edge);			
			}
		}
	}

	public void fillContinents() {

		List<Country> findAll = countryRepo.findAll();

		for (Country country : findAll) {
			System.out.println(country.getName());
			String name = country.getName().replaceAll(" ", "%20");
			String endpoint = "https://restcountries.eu/rest/v2/name/" + name;
			String region = null;
			try {
				region = RestHelper.resCall(endpoint);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			country.setRegion(region);
			countryRepo.save(country);

		}

	}

	// Path path = Paths.get("src/main/resources/graphs");
	// Stream<Path> files = Parser.getfiles(path);

	/*
	 * files.forEach(file -> { Graph graph; try { graph = Parser.proccess(file);
	 * UndirectedGraph graph2 = GephiHelper.getGraph(graph); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * }
	 * 
	 * });
	 * 
	 */

	// Node node = nodeRepository.findById(10L);
	// List<Edge> findBySource =
	// node.getEdgesOut();//edgeRepository.findBySource(10L);

	// for (Edge edge : findBySource) {
	// log.info(edge.toString());
	// }

}
