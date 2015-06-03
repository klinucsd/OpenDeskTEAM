package org.team.sdsc.datamodel.annotation;

import java.lang.reflect.*;


public class ColumnHeader {

	private Class clazz;
	private Method method;
	private int index;

	public ColumnHeader(Class clazz, Method method, int index) {
		this.clazz = clazz;
		this.method = method;
		this.index = index;
	}

	
	public Class getColumnClass() {
		return clazz;
	}
	
	
	public Method getColumnMethod() {
		return method;
	}
	
	
	public int getIndex() {
		return index;
	}

}

