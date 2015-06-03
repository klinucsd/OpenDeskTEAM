
package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;

/**
 * Represents a TEAM site.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="SITES_TEAM")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllSites", 
              query="from Site as site order by site.name"),
  @NamedQuery(name="org.team.sdsc.datamodel.siteByStatus", 
              query="from Site as site where upper(site.status)=upper(:status) order by site.name"),
  @NamedQuery(name="org.team.sdsc.datamodel.siteByName", 
              query="from Site as site where upper(site.name)=upper(:name)"),
  @NamedQuery(name="org.team.sdsc.datamodel.siteById", 
              query="from Site as site where site.id=:id")           
})
public class Site {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="SITE_ID")
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /**
     * The name by which this site is known.
     */
    @Column(name="SITE_NAME",nullable=false,unique=true)
    private String name;
  
    /**
     * The abbreviation name of this site.
     */
    @Column(name="SITE_ABBV",nullable=false,unique=true)
    private String abbv;
  
    /**
     * The status of this site.
     */
    @Column(name="SITE_STATUS",nullable=false)
    private String status;
  
    /**
     * The short name of this site.
     */
    @Column(name="SHORT_NAME",nullable=true,unique=true)
    private String shortName;

    /**
     * The time zone of this site.
     */
    @Column(name="TIME_ZONE",nullable=true,unique=true)
    private String timeZone;

    @Column(name="LATITUDE")
    private Float latitude;

    @Column(name="LONGITUDE")
    private Float longitude;

    @Column(name="site_institution_name")
    private String instName;

 
    /**
     * All protocols by this site which are found in the database.
     */
    @OneToMany
    @OrderBy("name")
    @JoinTable(name="SITE_PROTOCOL", 
               joinColumns={@JoinColumn(name="SITE_ID")},
               inverseJoinColumns={@JoinColumn(name="PROTOCOL_ID")})
    private Set<Protocol> protocols;

    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;

    @Column(name="last_event_by")
    private Integer lastEventBy;

    @Transient
    private List<Block> blocks;

    //@Transient
    //private Country country;

    @Column(name="public_id")
    private Integer publicId;

 
    public Country getCountry() {
	return country;
    }
    
    
    public void setCountry(Country country) {
	this.country = country;
    }
 


    /**
     * All blocks at this site which are found in the database.
     */    
    /*
    @OneToMany(mappedBy="site", fetch = FetchType.LAZY)
    @OrderBy("name")   
    @JoinColumn(name="SITE_ID")
	private Set<Block> blocks;
    */

    /**
     * All institutions at this site which are found in the database.
     */ 
    /*
    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("name")   
    @JoinTable(name="institutions_sites_team", 
               joinColumns={@JoinColumn(name="site_id")},
               inverseJoinColumns={@JoinColumn(name="institution_id")})    
	private Set<Institution> institutions; 
    */
      
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public Site() {}

    /**
     * Create a new Site.
     *
     * @param name the name by which the site is known.
     * @param abbv the abbreviation of the site.
     * @param status the status of the site.	
     * @param shortName the short name of the site.
     */
    public Site(String name, String abbv, String status, String shortName) {
        this.name = name;
        this.abbv = abbv;
        this.status = status;
        this.shortName = shortName;
    }

    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this site
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this site
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getPublicId() {
        return publicId;
    }


    public void setPublicId(Integer id) {
        this.publicId = id;
    }


    /**
     * Get the name by which this site is known.
     *
     * @return the name of the site.
     */
    @Display(title="Site Name", var="SiteName", index=1, width=100)
    public String getName() {
        return name;
    }

    /**
     * Set the name by which this site is known.
     *
     * @param name the name of the site.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Get the abbv of this site.
     *
     * @return the abbv of the site.
     */
    public String getAbbv() {
        return abbv;
    }

    /**
     * Set the abbv of this site.
     *
     * @param abbv the abbv of the site.
     */
    public void setAbbv(String abbv) {
        this.abbv = abbv;
    }


    /**
     * Get the status of this site.
     *
     * @return the status of the site.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of this site.
     *
     * @param status the status of the site.
     */
    public void setStatus(String status) {
        this.status = status;
    }



    public boolean isActive() {
	return this.status != null && this.status.toLowerCase().equals("active");
    }


    public boolean inSetup() {
	return this.status != null && this.status.toLowerCase().equals("setup");
    }


    /**
     * Get the short name by which this site is known.
     *
     * @return the short name of the site.
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Set the short name by which this site is known.
     *
     * @param shortName the short name of the site.
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    /**
     *  List all protocols installed in this site
     *
     */    
    public Set<Protocol> getProtocols() {
    	return protocols;
    }



    public Protocol getProtocol(String protocolName) {
	Protocol protocol = null;
	for (Protocol aProtocol : protocols) {
	    if (aProtocol.getName().equals(protocolName)) {
		protocol = aProtocol;
		break;
	    }
	}
    	return protocol;
    }



    public Protocol getProtocol(Integer aInt) {
	Protocol protocol = null;
	for (Protocol aProtocol : protocols) {
	    if (aProtocol.getId().equals(aInt)) {
		protocol = aProtocol;
		break;
	    }
	}
    	return protocol;
    }



    /**
     *  Get a protocol installed in this site with the id
     *
     *  @param id the id of the protocol
     */    
    public Protocol getProtocolById(int id) {
    	Protocol aProtocol = null;
        for (Iterator i=protocols.iterator(); i.hasNext(); ) {
    	    Protocol protocol = (Protocol)i.next();
    	    if (protocol.getId().intValue() == id) {
    	        aProtocol = protocol;
    	        break;
    	    }
        }
        return aProtocol;
    }


    public String getTimeZone() {
	return timeZone;
    }


    public Integer getLastEventBy() {
	return lastEventBy;
    }


    public Date getLastEventAt() {
	return lastEventAt;
    }


    /**
     *  List all blocks installed in this site
     *
     */    
    /*
    public Set<Block> getBlocks() {
    	return blocks;
    }
    */

    /**
     *  List all institutions at this site
     *
     */ 
    /*
    public Set<Institution> getInstitutions() {
    	return institutions;
    }
    */

    /**
     *  Get the leader institution at this site
     *
     */  
    /*
    public Institution getLeaderInstitution() {
    	if (institutions.isEmpty()) {
    		return null;
    	} else {
    		return (Institution)institutions.iterator().next();
        }
    }
    */



    /**
     *  List all blocks installed in this site for the protocol
     *
     */ 
    /*
    public Set<Block> getBlocks(Protocol protocol) {
    
    	Set<Block> blockSet = new HashSet<Block>();
        for (Iterator i=blocks.iterator(); i.hasNext(); ) {
        	Block aBlock = (Block)i.next();
        	Set<SamplingUnit> units = aBlock.getSamplingUnits();
        	for (Iterator j=units.iterator(); j.hasNext(); ) {
        		SamplingUnit unit = (SamplingUnit)j.next();
        		if (unit.getProtocolFamily().getName().equals(protocol.getName())) {
        			blockSet.add(aBlock);
        			break;
        		}
        	}
        }
    
    	return blockSet;
    }
    */
    

    /**
     * Get a block by its id
     *
     **/
    /*
    public Block getBlockById(int id) {
    	Block aBlock = null; 
    	for (Iterator i=blocks.iterator(); i.hasNext(); ) {
        	Block block = (Block)i.next();
        	if (block.getId().intValue() == id) {
        		aBlock = block;
        		break;
        	}
    	}
    	return aBlock;
    }
    */


    /**
     *  List all sampling units installed in this site for the protocol
     *
     */ 
    /*
    public Set<SamplingUnit> getSamplingUnits(Protocol protocol) {
    	Set<SamplingUnit> units = new HashSet<SamplingUnit>();
    	Set<Block> blocks = getBlocks(protocol);
    	for (Iterator i=blocks.iterator(); i.hasNext(); ) {
        	Block aBlock = (Block)i.next();
        	units.addAll(aBlock.getSamplingUnits());
    	}
    	return units;
    }
    */


    public List<Block> getBlocks() {
	return blocks;
    }
	
	
    public void setBlocks(List<Block> blocks) {
	this.blocks = blocks;
    }


    public void setProtocols(Set<Protocol> protocols) {
	this.protocols = protocols;
    }

    /*
    public Country getCountry() {
	return country;
    }
    */


    /**
     * Get a clone of this site
     *
     */
    public Site clone() {
	Site site = new Site();
	site.id = this.id;
	site.name = this.name;
	site.abbv = this.abbv;
	site.status = this.status;
	site.shortName = this.shortName;
 	site.protocols = this.protocols;
	site.blocks = new ArrayList<Block> ();
	//site.countryId = this.countryId;
	site.country = this.country;
	return site;
    }

    public Float getLatitude() {
	return latitude;
    }

    public void setLatitude(Float latitude) {
	this.latitude = latitude;
    }

    public Float getLongitude() {
	return longitude;
    }

    public void setLongitude(Float longitude) {
	this.longitude = longitude;
    }


    public String getInstName() {
        return instName;
    }

    public void setInstName(String name) {
        this.instName = name;
    }

    public void setLastEventBy(Integer lastEventBy) {
	this.lastEventBy = lastEventBy;
    }

 
    /**
     * Produce a human-readable representation of the site.
     *
     * @return a textual description of the site.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("name").append("='").append(getName()).append("' ");
        builder.append("abbv").append("='").append(getAbbv()).append("' ");
        builder.append("status").append("='").append(getStatus()).append("' ");
        builder.append("shortName").append("='").append(getShortName()).append("' ");
 
        if (protocols != null) {
            builder.append("protocols").append("='"); 
        	for (Iterator i=protocols.iterator(); i.hasNext(); ) {
        		Protocol aProtocol = (Protocol)i.next();
        		builder.append(aProtocol.getName()+"."+aProtocol.getVersion()).append(", ");
        	}
        	builder.append("' ");	
        }
        
        builder.append("]");
      
        return builder.toString();
    }

}
