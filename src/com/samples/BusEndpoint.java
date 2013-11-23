package com.samples;

import com.samples.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "birra")
public class BusEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listBus")
	public CollectionResponse<Bus> listBus(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Bus> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Bus.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Bus>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Bus obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Bus> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getBus")
	public Bus getBus(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Bus bus = null;
		try {
			bus = mgr.getObjectById(Bus.class, id);
		} finally {
			mgr.close();
		}
		return bus;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param bus the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertBus")
	public Bus insertBus(Bus bus) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (bus.getId() != null) {
				if (containsBus(bus)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(bus);
		} finally {
			mgr.close();
		}
		return bus;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param bus the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateBus")
	public Bus updateBus(Bus bus) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsBus(bus)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(bus);
		} finally {
			mgr.close();
		}
		return bus;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeBus")
	public void removeBus(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Bus bus = mgr.getObjectById(Bus.class, id);
			mgr.deletePersistent(bus);
		} finally {
			mgr.close();
		}
	}

	private boolean containsBus(Bus bus) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Bus.class, bus.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
