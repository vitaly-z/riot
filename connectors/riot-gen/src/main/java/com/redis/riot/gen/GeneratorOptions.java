package com.redis.riot.gen;

import picocli.CommandLine;

public class GeneratorOptions {

	@CommandLine.Option(names = "--start", description = "Start index (default: ${DEFAULT-VALUE})", paramLabel = "<int>")
	protected int start = 1;
	@CommandLine.Option(names = "--count", description = "Number of items to generate (default: ${DEFAULT-VALUE})", paramLabel = "<int>")
	protected int count = 1000;
	@CommandLine.Option(names = "--sleep", description = "Duration in ms to sleep before each item generation (default: ${DEFAULT-VALUE})", paramLabel = "<ms>")
	private long sleep = 0;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getSleep() {
		return sleep;
	}

	public void setSleep(long sleep) {
		this.sleep = sleep;
	}

}
