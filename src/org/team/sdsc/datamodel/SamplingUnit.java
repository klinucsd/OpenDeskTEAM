package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.text.*;
import javax.xml.bind.annotation.*;

/**
 * Represents a TEAM sampling unit.
 *
 * @author Kai Lin
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn( 
    name = "UNIT_TYPE", 
    discriminatorType = DiscriminatorType.STRING 
) 
@Table(name="SAMPLING_UNITS")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllSamplingUnits", 
              query="from SamplingUnit as unit order by unit.name")         
})
public class SamplingUnit {
	
    /**
     * The synthetic database key associated with this unit once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    //@GeneratedValue(strategy=GenerationType.AUTO, generator="sampling_unit_seq_gen")
    //@SequenceGenerator(name="sampling_unit_seq_gen", sequenceName="samplingunitserial")
    private Integer id;

    /**
     * The name by which this unit is known.
     */
    @Column(name="UNIT_NAME",nullable=false,unique=true)
    private String name;
    
    /**
     * the latitude of this sampling unit
     */
    //@XmlTransient
    @Column(name="LATITUDE",nullable=true,unique=false)
    private Double lat;
        
    /**
     * the longitude of this sampling unit
     */ 
    //@XmlTransient   
    @Column(name="LONGITUDE",nullable=true,unique=false)
    private Double lon;

    @XmlTransient
    @Column(name="PROPOSED_LATITUDE",nullable=true,unique=false)
    private Double proposedLat;

    @XmlTransient
    @Column(name="PROPOSED_LONGITUDE",nullable=true,unique=false)
    private Double proposedLon;

    @XmlTransient
    @Column(name="method_team",nullable=true,unique=false)   
    private String method;
   
    @XmlTransient
    @Column(name="subplot_number",nullable=true,unique=false)
    private Integer subplotNumber;
    
    @XmlTransient
    @Column(name="plot_x_coordinate", nullable=true, unique=false)   
    private Float plotXCoordinate;

    @XmlTransient   	
    @Column(name="plot_y_coordinate", nullable=true, unique=false)
    private Float plotYCoordinate;

    @XmlTransient
    @Column(name="ima_x_coordinate", nullable=true, unique=false)   
    private Integer imaXCoordinate;
   	
    @XmlTransient
    @Column(name="ima_y_coordinate", nullable=true, unique=false)
    private Integer imaYCoordinate;
       	
    @XmlTransient
    @Column(name="tree_number",nullable=true,unique=false)   
    private Float treeNumber;   

    @XmlTransient
    @Column(name="stratum",nullable=true,unique=false)   
    private String stratum;

    @XmlTransient
    @Column(name="trap_number",nullable=true,unique=false)   
    private Integer trapNumber;

    @XmlTransient
    @Column(name="distance",nullable=true,unique=false)   
    private Float distance;

    @XmlTransient
    @Column(name="bearing",nullable=true,unique=false)   
    private Float bearing;

    @XmlTransient
    @OneToOne
    @JoinColumn(name="PROTOCOL_Id")
    private ProtocolFamily protocolFamily;

    @XmlTransient 
    @OneToOne
    @JoinColumn(name="SITE_Id")
    private Site site; 	
 	
    //@XmlTransient
    @OneToOne
    @JoinColumn(name="IMA_Id")
    private Block block;

    @XmlTransient
    @Temporal(TemporalType.DATE)
    @Column(name="gps_reading_date")
    private Date gpsReadingDate;

    @XmlTransient
    @Column(name="gps_reading_first_name")
    private String gpsReadingFirstName;

    @XmlTransient
    @Column(name="gps_reading_last_name")
    private String gpsReadingLastName;

    @XmlTransient
    @Column(name="gps_number_of_reading")
    private Integer gpsNumberOfReading;

    @XmlTransient
    @Column(name="gps_notes")
    private String gpsNotes;

    @XmlTransient
    @Column(name="gps_make_and_model")
    private String gpsModel;

    @XmlTransient
    @Column(name="x_stake")
    private Integer stakeX;

    @XmlTransient
    @Column(name="y_stake")
    private Integer stakeY;

    public SamplingUnit() {}
 
    public SamplingUnit(Integer Id,
			String name,
			Double latitude,
			Double longitude,
			String method,
			Integer subplotNumber,
			Float plotXCoordinate,
			Float plotYCoordinate,
			Float treeNumber,
			ProtocolFamily protocolFamily,
			Site site,
			Block block) {
 	          
	this.id =Id;
	this.name = name;
	this.lat = latitude;
	this.lon = longitude;
	this.method = method;
	this.subplotNumber = subplotNumber;
	this.plotXCoordinate = plotXCoordinate; 
	this.plotYCoordinate = plotYCoordinate;
	this.treeNumber = treeNumber;
	this.protocolFamily = protocolFamily;
	this.site = site;
	this.block = block;          
    }
 	
 
    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this sampling unit
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }


    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this sampling unit
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Get the name by which this sampling unit is known.
     *
     * @return the name of the sampling unit.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name by which this sampling unit is known.
     *
     * @param name the name of the sampling unit.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Get the latitude of this sampling unit.
     *
     * @return the latitude of the sampling unit.
     */
    public Double getLatitude() {
        return lat;
    }


    /**
     * Set the latitude of this sampling unit.
     *
     * @param latitude the latitude of the sampling unit.
     */
    public void setLatitude(Double lat) {
        this.lat = lat;
    }
    
    
    /**
     * Get the longitude of this sampling unit.
     *
     * @return the longitude of the sampling unit.
     */
    public Double getLongitude() {
        return lon;
    }


    /**
     * Set the longitude of this sampling unit.
     *
     * @param longitude the longitude of the sampling unit.
     */
    public void setLongitude(Double lon) {
        this.lon = lon;
    }
    
        
    /**
     * Get the protocol family of this sampling unit.
     *
     * @return the protocol family of the sampling unit.
     */
    public ProtocolFamily getProtocolFamily() {
        return protocolFamily;
    }


    /**
     * Set the protocol family of this sampling unit.
     *
     * @param protocolFamily the protocol family of the sampling unit.
     */
    public void setProtocolFamily(ProtocolFamily protocolFamily) {
        this.protocolFamily = protocolFamily;
    }
  
      
    /**
     * Get the site of this sampling unit.
     *
     * @return the site of the sampling unit.
     */
    public Site getSite() {
        return site;
    }
   

    /**
     * Set the site of this sampling unit.
     *
     * @param site the site of the sampling unit.
     */
    public void setSite(Site site) {
        this.site = site;
    }
  
  
    public Block getBlock() {
	return block;
    }
  

    public void setBlock(Block block) {
	this.block = block;
    }

 
    public String getMethod() {
	return method;
    }
 	
 	
    public Integer getSubplotNumber() {
	return subplotNumber;
    }


    public void setSubplotNumber(Integer subplotNumber) {
	this.subplotNumber = subplotNumber;
    }

 	
    public Float getPlotXCoordinate() {
	return plotXCoordinate;
    }


    public void setPlotXCoordinate(Float plotXCoordinate) {
        this.plotXCoordinate =  plotXCoordinate;
    }
 	 
 	 
    public Float getPlotYCoordinate() {
	return plotYCoordinate;
    }


    public void setPlotYCoordinate(Float plotYCoordinate) {
        this.plotYCoordinate =  plotYCoordinate;
    }

 	
    public Integer getImaXCoordinate() {
	return imaXCoordinate;
    }
 	 
 	 
    public Integer getImaYCoordinate() {
	return imaYCoordinate;
    }
 	
 	
    public Float getTreeNumber() {
	return treeNumber;
    }


    public void setTreeNumber(Float treeNumber) {
	this.treeNumber = treeNumber;
    }
 	

    public boolean sameLocation(SamplingUnit unit) {

	if (this.lat != null && this.lon != null && unit.lat != null && unit.lon != null) {
	    return this.lat.floatValue() == unit.lat.floatValue() &&
		this.lon.floatValue() == unit.lon.floatValue();
	} else {
	    return false;
	}

    }


    public String getStratum() {
	return stratum;
    }


    public Integer getTrapNumber() {
	return trapNumber;
    }


    static public String getSortPath(String string) {
	String result = "id";
	if (string.equals("Id")) {
	    result = "id";
	} else if (string.equals("unit_name")) {
	    result = "name";
	} else if (string.equals("unit_type")) {
	    result = "id";
	} else if (string.equals("proposed_latitude")) {
	    result = "proposedLat";
	} else if (string.equals("proposed_longitude")) {
	    result = "proposedLon";
	} else if (string.equals("latitude")) {
	    result = "lat";
	} else if (string.equals("longitude")) {
	    result = "lon";
	} else if (string.equals("ima_name")) {
	    result = "id";
	} else if (string.equals("ima_x_coordinate")) {
	    result = "imaXCoordinate";
	} else if (string.equals("ima_y_coordinate")) {
	    result = "imaYCoordinate";
	} else if (string.equals("method_team")) {
	    result = "method";
	} else if (string.equals("gps_reading_date")) {
	    result = "gpsReadingDate";
	} else if (string.equals("gps_number_of_reading")) {
	    result = "gpsNumberOfReading";
	} else if (string.equals("gps_notes")) {
	    result = "gpsNotes";
        } else if (string.equals("gps_make_and_model")) {
	    result = "gpsModel";
        } else if (string.equals("gps_reading_person")) {
	    result = "gpsReadingLastName";
	}

	return result;
    }



    public void setValue(String name, String value) throws Exception {
	if (name.equals("latitude")) {
	    if (value == null) {
		lat = null;
	    } else {
		lat = new Double(value);
	    }
	} else if (name.equals("longitude")) {
	    if (value == null) {
		lon = null;
	    } else {
		lon = new Double(value);
	    }
	} else if (name.equals("proposed_latitude")) {
	    if (value == null) {
		proposedLat = null;
	    } else {
		proposedLat = new Double(value);
	    }
	} else if (name.equals("proposed_longitude")) {
	    if (value == null) {
		proposedLon = null;
	    } else {
		proposedLon = new Double(value);
	    }
	} else if (name.equals("method_team")) {
	    method = value;
	} else if (name.equals("gps_reading_date")) {
	    if (value == null) {
		gpsReadingDate = null;
	    } else {
		int index = value.indexOf("GMT");
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
		if (index != -1) value = value.substring(0, index);
		gpsReadingDate = formatter.parse(value);
	    }
	} else if (name.equals("gps_number_of_reading")) {
	    if (value == null) {
		gpsNumberOfReading = null;
	    } else {
		gpsNumberOfReading = new Integer(value);
	    }
	} else if (name.equals("gps_notes")) {
	    gpsNotes = value;
	} else if (name.equals("gps_reading_person")) {
	    if (value == null) {
		gpsReadingFirstName = null;
		gpsReadingLastName = null;
	    } else {
		int p = value.indexOf(",");
		if (p != -1) {
		    gpsReadingFirstName = value.substring(p+1);
		    gpsReadingLastName = value.substring(0, p);
		}
	    }
	} else if (name.equals("gps_make_and_model")) {
            gpsModel = value;
        }

    }


    public void setProposedLatitude(Double lat) {
	this.proposedLat = lat;
    }



    public void setProposedLongitude(Double lon) {
	this.proposedLon = lon;
    }


    public Double getProposedLatitude() {
	return this.proposedLat;
    }


    public Double getProposedLongitude() {
	return this.proposedLon;
    }


    public Date getGpsReadingDate() {
	return gpsReadingDate;
    }


    public void setGpsReadingDate(Date date) {
	this.gpsReadingDate = date;
    }

    
    public String getGpsReadingFirstName() {
	return gpsReadingFirstName;
    }

    
    public void setGpsReadingFirstName(String firstName) {
        gpsReadingFirstName = firstName;
    }

    
    public String getGpsReadingLastName() {
	return gpsReadingLastName;
    }

    
    public void setGpsReadingLastName(String lastName) {
        gpsReadingLastName = lastName;
    }

    
    public Integer getGpsNumberOfReading() {
	return gpsNumberOfReading;
    }


    public void setGpsNumberOfReading(Integer aInt) {
	gpsNumberOfReading = aInt;
    }


    public String getGpsNotes() {
	return gpsNotes;
    }


    public void setGpsNotes(String notes) {
	this.gpsNotes = notes;
    }


    public String getGpsModel() {
	return gpsModel;
    }


    public void setGpsModel(String model) {
	this.gpsModel = model;
    }


    public Float getDistance() {
	return distance;
    }


    public void setDistance(Float distance) {
	this.distance = distance;
    }


    public Float getBearing() {
	return bearing;
    }


    public void setBearing(Float bearing) {
	this.bearing = bearing;
    }


    public Integer getStakeX() {
	return stakeX;
    }


    public Integer getStakeY() {
	return stakeY;
    }


    public void setStakeX(Integer stakeX) {
	this.stakeX = stakeX;
    }


    public void setStakeY(Integer stakeY) {
	this.stakeY = stakeY;
    }


    /**
     * Produce a human-readable representation.
     *
     * @return a textual description of the sampling unit.
     */
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(id).append("' ");
        builder.append("name").append("='").append(getName()).append("' ");  
        builder.append("latitude").append("='").append(lat).append("' ");  
        builder.append("longitude").append("='").append(lon).append("' ");    
        builder.append("proposed latitude").append("='").append(proposedLat).append("' ");      
        builder.append("proposed longitude").append("='").append(proposedLon).append("' ");  
	if (block != null) builder.append("block").append("='").append(block.getName()).append("' ");   
        builder.append("site").append("='").append(site.getName()).append("' ");   
        builder.append("protocol family").append("='").append(protocolFamily).append("' ");   
        builder.append("gps model").append("='").append(gpsModel).append("' ");   
	if (stakeX != null) builder.append("stakeX").append("='").append(stakeX).append("' ");      
	if (stakeY != null) builder.append("stakeY").append("='").append(stakeY).append("' ");   

        builder.append("]");
     
        return builder.toString();
    }




}