package fr.certu.chouette.struts.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chouette.schema.types.ChouetteAreaType;
import chouette.schema.types.ConnectionLinkTypeType;
import chouette.schema.types.DayTypeType;
import chouette.schema.types.LongLatTypeType;
import chouette.schema.types.PTDirectionType;
import chouette.schema.types.ServiceStatusValueType;
import chouette.schema.types.TransportModeNameType;
import com.opensymphony.xwork2.ActionContext;


public class EnumerationApplication
{
	private static final Log log = LogFactory.getLog(EnumerationApplication.class);
	private String strutsProperties;
	private String direction;
	private String jourType;
	private String mode;
	private String statut;
	private String longitudeLatitude;
	private String correspondance;
	private String zoneType;
	private String cleLangue; // = "struts.locale";
	
	public static final String MODE_LAST_ENTRY = "Other";

	public static final String AUTHORIZEDTYPESET_ALL = "All";
	public static final String AUTHORIZEDTYPESET_C = "CommercialStop";
	public static final String AUTHORIZEDTYPESET_S = "StopPlace";
	public static final String AUTHORIZEDTYPESET_CS = "CommercialStopStopPlace";
	public static final String AUTHORIZEDTYPESET_QB = "QuayBoardingPosition";
	
	private static List<ObjetEnumere> directions = null;
	private static List<ObjetEnumere> joursTypes = null;
	private static List<ObjetEnumere> modes = null;
	private static List<ObjetEnumere> statuts = null;
	private static List<ObjetEnumere> longitudeLatitudes = null;
	private static List<ObjetEnumere> correspondances = null;
	private static List<ObjetEnumere> toutesZonesTypes = null;
	private static List<ObjetEnumere> zonesTypes = null;
	private static List<ObjetEnumere> arretTypes = null;
	
	public static final int WEEKDAY_TYPE = 0;
    public static final int WEEKEND_TYPE = 1;
    public static final int MONDAY_TYPE = 2;
    public static final int TUESDAY_TYPE = 3;
    public static final int WEDNESDAY_TYPE = 4;
    public static final int THURSDAY_TYPE = 5;
    public static final int FRIDAY_TYPE = 6;
    public static final int SATURDAY_TYPE = 7;
    public static final int SUNDAY_TYPE = 8;
    public static final int SCHOOLHOLLIDAY_TYPE = 9;
    public static final int PUBLICHOLLIDAY_TYPE = 10;
    public static final int MARKETDAY_TYPE = 11;

	public void reinit()
	{
		Locale locale = Locale.getDefault();
		log.debug("locale : " + locale);
		initDirection(locale);
		initJoursTypes(locale);
		initMode(locale);
		initLongitudeLatitude(locale);
		initStatut(locale);
		initCorrespondance(locale);
		initZoneType(locale);
	}

	private void initDirection(Locale locale) {
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		SortedSet<String> traductionTriees = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER);
		
		ResourceBundle rsDir = ResourceBundle.getBundle( direction, locale);
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		traductionTriees.addAll( cleParTraduction.keySet());
		
		directions = new ArrayList<ObjetEnumere>();
		for (String traduction : traductionTriees) 
		{
			PTDirectionType ptDirectionType = null;
			try
			{
				ptDirectionType = PTDirectionType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			directions.add( new ObjetEnumere( ptDirectionType, traduction));
		}
	}
	
	private void initJoursTypes(Locale locale) {
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		
		ResourceBundle rsDir = ResourceBundle.getBundle( jourType, locale);
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		
		//	On ne prend que les tableaux de marche de lundi à dimanche		
		ObjetEnumere[] joursTypesTab = new ObjetEnumere[SUNDAY_TYPE - 1];
		
		for (String traduction : cleParTraduction.keySet()) 
		{
			DayTypeType dayTypeType = null;
			try
			{
				dayTypeType = DayTypeType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			//	On ne prend que les tableaux de marche de lundi à dimanche
			if(MONDAY_TYPE<=dayTypeType.ordinal() && dayTypeType.ordinal() <=  SUNDAY_TYPE)
			{
				joursTypesTab[dayTypeType.ordinal()-2] = new ObjetEnumere( dayTypeType, traduction);
			}
		}
		joursTypes = Arrays.asList(joursTypesTab);
	}	
	
	private void initMode(Locale locale) {
		ResourceBundle rsDir = ResourceBundle.getBundle( mode, locale);
		String lastValue = rsDir.getString( MODE_LAST_ENTRY);
		
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		SortedSet<String> traductionTriees = new TreeSet<String>(
				new ComparatorSpecial( lastValue));
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		traductionTriees.addAll( cleParTraduction.keySet());
		
		modes = new ArrayList<ObjetEnumere>();
		for (String traduction : traductionTriees) 
		{
			TransportModeNameType modeType = null;
			try
			{
				modeType = TransportModeNameType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			modes.add( new ObjetEnumere( modeType, traduction));
			//log.debug( "modeType="+modeType+" "+traduction);
		}
	}
	
	private void initStatut(Locale locale) {
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		SortedSet<String> traductionTriees = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER);
		
		ResourceBundle rsDir = ResourceBundle.getBundle( statut, locale);
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		traductionTriees.addAll( cleParTraduction.keySet());
		
		statuts = new ArrayList<ObjetEnumere>();
		for (String traduction : traductionTriees) 
		{
			ServiceStatusValueType statutType = null;
			try
			{
				statutType = ServiceStatusValueType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			statuts.add( new ObjetEnumere( statutType, traduction));
			//log.debug( "statutType="+statutType+" "+traduction);
		}
	}
	
	private void initCorrespondance(Locale locale) {
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		SortedSet<String> traductionTriees = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER);
		
		ResourceBundle rsDir = ResourceBundle.getBundle( correspondance, locale);
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		traductionTriees.addAll( cleParTraduction.keySet());
		
		correspondances = new ArrayList<ObjetEnumere>();
		for (String traduction : traductionTriees) 
		{
			ConnectionLinkTypeType correspondanceType = null;
			try
			{
				correspondanceType = ConnectionLinkTypeType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			correspondances.add( new ObjetEnumere( correspondanceType, traduction));
			//log.debug( "correspondanceType="+correspondanceType+" "+traduction);
		}
	}	
	
	private void initZoneType(Locale locale) {
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		SortedSet<String> traductionTriees = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER);
		
		ResourceBundle rsDir = ResourceBundle.getBundle( zoneType, locale);
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		traductionTriees.addAll( cleParTraduction.keySet());
		
		toutesZonesTypes = new ArrayList<ObjetEnumere>();
		zonesTypes = new ArrayList<ObjetEnumere>();
		arretTypes = new ArrayList<ObjetEnumere>();
		for (String traduction : traductionTriees) 
		{
			ChouetteAreaType areaType = null;
			try
			{
				areaType = ChouetteAreaType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			ObjetEnumere objetEnumere = new ObjetEnumere( areaType, traduction);
			if ( ChouetteAreaType.BOARDINGPOSITION.equals( areaType) 
					|| ChouetteAreaType.QUAY.equals( areaType))
			{
				zonesTypes.add( objetEnumere);
				toutesZonesTypes.add( objetEnumere);
			}
			else if ( ChouetteAreaType.COMMERCIALSTOPPOINT.equals( areaType) 
					|| ChouetteAreaType.STOPPLACE.equals( areaType))
			{
				arretTypes.add( objetEnumere);
				toutesZonesTypes.add( objetEnumere);
			}
			//log.debug( "areaType="+areaType+" "+traduction);
		}
	}	
	private void initLongitudeLatitude(Locale locale) {
		Map<String, String> cleParTraduction = new Hashtable<String, String>();
		SortedSet<String> traductionTriees = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER);
		
		ResourceBundle rsDir = ResourceBundle.getBundle( longitudeLatitude, locale);
		
		Enumeration<String> rsDirEnum = rsDir.getKeys();
		while (rsDirEnum.hasMoreElements()) 
		{
			String cle = rsDirEnum.nextElement();
			String traduction = rsDir.getString( cle);
			if ( traduction!=null && !traduction.isEmpty())
				cleParTraduction.put( traduction, cle);
		}
		traductionTriees.addAll( cleParTraduction.keySet());
		
		longitudeLatitudes = new ArrayList<ObjetEnumere>();
		for (String traduction : traductionTriees) 
		{
			LongLatTypeType longLatType = null;
			try
			{
				longLatType = LongLatTypeType.fromValue( cleParTraduction.get( traduction));
			}
			catch( Exception e)
			{
				log.error( e.getMessage(), e);
			}
			longitudeLatitudes.add( new ObjetEnumere( longLatType, traduction));
			//log.debug( "longLatType="+longLatType+" "+traduction);
		}
	}

	
	public static List<ObjetEnumere> getModeTransportEnum()
	{
		return modes;
	}

	public static List<ObjetEnumere> getDirectionEnum()
	{
		return directions;
	}
	
	public static List<ObjetEnumere> getJoursTypesEnum()
	{
		return joursTypes;
	}

	public static List<ObjetEnumere> getLongLatEnum()
	{
		return longitudeLatitudes;
	}
	
	public static List<ObjetEnumere> getServiceStatusValueEnum()
	{
		return statuts;
	}

	public static List<ObjetEnumere> getStopAreaTypeEnum(String authorizedTypes)
	{ 
		if (AUTHORIZEDTYPESET_ALL.equals(authorizedTypes)) 
			return toutesZonesTypes;
		else if (AUTHORIZEDTYPESET_CS.equals(authorizedTypes)) {
			List<ObjetEnumere> l = new ArrayList<ObjetEnumere>();
			for (int i=0; i<toutesZonesTypes.size(); i++) {
				ChouetteAreaType type = (ChouetteAreaType)toutesZonesTypes.get(i).getEnumeratedTypeAccess();
				if (type.ordinal() == ChouetteAreaType.STOPPLACE.ordinal() || type.ordinal() == ChouetteAreaType.COMMERCIALSTOPPOINT.ordinal()) {
					l.add(toutesZonesTypes.get(i));
				}
			}
			return l;			
		}
		else if (AUTHORIZEDTYPESET_QB.equals(authorizedTypes)) {
			List<ObjetEnumere> l = new ArrayList<ObjetEnumere>();
			for (int i=0; i<toutesZonesTypes.size(); i++) {
				ChouetteAreaType type = (ChouetteAreaType)toutesZonesTypes.get(i).getEnumeratedTypeAccess();
				if (type.ordinal() == ChouetteAreaType.BOARDINGPOSITION.ordinal() || type.ordinal() == ChouetteAreaType.QUAY.ordinal()) {
					l.add(toutesZonesTypes.get(i));
				}
			}
			return l;
		}
		else if (AUTHORIZEDTYPESET_S.equals(authorizedTypes)) {
			List<ObjetEnumere> l = new ArrayList<ObjetEnumere>();
			for (int i=0; i<toutesZonesTypes.size(); i++) {
				ChouetteAreaType type = (ChouetteAreaType)toutesZonesTypes.get(i).getEnumeratedTypeAccess();
				if (type.ordinal() == ChouetteAreaType.STOPPLACE.ordinal()) {
					l.add(toutesZonesTypes.get(i));
				}
			}
			return l;
		}
		else if (AUTHORIZEDTYPESET_C.equals(authorizedTypes)) {
			List<ObjetEnumere> l = new ArrayList<ObjetEnumere>();
			for (int i=0; i<toutesZonesTypes.size(); i++) {
				ChouetteAreaType type = (ChouetteAreaType)toutesZonesTypes.get(i).getEnumeratedTypeAccess();
				if (type.ordinal() == ChouetteAreaType.COMMERCIALSTOPPOINT.ordinal()) {
					l.add(toutesZonesTypes.get(i));
				}
			}
			return l;
		}
		else
			return toutesZonesTypes;
	}
	
	
	public static List<ObjetEnumere> getStopAreaTypeEnum()
	{ 
		return toutesZonesTypes;
	}

	public static List<ObjetEnumere> getArretPhysiqueAreaTypeEnum()
	{ 
		return arretTypes;
	}

	public static List<ObjetEnumere> getZoneAreaTypeEnum()
	{ 
		return zonesTypes;
	}
	
	public static List<ObjetEnumere> getConnectionLinkTypeEnum()
	{ 
		return correspondances;
	}

	public void setStrutsProperties(String strutsProperties) {
		this.strutsProperties = strutsProperties;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public void setJourType(String jourType) {
		this.jourType = jourType;
	}


	public void setCleLangue(String cleLangue) {
		this.cleLangue = cleLangue;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public void setLongitudeLatitude(String longitudeLatitude) {
		this.longitudeLatitude = longitudeLatitude;
	}

	public void setCorrespondance(String correspondance) {
		this.correspondance = correspondance;
	}

	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}
	
	private class ComparatorSpecial implements Comparator<String> 
	{
		private String lastValue;
		public ComparatorSpecial( String lastValue)
		{
			if ( lastValue==null) throw new IllegalArgumentException();
			this.lastValue = lastValue;
		}

		public int compare(String o1, String o2){
			if ( lastValue.equals( o1)) return 1;
			if ( lastValue.equals( o2)) return -1;
			return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
		}
	}

}