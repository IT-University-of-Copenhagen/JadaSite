/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.dao;

import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.State;

public class StateDAO extends State {
	private static final long serialVersionUID = -2251330104927315726L;
	public static State load(String siteId, Long stateId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		State state = (State) em.find(State.class, stateId);
		if (!state.getCountry().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return state;
	}
	public static State loadByStateName(String siteId, String stateName) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "select state " +
		 			 "from   State state " +
		          	 "inner  join state.country country " +
		          	 "where  country.siteId = :siteId " +
		          	 "and    state.stateName = :stateName";
		Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("stateName", stateName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (State) iterator.next();
    	}
    	return null;
	}
	public static State loadByStateCode(String siteId, String stateCode) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "select state " +
        			 "from   State state " +
  	                 "inner  join state.country country " +
  	                 "where  country.siteId = :siteId " +
  	                 "and    state.stateCode = :stateCode";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("stateCode", stateCode);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (State) iterator.next();
    	}
    	return null;
	}
}
