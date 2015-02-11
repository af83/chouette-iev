package mobi.chouette.exchange.importer.updater;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.CollectionUtils;
import mobi.chouette.common.Context;
import mobi.chouette.common.Pair;
import mobi.chouette.dao.CompanyDAO;
import mobi.chouette.dao.GroupOfLineDAO;
import mobi.chouette.dao.PTNetworkDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.model.Company;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.Line;
import mobi.chouette.model.PTNetwork;
import mobi.chouette.model.Route;

@Log4j
@Stateless(name = LineUpdater.BEAN_NAME)
public class LineUpdater implements Updater<Line> {

	public static final String BEAN_NAME = "LineUpdater";

	@EJB
	private PTNetworkDAO ptNetworkDAO;

	@EJB
	private CompanyDAO companyDAO;

	@EJB
	private GroupOfLineDAO groupOfLineDAO;

	@EJB
	private RouteDAO routeDAO;

	@Override
	public void update(Context context, Line oldValue, Line newValue)
			throws Exception {

		InitialContext initialContext = (InitialContext) context
				.get(INITIAL_CONTEXT);

		if (newValue.isSaved()) {
			return;
		}
		newValue.setSaved(true);

		if (newValue.getObjectId() != null
				&& !newValue.getObjectId().equals(oldValue.getObjectId())) {
			oldValue.setObjectId(newValue.getObjectId());
		}
		if (newValue.getObjectVersion() != null
				&& !newValue.getObjectVersion().equals(
						oldValue.getObjectVersion())) {
			oldValue.setObjectVersion(newValue.getObjectVersion());
		}
		if (newValue.getCreationTime() != null
				&& !newValue.getCreationTime().equals(
						oldValue.getCreationTime())) {
			oldValue.setCreationTime(newValue.getCreationTime());
		}
		if (newValue.getCreatorId() != null
				&& !newValue.getCreatorId().equals(oldValue.getCreatorId())) {
			oldValue.setCreatorId(newValue.getCreatorId());
		}
		if (newValue.getName() != null
				&& !!newValue.getName().equals(oldValue.getName())) {
			oldValue.setName(newValue.getName());
		}
		if (newValue.getComment() != null
				&& !newValue.getComment().equals(oldValue.getComment())) {
			oldValue.setComment(newValue.getComment());
		}
		if (newValue.getNumber() != null
				&& !newValue.getNumber().equals(oldValue.getNumber())) {
			oldValue.setNumber(newValue.getNumber());
		}
		if (newValue.getPublishedName() != null
				&& !newValue.getPublishedName().equals(
						oldValue.getPublishedName())) {
			oldValue.setPublishedName(newValue.getPublishedName());
		}
		if (newValue.getRegistrationNumber() != null
				&& !newValue.getRegistrationNumber().equals(
						oldValue.getRegistrationNumber())) {
			oldValue.setRegistrationNumber(newValue.getRegistrationNumber());
		}
		if (newValue.getTransportModeName() != null
				&& !newValue.getTransportModeName().equals(
						oldValue.getTransportModeName())) {
			oldValue.setTransportModeName(newValue.getTransportModeName());
		}
		if (newValue.getMobilityRestrictedSuitable() != null
				&& !newValue.getMobilityRestrictedSuitable().equals(
						oldValue.getMobilityRestrictedSuitable())) {
			oldValue.setMobilityRestrictedSuitable(newValue
					.getMobilityRestrictedSuitable());
		}
		if (newValue.getIntUserNeeds() != null
				&& !newValue.getIntUserNeeds().equals(
						oldValue.getIntUserNeeds())) {
			oldValue.setIntUserNeeds(newValue.getIntUserNeeds());
		}
		if (newValue.getUrl() != null
				&& !newValue.getUrl().equals(oldValue.getUrl())) {
			oldValue.setUrl(newValue.getUrl());
		}
		if (newValue.getColor() != null
				&& !newValue.getColor().equals(oldValue.getColor())) {
			oldValue.setColor(newValue.getColor());
		}
		if (newValue.getTextColor() != null
				&& !newValue.getTextColor().equals(oldValue.getTextColor())) {
			oldValue.setTextColor(newValue.getTextColor());
		}

		// PTNetwork
		if (newValue.getPtNetwork() == null) {
			oldValue.setPTNetwork(null);
		} else {
			PTNetwork ptNetwork = ptNetworkDAO.findByObjectId(newValue
					.getPtNetwork().getObjectId());
			if (ptNetwork == null) {
				ptNetwork = new PTNetwork();
				ptNetwork.setObjectId(newValue.getPtNetwork().getObjectId());
				ptNetworkDAO.create(ptNetwork);
			}
			Updater<PTNetwork> ptNetworkUpdater = UpdaterFactory.create(
					initialContext, PTNetworkUpdater.class.getName());
			ptNetworkUpdater.update(null, oldValue.getPtNetwork(),
					newValue.getPtNetwork());
			oldValue.setPTNetwork(ptNetwork);
		}

		// Company
		if (!newValue.isSaved()) {
			if (newValue.getCompany() == null) {
				oldValue.setCompany(null);
			} else {
				Company company = companyDAO.findByObjectId(newValue
						.getCompany().getObjectId());
				if (company == null) {
					company = new Company();
					company.setObjectId(newValue.getCompany().getObjectId());
					companyDAO.create(company);
				}
				Updater<Company> companyUpdater = UpdaterFactory.create(
						initialContext, CompanyUpdater.class.getName());
				companyUpdater.update(null, oldValue.getCompany(),
						newValue.getCompany());
				oldValue.setCompany(company);
			}
		}
		// GroupOfLine
		Collection<GroupOfLine> addedGroupOfLine = CollectionUtils.substract(
				newValue.getGroupOfLines(), oldValue.getGroupOfLines(),
				NeptuneIdentifiedObjectComparator.INSTANCE);
		for (GroupOfLine item : addedGroupOfLine) {
			GroupOfLine groupOfLine = groupOfLineDAO.findByObjectId(item
					.getObjectId());
			if (groupOfLine == null) {
				groupOfLine = new GroupOfLine();
				groupOfLine.setObjectId(item.getObjectId());
				groupOfLineDAO.create(groupOfLine);
			}
			groupOfLine.addLine(oldValue);
		}

		Updater<GroupOfLine> groupOfLineUpdater = UpdaterFactory.create(
				initialContext, GroupOfLineUpdater.class.getName());
		Collection<Pair<GroupOfLine, GroupOfLine>> modifiedGroupOfLine = CollectionUtils
				.intersection(oldValue.getGroupOfLines(),
						newValue.getGroupOfLines(),
						NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Pair<GroupOfLine, GroupOfLine> pair : modifiedGroupOfLine) {
			groupOfLineUpdater.update(null, pair.getLeft(), pair.getRight());
		}

		Collection<GroupOfLine> removedGroupOfLine = CollectionUtils.substract(
				oldValue.getGroupOfLines(), newValue.getGroupOfLines(),
				NeptuneIdentifiedObjectComparator.INSTANCE);
		for (GroupOfLine groupOfLine : removedGroupOfLine) {
			groupOfLine.removeLine(oldValue);
		}

		// Route
		Collection<Route> addedRoute = CollectionUtils.substract(
				newValue.getRoutes(), oldValue.getRoutes(),
				NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Route item : addedRoute) {
			Route route = routeDAO.findByObjectId(item.getObjectId());
			if (route == null) {
				route = new Route();
				route.setObjectId(item.getObjectId());
				routeDAO.create(route);
			}
			route.setLine(oldValue);
		}

		Updater<Route> routeUpdater = UpdaterFactory.create(initialContext,
				RouteUpdater.class.getName());
		Collection<Pair<Route, Route>> modifiedRoute = CollectionUtils
				.intersection(oldValue.getRoutes(), newValue.getRoutes(),
						NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Pair<Route, Route> pair : modifiedRoute) {
			routeUpdater.update(null, pair.getLeft(), pair.getRight());
		}

		// Collection<Route> removedRoute = CollectionUtils.substract(
		// oldValue.getRoutes(), newValue.getRoutes(),
		// NeptuneIdentifiedObjectComparator.INSTANCE);
		// for (Route route : removedRoute) {
		// route.setLine(null);
		// routeDAO.delete(route);
		// }

		// TODO stop area list (routingConstraintLines)
	}

	static {
		UpdaterFactory.register(LineUpdater.class.getName(),
				new UpdaterFactory() {

					@Override
					protected <T> Updater<T> create(InitialContext context) {
						Updater result = null;
						try {
							result = (Updater) context
									.lookup("java:app/mobi.chouette.exchange/"
											+ BEAN_NAME);
						} catch (NamingException e) {
							log.error(e);
						}
						return result;
					}
				});
	}

}