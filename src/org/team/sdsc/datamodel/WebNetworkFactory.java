
package org.team.sdsc.datamodel;

import java.util.*;
import java.text.*;
import java.io.*;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;
import au.com.bytecode.opencsv.*;

/**
 * Represents a TEAM Network.
 *
 * @author Kai Lin
 */
public class WebNetworkFactory {

    private static final SessionFactory factory;
    //private static final ThreadLocal session = new ThreadLocal(); 
   
    private static Session session;	  


    static {
	// Create a configuration based on the annotations in our model classes.
	Configuration config = new AnnotationConfiguration();
	config.configure("webTEAM.hibernate.cfg.xml");

	// Get the session factory we can use for persistence
	factory = config.buildSessionFactory();

	session = factory.openSession();
    }


    public static Network newInstance() {

	Session session = factory.openSession();
	Transaction tx = session.beginTransaction();
	return new Network(session, tx);

    }


}