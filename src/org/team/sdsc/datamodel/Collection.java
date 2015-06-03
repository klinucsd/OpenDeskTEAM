package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;

/**
 * Represents a TEAM person.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="COLLECTIONS")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllCollections", 
              query="from Collection as collection order by collection.id")
})
public class Collection {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="collection_seq_gen")
    @SequenceGenerator(name="collection_seq_gen", sequenceName="collectionserial")
    private Integer id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SITE_Id")
    private Site site;
    
    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name="PROTOCOL_Id")
    private Protocol protocol;
    
    /**
     * The name by which this collection is known.
     */
    @Column(name="COLLECTION_NAME",nullable=false,unique=true)
    private String name;
    
    /**
     * The file name of this collection.
     */
    @Column(name="FILE_NAME",nullable=false,unique=true)
    private String fileName;
    
    @Temporal(TemporalType.TIMESTAMP) 
    @Column(name="LAST_EVENT_AT",nullable=true,unique=false)
    private Date lastEventAt;
       
   
    /**
     * All sampling units by this collection which are found in the database.
     **/
    /*
    @OneToMany
    @OrderBy("name")
    @JoinTable(name="COLLECTION_SAMPLING_UNITS", 
               joinColumns={@JoinColumn(name="COLLECTION_ID")},
               inverseJoinColumns={@JoinColumn(name="SAMPLING_UNIT_ID")})
    private Set<SamplingUnit> samplingUnits;
    */
 

    public Collection() {
    }


    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this collection
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this collection
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }
 
 
    /**
     * Get the site of this collection.
     *
     * @return the site of the collection.
     */
    public Site getSite() {
        return site;
    }

    /**
     * Set the site of this collection.
     *
     * @param site the site of the collection.
     */
    public void setSite(Site site) {
        this.site = site;
    }
   
 
 
    /**
     * Get the name by which this collection is known.
     *
     * @return the name of the collection .
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name by which this collection  is known.
     *
     * @param name the name of the collection .
     */
    public void setName(String name) {
        this.name = name;
    }
 

    /**
     * Get the protocol of this collection.
     *
     * @return the protocol of the collection.
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * Set the protocol of this collection.
     *
     * @param protocol the protocol of the collection.
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
    
    
    /**
     * Get the last event of this collection.
     *
     * @return the last event of the collection.
     */
    public Date getLastEventAt() {
        return lastEventAt;
    }

    /**
     * Set the last event of this collection.
     *
     * @param lastEventAt the last event of the collection.
     */
    public void setLastEventAt(Date lastEventAt) {
        this.lastEventAt = lastEventAt;
    }
 
  
    public String getFileName() {
	return fileName;
    }


    public void setFileName(String fileName) {
	this.fileName = fileName;
    }



    /**
     * Produce a human-readable representation of the collection.
     *
     * @return a textual description of the site.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("name").append("='").append(getName()).append("' ");
        builder.append("site").append("='").append(getSite().getName()).append("' ");
        builder.append("protocol").append("='").append(getProtocol().getName()).append("' ");   
        builder.append("last-event-at").append("='").append(getLastEventAt()).append("' ");
        builder.append("]");
      
        return builder.toString();
    }


}