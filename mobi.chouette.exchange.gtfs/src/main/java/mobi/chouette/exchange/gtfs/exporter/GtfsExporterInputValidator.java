package mobi.chouette.exchange.gtfs.exporter;

import java.io.IOException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;

@Log4j
public class GtfsExporterInputValidator extends AbstractInputValidator {

	private static String[] allowedTypes = { "line", "network", "company", "group_of_line", "stop_area" };

	@Override
	public boolean checkParameters(AbstractParameter abstractParameter, ValidationParameters validationParameters) {

		if (!(abstractParameter instanceof GtfsExportParameters)) {
			log.error("invalid parameters for gtfs export " + abstractParameter.getClass().getName());
			return false;
		}

		GtfsExportParameters parameters = (GtfsExportParameters) abstractParameter;
		return parameters.isValid(log, allowedTypes);
	}

	@Override
	public boolean checkFilename(String fileName) {
		if (fileName != null) {
			log.error("input data not expected");
			return false;
		}

		return true;
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			InputValidator result = new GtfsExporterInputValidator();
			return result;
		}
	}

	static {
		InputValidatorFactory.factories.put(GtfsExporterInputValidator.class.getName(), new DefaultFactory());
	}

}
