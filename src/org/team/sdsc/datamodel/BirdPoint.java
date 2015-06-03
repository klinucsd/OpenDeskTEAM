package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM person.
 *
 * @author Kai Lin
 */
@Entity
@DiscriminatorValue("Bird Point")
public class BirdPoint extends SamplingUnit {

           
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public BirdPoint() {
    	super();
    }
    
    
    /**
     * Get the name by which this sampling unit is known.
     *
     * @return the name of the sampling unit.
     */
    @Display(title="Sampling Unit Name", var="SamplingUnitName", index=1, width=100)  
    public String getName() {
        return super.getName();
    }

    /**
     * Get the latitude of this sampling unit.
     *
     * @return the latitude of the sampling unit.
     */
    @Display(title="Latitude", index=2, width=100)   
    public Double getLatitude() {
        return super.getLatitude() ;
    }
    
    
    /**
     * Get the longitude of this sampling unit.
     *
     * @return the longitude of the sampling unit.
     */
    @Display(title="Longitude", index=3, width=100)   
    public Double getLongitude() {
        return super.getLongitude();
    }


    @Display(title="Method", index=4, width=100) 
    public String getMethod() {
	return super.getMethod();
    }
    
    
    @Display(title="Block X Coordinate", var="ImaXCoordinate", index=5, width=100) 
    public Integer getImaXCoordinate() {
	return super.getImaXCoordinate();
    }
 	
 	
    @Display(title="Block Y Coordinate", var="ImaYCoordinate", index=6, width=100)  
    public Integer getImaYCoordinate() {
	return super.getImaYCoordinate();
    }
    
    
}