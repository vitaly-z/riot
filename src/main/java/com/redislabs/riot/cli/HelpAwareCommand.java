package com.redislabs.riot.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(abbreviateSynopsis = true)
public class HelpAwareCommand {

	@Option(names = "--help", usageHelp = true, description = "Show this help message and exit")
	private boolean helpRequested;

}