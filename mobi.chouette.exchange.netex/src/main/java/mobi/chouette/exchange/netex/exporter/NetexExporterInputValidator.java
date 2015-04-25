package mobi.chouette.exchange.netex.exporter;

import java.io.IOException;
import java.util.Arrays;

import lombok.extern.log4j.Log4j;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;

@Log4j
public class NetexExporterInputValidator implements InputValidator {

	private static String[] allowedTypes = { "all", "line", "network", "company", "groupofline" };

	@Override
	public boolean check(AbstractParameter abstractParameter, ValidationParameters validationParameters, String fileName) {
		if (!(abstractParameter instanceof NetexExportParameters)) {
			log.error("invalid parameters for netex export " + abstractParameter.getClass().getName());
			return false;
		}

		NetexExportParameters parameters = (NetexExportParameters) abstractParameter;
		if (parameters.getStartDate() != null && parameters.getEndDate() != null) {
			if (parameters.getStartDate().after(parameters.getEndDate())) {
				log.error("end date before start date ");
				return false;
			}
		}

		String type = parameters.getReferencesType();
		if (type == null || type.isEmpty()) {
			log.error("missing type");
			return false;
		}
		if (!Arrays.asList(allowedTypes).contains(type.toLowerCase())) {
			log.error("invalid type " + type);
			return false;
		}

		if (fileName != null) {
			log.error("input data not expected");
			return false;
		}
		return true;
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			InputValidator result = new NetexExporterInputValidator();
			return result;
		}
	}

	static {
		InputValidatorFactory.factories.put(NetexExporterInputValidator.class.getName(), new DefaultFactory());
	}

}
