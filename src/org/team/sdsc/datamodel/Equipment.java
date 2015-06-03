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
public class Equipment {
	
    /**
     * The synthetic database key associated with this equipment once it is
     * persisted to a database.
     */
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="type")
    private String type;

    @Temporal(TemporalType.DATE)
    @Column(name="purchase_date")
    private Date purchaseDate;

    @Column(name="cost")
    private Float cost;

    @Column(name="condition")
    private String condition;

    @Temporal(TemporalType.DATE)
    @Column(name="decomission_date")
    private Date decomissionDate;

    @Column(name="reason")
    private String reason;

    @Column(name="manufacturer")
    private String manufacturer;

    @Column(name="model")
    private String model;

    @Column(name="serial_number")
    private String serialNumber;

    @OneToOne
    @JoinColumn(name="site_assigned_to")
    private Site site;


    public Integer getId() {
	return id;
    }


    public String getType() {
	return type;
    }


    public Date getPurchaseDate() {
	return purchaseDate;
    }


    public Float getCost() {
	return cost;
    }


    public String getCondition() {
	return condition;
    }


    public Date getDecomissionDate() {
	return decomissionDate;
    }


    public String getReason() {
	return reason;
    }


    public String getManufacturer() {
	return manufacturer;
    }


    public String getModel() {
	return model;
    }


    public String getSerialNumber() {
	return serialNumber;
    }


    public Site getSite() {
	return site;
    }


}