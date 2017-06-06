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
import ar.com.notarip.teocom.graphs.repository.ScoreRepository;
import ar.com.notarip.teocom.graphs.util.GephiHelper;
import ar.com.notarip.teocom.graphs.util.RestHelper;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

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

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		createGraph(arg0);

		// gephi.getGraphTest();
	}

	private void createGraph(String... arg0) throws Exception {

		List<String> datasets = new ArrayList<String>();
		String year = null;
		Boolean force = Boolean.FALSE;
		int i = 0;
		try {
			for (String param : arg0) {

				if (param.equals("-d")) {
					String datas = arg0[i + 1];
					String[] split = datas.split(",");
					datasets.addAll(Arrays.asList(split));
				}
				if (param.equals("-y")) {
					year = arg0[i + 1];

				}
				if (param.equals("-f")) {
					force = Boolean.TRUE;

				}
				i++;
			}
		} catch (Exception e) {
			log.error("error en los parametros de entrada -d [datasets by comma] -y [year] [-f]");
			throw e;
		}

		if (!datasets.isEmpty() && !year.isEmpty()) {
			
			List<Long> datas = listToLong(datasets);
			Long lYear = Long.valueOf(year);

			createAdyansencyMatrix(datas, lYear, force);
			gephi.generateGraph(datas, lYear);
			
		} else {
			
			log.error("error en los parametros de entrada -d [datasets by comma] -y [year] [-f]");
			
		}

	}

	private List<Long> listToLong(List<String> datasets) {

		List<Long> datas = new ArrayList<>();
		datasets.forEach(s -> {
			datas.add(Long.valueOf(s));
			});
		return datas;
		
	}

	private void createAdyansencyMatrix(List<Long> datasets, Long year, Boolean force) {

		if (!datasets.isEmpty()) {

			String msg = "creating adyancense matrix with datasets %s and year %s";
			msg = String.format(msg, datasets.toString(), year);
			log.info(msg);
			
			if(force) 
				edgeRepo.deleteByDataSetIdInAndYear(datasets, year);

			for (Long datasetId : datasets) {

				Long dataSetId = Long.valueOf(datasetId);
				
				DataSet dataSet = datasetRepo.findById(dataSetId);
				double delta = (dataSet.getMax() - dataSet.getMin()) / 100 * dataSet.getPercent();

				List<Score> scores = scoreRepo.findByDatasetIdAndYear(dataSetId, year);
				for (Score score : scores) {
					double from = score.getScore() - delta;
					double to = score.getScore() + delta;
					List<Score> scoresRelated = scoreRepo.findByScoreBetweenFromToAndDatasourceIdAndYear(from, to,
							dataSetId, year);
					createEdges(score, scoresRelated, dataSetId, year);

				}
			}
			log.info("adyancense matrix created");
		}

	}

	private void createEdges(Score score, List<Score> scoresRelated, Long dataSetId, Long year) {

		for (Score score2 : scoresRelated) {

			Long source = score.getCountryId();
			Long target = score2.getCountryId();
			Long lYear= Long.valueOf(year);
			
			if (source != target) {

				Edge edge = new Edge(source, target);
				edge.setDataSetId(dataSetId);
				
				edge.setYear(lYear);
				
				Edge existentEdge = edgeRepo.findBySourceAndTargetAndDataSetIdAndYear(source, target, dataSetId, lYear);
				
				if(existentEdge == null)
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


}
