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
@DiscriminatorValue("Liana Point")
public class LianaPoint extends SamplingUnit {

           
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public LianaPoint() {
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

    @Display(title="Subplot Number", var="SubplotNumber", index=5, width=100) 
    public Integer getSubplotNumber() {
	return super.getSubplotNumber();
    }
 	
    @Display(title="1ha Plot X Coordinate", var="PlotXCoordinate", index=6, width=100) 
    public Float getPlotXCoordinate() {
	return super.getPlotXCoordinate();
    }
 	 
    @Display(title="1ha Plot Y Coordinate", var="PlotYCoordinate", index=7, width=100)  
    public Float getPlotYCoordinate() {
	return super.getPlotYCoordinate();
    }
 	
    @Display(title="Tree Number", var="TreeNumber", index=8, width=100) 
    public Float getTreeNumber() {
	return super.getTreeNumber();
    }  
    
    
    
}