/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */

package fr.certu.chouette.plugin.validation;

import fr.certu.chouette.plugin.report.ReportItem;


/**
 * @author michel
 *
 */
public class ValidationClassReportItem extends ReportItem 
{
    public enum CLASS {ZERO,ONE,TWO,THREE};
	/**
	 * 
	 */
	public ValidationClassReportItem(CLASS validationClass) 
	{
		setMessageKey(validationClass.name());
	}
	
	/* (non-Javadoc)
	 * @see fr.certu.chouette.plugin.report.Report#addItem(fr.certu.chouette.plugin.report.ReportItem)
	 */
	@Override
	public void addItem(ReportItem item) 
	{
		super.addItem(item);
		int status = getStatus().ordinal();
		int itemStatus = item.getStatus().ordinal();
		if (itemStatus > status) setStatus(item.getStatus());
	}
	
	

}