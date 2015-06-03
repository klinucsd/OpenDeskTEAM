
package org.team.sdsc.datamodel.util;

import java.util.*;
import java.text.*;
import javax.xml.bind.annotation.*;
import org.team.sdsc.datamodel.*;


/**
 * Represents a TEAM sampling unit.
 *
 * @author Kai Lin
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@XmlRootElement( name="SamplingUnit" )
public class SamplingUnitDelegate {

    private Integer id;
    private String name;
    private Double lat;
    private Double lon;
    private Double proposedLat;
    private Double proposedLon;
    private String method;
    private Integer subplotNumber;
    private Float plotXCoordinate;
    private Float plotYCoordinate;
    private Integer imaXCoordinate;
    private Integer imaYCoordinate;
    private Float treeNumber;   
    private String stratum;
    private Integer trapNumber;
    private Float distance;
    private Float bearing;

    @XmlTransient
    private ProtocolFamily protocolFamily;

    @XmlTransient 
    private Site site; 	
 	
    @XmlTransient
    private Block block;

    private Date gpsReadingDate;
    private String gpsReadingFirstName;
    private String gpsReadingLastName;
    private Integer gpsNumberOfReading;
    private String gpsNotes;
    private String gpsModel;
    private Integer stakeX;
    private Integer stakeY;

    public SamplingUnitDelegate() {}
 
    public SamplingUnitDelegate(SamplingUnit unit) {

	this.id = unit.getId();
	this.name = unit.getName();
	this.lat = unit.getLatitude();
	this.lon = unit.getLongitude();
	this.proposedLat = unit.getProposedLatitude();
	this.proposedLon = unit.getProposedLongitude();
	this.method = unit.getMethod();
	this.subplotNumber = unit.getSubplotNumber();
	this.plotXCoordinate = unit.getPlotXCoordinate();
	this.plotYCoordinate = unit.getPlotYCoordinate();
	this.imaXCoordinate = unit.getImaXCoordinate();
	this.imaYCoordinate = unit.getImaYCoordinate();
	this.treeNumber = unit.getTreeNumber();   
	this.stratum = unit.getStratum();
	this.trapNumber = unit.getTrapNumber();
	this.distance = unit.getDistance();
	this.bearing = unit.getBearing();
	this.protocolFamily = unit.getProtocolFamily();
	this.site = unit.getSite(); 	
	this.block = unit.getBlock();
	this.gpsReadingDate = unit.getGpsReadingDate();
	this.gpsReadingFirstName = unit.getGpsReadingFirstName();
	this.gpsReadingLastName = unit.getGpsReadingLastName();
	this.gpsNumberOfReading = unit.getGpsNumberOfReading();
	this.gpsNotes = unit.getGpsNotes();
	this.gpsModel = unit.getGpsModel();
	this.stakeX = unit.getStakeX();
	this.stakeY = unit.getStakeY();

    }
 	



}