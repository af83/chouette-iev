package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.naming.InitialContext;

import org.apache.commons.io.FilenameUtils;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.FileUtils;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.report.Report;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.report.ZipItem;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

// @Stateless(name = UncompressCommand.COMMAND)
@Log4j
public class UncompressCommand implements Command, ReportConstant {

	public static final String COMMAND = "UncompressCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);
		Report report = (Report) context.get(REPORT);

		String path = (String) context.get(PATH);
		String file = (String) context.get(ARCHIVE);
		Path filename = Paths.get(path, file);
		Path target = Paths.get(path, INPUT);
		if (!Files.exists(target)) {
			Files.createDirectories(target);
		}
		if (FilenameUtils.getExtension(filename.toString()).equalsIgnoreCase("zip"))
		{
			ZipItem zip = new ZipItem();
			zip.setName(file);

			try {
				report.setZip(zip);
				FileUtils.uncompress(filename.toString(), target.toString());
				result = SUCCESS;
				zip.setStatus(STATUS_OK);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				zip.addError(e.getMessage());
				zip.setStatus(STATUS_ERROR);
				report.setResult(STATUS_ERROR);
				report.setFailure("invalid_zip");
			}
		}
		else
		{
			org.apache.commons.io.FileUtils.copyFileToDirectory(filename.toFile(), target.toFile());
		}

		log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new UncompressCommand();
//			try {
//				String name = "java:app/mobi.chouette.exchange/"
//						+ COMMAND;
//				result = (Command) context.lookup(name);
//			} catch (NamingException e) {
//				log.error(e);
//			}
			return result;
		}
	}

	static {
		CommandFactory.factories
				.put(UncompressCommand.class.getName(), new DefaultCommandFactory());
	}
}
