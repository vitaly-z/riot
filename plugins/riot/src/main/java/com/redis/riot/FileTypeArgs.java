package com.redis.riot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.MimeType;

import com.redis.riot.file.ResourceFactory;

import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;

public class FileTypeArgs {

	@Option(names = { "-t",
			"--type" }, description = "File type: ${COMPLETION-CANDIDATES}.", paramLabel = "<type>", completionCandidates = FileTypeCandidates.class, converter = FileTypeConverter.class)
	private MimeType type;

	public MimeType getType() {
		return type;
	}

	public void setType(MimeType type) {
		this.type = type;
	}

	private static final Map<String, MimeType> typeMap = typeMap();

	public static Map<String, MimeType> typeMap() {
		Map<String, MimeType> map = new HashMap<>();
		map.put("csv", ResourceFactory.CSV);
		map.put("psv", ResourceFactory.PSV);
		map.put("tsv", ResourceFactory.TSV);
		map.put("fw", ResourceFactory.TEXT);
		map.put("json", ResourceFactory.JSON);
		map.put("jsonl", ResourceFactory.JSON_LINES);
		map.put("xml", ResourceFactory.XML);
		return map;
	}

	static class FileTypeConverter implements ITypeConverter<MimeType> {

		@Override
		public MimeType convert(String value) throws Exception {
			return typeMap.get(value.toLowerCase());
		}
	}

	@SuppressWarnings("serial")
	static class FileTypeCandidates extends ArrayList<String> {

		FileTypeCandidates() {
			super(typeMap.keySet());
		}
	}
}
