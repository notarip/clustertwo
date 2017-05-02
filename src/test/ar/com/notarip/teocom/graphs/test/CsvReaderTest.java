package ar.com.notarip.teocom.graphs.test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ar.com.notarip.teocom.graphs.util.CsvReader;


public class CsvReaderTest {
	 
    @Test
    public void readsHeader() {
        CsvReader csvReader = createCsvReader();
        List<String> header = csvReader.readHeader();
        
        Assert.assertTrue(header.contains("username") && header.contains("password"));
        
    }
 
	private CsvReader createCsvReader() {
		try {
			Path path = Paths.get("src/test/resources", "sample.csv");
			return new CsvReader(path, Boolean.TRUE);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
