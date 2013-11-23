package com.samples;

import com.samples.PMF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import com.google.appengine.api.datastore.Cursor;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;

import com.google.appengine.datanucleus.query.JDOCursorHelper;


import javax.annotation.Nullable;

import javax.inject.Named;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api (name = "birra") 
public class BeerEndpoint {

	private static final Index INDEX = getIndex();
	
	private static Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder()
				.setName("beerindex").build();
		
		Index indexServiceFactory = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		return indexServiceFactory;
	}
	
	/**
	 * This method lists all the entities by query param inserted in datastore.
	 * It uses HTTP GET method.
	 * 
	 * @param queryString Filter String 
	 * @return A CollectionResponse class containing the list of all query entities
	 */
	@ApiMethod (httpMethod="GET", name="beer.search")
	public List<Beer> searchBeer(@Named("term") String queryString) {

		  List<Beer> beerList = new ArrayList<Beer>();
	      Results<ScoredDocument> results = INDEX.search(queryString);

	      for (ScoredDocument scoredDoc : results) {
	    	  try {
			      Field f = scoredDoc.getOnlyField("id");
			      if (f == null || f.getText() == null) continue;
	
		         long beerId = Long.parseLong(f.getText());
		 	     if (beerId != -1) {
		 	        Beer b = getBeer(beerId);
		 	        beerList.add(b);
		 	     }
	    	 } catch (Exception e){
	    		 e.printStackTrace();
	    	 }
	      }
	     return beerList;
	}
	
	
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listBeer")
	public CollectionResponse<Beer> listBeer(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Beer> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Beer.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Beer>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Beer obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Beer> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getBeer")
	public Beer getBeer(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Beer beer = null;
		try {
			beer = mgr.getObjectById(Beer.class, id);
		} finally {
			mgr.close();
		}
		return beer;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param beer the entity to be inserted.
	 * @return The inserted entity.
	 * 
	 * Fix : http://stackoverflow.com/questions/14971558/generate-automatic-id-idgeneratorstrategy
	 */
	@ApiMethod(name = "insertBeer")
	public Beer insertBeer(Beer beer) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (beer.getId() != null) {
				if (containsBeer(beer)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(beer);
		} finally {
			mgr.close();
		}
		
		addBeerToSearchIndex (beer) ;
		
		return beer;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param beer the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateBeer")
	public Beer updateBeer(Beer beer) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsBeer(beer)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(beer);
		} finally {
			mgr.close();
		}
		
		addBeerToSearchIndex (beer) ;
		
		return beer;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "beers.delete")
	public void removeBeer(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Beer beer = mgr.getObjectById(Beer.class, id);
			mgr.deletePersistent(beer);
		} finally {
			mgr.close();
		}
	}

	private boolean containsBeer(Beer beer) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Beer.class, beer.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}
	
	private static void addBeerToSearchIndex(Beer beer) {
	    Document.Builder docBuilder = Document.newBuilder();
	    /*** Id ***/
	    Long beerId = beer.getId();
	    docBuilder.addField(Field.newBuilder().setName("id").setText(Long.toString(beerId)));
	    
	    /*** Name ***/
	    String beerName = beer.getBeerName(); 
	    String docBuilderName = "";
	    if (beerName != null) {
	    	docBuilderName = beerName;
	    }
	    docBuilder.addField(Field.newBuilder().setName("name").setText(docBuilderName));
	    /*** Name ***/
	    
	    /*** Country ***/
	    String beerCountry = beer.getCountry();
	    String docBuilderCountry = "";
	    if (beerCountry != null) {
	    	docBuilderCountry = beerCountry;
	    }
	    docBuilder.addField(Field.newBuilder().setName("country").setText(docBuilderCountry));
	    /*** Country ***/
	    
	    /*** Kind Of Beer ***/
	    String beerKindOfBeer = beer.getKindOfBeer();
	    String docBuilderKindOfBeer = "";
	    if (beerKindOfBeer != null) {
	    	docBuilderKindOfBeer = beerKindOfBeer;
	    }
	    docBuilder.addField(Field.newBuilder().setName("kindOfBeer").setText(docBuilderKindOfBeer));
	    /*** Kind Of Beer ***/
	    
	    /*** Latitude ***/
	    Double beerLatitude = beer.getLatitude();
	    Double docBulderLatitude = (double) 0;
	    if (beerLatitude != null) {
	    	docBulderLatitude = beerLatitude;
	    }
	    docBuilder.addField(Field.newBuilder().setName("latitude").setNumber(docBulderLatitude));
	    /*** Latitude ***/ 
	    
	    /*** Longitude ***/
	    Double beerLongitude = beer.getLongitude();
	    Double docBulderLongitude = (double) 0;
	    if (beerLongitude != null) {
	    	docBulderLongitude = beerLongitude;
	    }
	    docBuilder.addField(Field.newBuilder().setName("longitude").setNumber(docBulderLongitude));
	    /*** Longitude ***/ 
	    
	    /*** Description ***/
	    String beerDescription = beer.getDescription();
	    String docBulderDescription = "";
	    if (beerDescription != null) {
	    	docBulderDescription = beerDescription;
	    }
	    docBuilder.addField(Field.newBuilder().setName("description").setText(docBulderDescription));
	    /*** Description ***/
	    
	    /*** Score ***/
	    Long beerScore = beer.getScore();
	    Long docBuilderScore = (long) 0;
	    if (beerScore != null) {
	    	docBuilderScore = beerScore;
	    }
	    docBuilder.addField(Field.newBuilder().setName("score").setNumber(docBuilderScore));
	    /*** Score ***/
	    
	    /*** Number of Drinks ***/
	    Long beerNumberOfDrinks = beer.getNumberOfDrinks();
	    Long docBuilderNumberOfDrinks = (long) 0;
	    if (beerNumberOfDrinks != null) {
	    	docBuilderNumberOfDrinks = beerNumberOfDrinks;
	    }
	    docBuilder.addField(Field.newBuilder().setName("numberOfDrinks").setNumber(docBuilderNumberOfDrinks));
	    /*** Number of Drinks ***/
	    
	    docBuilder.addField(Field.newBuilder().setName("published").setDate(new Date()));
	    
	    docBuilder.setId(Long.toString(beerId));
	    
	    Document document = docBuilder.build();
	    INDEX.put(document);
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
