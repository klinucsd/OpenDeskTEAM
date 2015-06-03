package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.text.*;

/**
 * Represents a TEAM equipment.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="inventory")
public class Camera {
	
    /**
     * The synthetic database key associated with this equipment once it is
     * persisted to a database.
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="type")
    private String type;

    @Column(name="manufacturer")
    private String manufacturer;

    @Column(name="model")
    private String model;

    @Column(name="serial_number")
    private String serialNumber;

    @OneToOne
    @JoinColumn(name="site_id")
    private Site site;

    @Column(name="public_id")
    private Integer publicId;


    public Integer getId() {
	return id;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }
    
    public String getManufacturer() {
	return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
    }

    public String getModel() {
	return model;
    }

    public void setModel(String model) {
	this.model = model;
    }

    public String getSerialNumber() {
	return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
	this.serialNumber = serialNumber;
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