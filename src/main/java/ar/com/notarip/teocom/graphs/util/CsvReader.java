package ar.com.notarip.teocom.graphs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReader {

	private static final String SEPARATOR = ";";

	private final Reader source;
	private final Boolean header;

	public CsvReader(Path path, Boolean header) throws IOException {
		Reader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
		this.source = reader;
		this.header = header;
	}

	public List<String> readHeader() {
		try (BufferedReader reader = new BufferedReader(source)) {
			return reader.lines().findFirst().map(line -> Arrays.asList(line.split(SEPARATOR))).get();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	List<List<String>> readRecords() {
		try (BufferedReader reader = new BufferedReader(source)) {
			Long skip = 0L;
			if(header) skip = 1L;
			return reader.lines().skip(skip).map(line -> Arrays.asList(line.split(SEPARATOR)))
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
