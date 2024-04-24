package com.redis.riot.cli;

import java.io.PrintWriter;

import org.springframework.expression.Expression;

import com.redis.riot.core.RiotUtils;
import com.redis.riot.core.SlotRange;
import com.redis.riot.core.TemplateExpression;
import com.redis.spring.batch.gen.Range;

import picocli.AutoComplete.GenerateCompletion;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExecutionStrategy;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.RunFirst;
import picocli.CommandLine.RunLast;

@Command(subcommands = { DbImportCommand.class, DbExportCommand.class, FileDumpImportCommand.class,
		FileImportCommand.class, FileDumpExportCommand.class, FakerImportCommand.class, GenerateCommand.class,
		ReplicateCommand.class, PingCommand.class, GenerateCompletion.class })
public abstract class AbstractMainCommand extends BaseCommand implements Runnable {

	PrintWriter out;

	PrintWriter err;

	@ArgGroup(exclusive = false)
	private RedisArgs redisArgs = new RedisArgs();

	@Override
	public void run() {
		spec.commandLine().usage(out);
	}

	public static int run(AbstractMainCommand cmd, String... args) {
		CommandLine commandLine = new CommandLine(cmd);
		cmd.out = commandLine.getOut();
		cmd.err = commandLine.getErr();
		return execute(commandLine, args);
	}

	public static int run(AbstractMainCommand cmd, PrintWriter out, PrintWriter err, String[] args,
			IExecutionStrategy... executionStrategies) {
		CommandLine commandLine = new CommandLine(cmd);
		commandLine.setOut(out);
		commandLine.setErr(err);
		cmd.out = out;
		cmd.err = err;
		return execute(commandLine, args, executionStrategies);
	}

	private static int execute(CommandLine commandLine, String[] args, IExecutionStrategy... executionStrategies) {
		CompositeExecutionStrategy executionStrategy = new CompositeExecutionStrategy();
		executionStrategy.addDelegates(executionStrategies);
		executionStrategy.addDelegates(AbstractMainCommand::executionStrategy);
		commandLine.setExecutionStrategy(executionStrategy);
		commandLine.registerConverter(Range.class, new RangeTypeConverter<>(Range::of));
		commandLine.registerConverter(SlotRange.class, new RangeTypeConverter<>(SlotRange::of));
		commandLine.registerConverter(Expression.class, RiotUtils::parse);
		commandLine.registerConverter(TemplateExpression.class, RiotUtils::parseTemplate);
		commandLine.setCaseInsensitiveEnumValuesAllowed(true);
		commandLine.setUnmatchedOptionsAllowedAsOptionParameters(false);
		return commandLine.execute(args);
	}

	private static int executionStrategy(ParseResult parseResult) {
		for (ParseResult subcommand : parseResult.subcommands()) {
			Object command = subcommand.commandSpec().userObject();
			if (AbstractImportCommand.class.isAssignableFrom(command.getClass())) {
				AbstractImportCommand importCommand = (AbstractImportCommand) command;
				for (ParseResult redisCommand : subcommand.subcommands()) {
					if (redisCommand.isUsageHelpRequested()) {
						return new RunLast().execute(redisCommand);
					}
					importCommand.getCommands().add((RedisCommand) redisCommand.commandSpec().userObject());
				}
				return new RunFirst().execute(subcommand);
			}
		}
		return new RunLast().execute(parseResult); // default execution strategy
	}

	public RedisArgs getRedisArgs() {
		return redisArgs;
	}

	public void setRedisArgs(RedisArgs redisArgs) {
		this.redisArgs = redisArgs;
	}

}
