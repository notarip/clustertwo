package ar.com.notarip.teocom.graphs.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.notarip.teocom.graphs.domain.Edge;
import ar.com.notarip.teocom.graphs.domain.EdgeDTO;
import ar.com.notarip.teocom.graphs.domain.Graph;
import ar.com.notarip.teocom.graphs.util.Parser;

public class ParserTest {

	
	Graph graph;
	@Before
	public void setup() throws IOException{
		
		Path path = Paths.get("src/test/resources", "graph1_social.csv");
		
		graph = Parser.proccess(path);
		
	}

	@Test
	public void testFileName() throws IOException{
		
		Assert.assertTrue(graph.getName().equals("graph1"));
		
	}
	
	@Test 
	public void testType(){
		
		Assert.assertTrue(graph.getType().equals("social"));
		
	}
	
	@Test
	public void testEdges(){
		
		EdgeDTO e1 = graph.getEdges().get(0);
		EdgeDTO e2 = graph.getEdges().get(1);
		
		Assert.assertTrue(e1.getSource().equals(1L));
		Assert.assertTrue(e1.getTarget().equals(1L));
		Assert.assertTrue(e2.getSource().equals(1L));
		Assert.assertTrue(e2.getTarget().equals(2L));
	}
}
