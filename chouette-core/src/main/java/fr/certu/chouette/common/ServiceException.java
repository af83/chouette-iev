package fr.certu.chouette.common;

public class ServiceException extends RuntimeException 
{
	private CodeIncident code;

	public ServiceException(CodeIncident code, String message) 
	{
		super( message);
		this.code = code;
	}
	public ServiceException(CodeIncident code, String message, Throwable exception) 
	{
		super( message, exception);
		this.code = code;
	}

	public CodeIncident getCode() {
		return code;
	}

}
