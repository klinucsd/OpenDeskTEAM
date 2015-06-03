
package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;

/**
 * Represents a TEAM sampling event.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="SAMPLING_EVENTS")
public class SamplingEvent {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /**
     * The name by which this site is known.
     */
    @Column(name="SAMPLING_EVENT",nullable=false,unique=true)
    private String event;

    @OneToOne
    @JoinColumn(name="site_id")
    private Site site;

    @Column(name="public_id")
    private Integer publicId;

    public Integer getId() {
	return id;
    }

    public String getEvent() {
	return event;
    }

    public void setEvent(String event) {
	this.event = event;
    }

    public Site getSite() {
	return this.site;
    }

    public void setSite(Site site) {
	this.site = site;
    }
    
    public Integer getPublicId() {
        return publicId;
    }

    public void setPublicId(Integer id) {
        this.publicId = id;
    }

}
