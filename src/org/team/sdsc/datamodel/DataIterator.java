package org.team.sdsc.datamodel;

import java.util.*;
import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Represents a TEAM DataIterator.
 *
 * @author Kai Lin
 */
public class DataIterator implements Iterator {

	private Network network;
	private Query query;
	private int start;
	private int length;
	private int size;
	private Iterator iter;

	
	protected DataIterator(Network network, Query query) {
		this.network = network;
		this.query = query;
		this.start = 0;
		this.length = 10000;
		List records = network.getData(query, start, length);
		this.iter = records.iterator();
		this.size = records.size();
	}
	
	
	public boolean hasNext() {
		if (iter.hasNext()) {
			return true;
		} else if (size > 0) {
			this.start += this.length;
			List records = network.getData(query, start, length);
			this.iter = records.iterator();
			this.size = records.size();
			return iter.hasNext();
		} else {
			return false;
		}
	}
	
	
	public Object next() {
		if (iter.hasNext()) {
			return iter.next();
		} else if (size > 0) {
			this.start += this.length;
			List records = network.getData(query, start, length);
			this.iter = records.iterator();
			return iter.next();
		} else {
			throw new NoSuchElementException();
		}
	}
	
	
	public void remove() {
	
	}
	

	
	

}