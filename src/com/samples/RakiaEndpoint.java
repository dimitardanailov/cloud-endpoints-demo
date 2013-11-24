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

@Api (name = "birra")
public class RakiaEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listRakia")
	public CollectionResponse<Rakia> listRakia(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Rakia> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Rakia.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Rakia>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Rakia obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Rakia> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getRakia")
	public Rakia getRakia(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Rakia rakia = null;
		try {
			rakia = mgr.getObjectById(Rakia.class, id);
		} finally {
			mgr.close();
		}
		return rakia;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param rakia the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertRakia")
	public Rakia insertRakia(Rakia rakia) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (rakia.getId() != null) {
				if (containsRakia(rakia)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(rakia);
		} finally {
			mgr.close();
		}
		return rakia;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param rakia the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateRakia")
	public Rakia updateRakia(Rakia rakia) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsRakia(rakia)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(rakia);
		} finally {
			mgr.close();
		}
		return rakia;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeRakia")
	public void removeRakia(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Rakia rakia = mgr.getObjectById(Rakia.class, id);
			mgr.deletePersistent(rakia);
		} finally {
			mgr.close();
		}
	}

	private boolean containsRakia(Rakia rakia) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Rakia.class, rakia.getId());
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
