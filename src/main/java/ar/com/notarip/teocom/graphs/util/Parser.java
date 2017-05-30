package ar.com.notarip.teocom.graphs.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ar.com.notarip.teocom.graphs.domain.EdgeDTO;
import ar.com.notarip.teocom.graphs.domain.Graph;

public class Parser {

	public static Stream<Path> getfiles(Path path) throws IOException {

		Stream<Path> list = Files.list(path).filter(Files::isRegularFile);

		return list;
	}

	public static Graph proccess(Path file) throws IOException {

		String[] split = file.getFileName().toString().split("_");
		String name = split[0];
		String[] aType = split[1].split("\\.");
		String type = aType[0];

		CsvReader reader = new CsvReader(file, Boolean.FALSE);

		List<List<String>> readRecords = reader.readRecords();
		List<EdgeDTO> edges = new ArrayList<EdgeDTO>();

		for (List<String> list : readRecords) {
			EdgeDTO edge = new EdgeDTO();
			edge.setSource(Long.parseLong(list.get(0)));
			edge.setTarget(Long.parseLong(list.get(1)));
			edges.add(edge);
		}

		Graph graph = new Graph();
//
//		graph.setName(name);
//		graph.setType(type);
//		graph.setEdges(edges);

		return graph;
	}

}
