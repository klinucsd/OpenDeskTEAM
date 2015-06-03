package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM Institution.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="institution")
public class Institution {

    /**
     * The synthetic database key associated with this institution once it is
     * persisted to a database.
     */
    @Id
    @Column(name="institution_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
 
     /**
     * The name of the institution.
     */
    @Column(name="institution_name",nullable=false,unique=false)
    private String name;
 
    /**
     * The address of this institution.
     */
    @Column(name="institution_address",nullable=true)
    private String address;
  
    /**
     * The phone number of this institution.
     */
    @Column(name="institution_phone",nullable=true)
    private String phoneNumber;
 
    /**
     * The email of this institution.
     */
    @Column(name="institution_email",nullable=true)
    private String email; 
 
     /**
     * The city of the institution.
     */
    @Column(name="institution_city",nullable=true)
    private String city;
 
     /**
     * The province of the institution.
     */
    @Column(name="institution_state",nullable=true)
    private String state;
 
                  
    /**
     * The country of the institution.
     */
    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;
                  
    /**
     * The last event time of the institution.
     */
    //@Temporal(TemporalType.TIMESTAMP)     
    //@Column(name="last_event_at",nullable=true,unique=false) 
    //private Date lastEventAt;

    /**
     * The last event person of the institution.
     */
    //@Column(name="last_event_by",nullable=true,unique=false) 
    //private String lastEventBy;
    
    /**
     * The url of the website of the institution.
     */   
    //@Column(name="institution_website",nullable=true,unique=false)
    //private String website;

    /**
     * The site of the institution.
     */       
    @OneToOne
    @JoinTable(name="institution_site", 
               joinColumns={@JoinColumn(name="institution_id")},
               inverseJoinColumns={@JoinColumn(name="site_id")})
    private Site site;

    /**
     * The affiliation of the institution.
     */
    //@Column(name="institution_affiliation",nullable=true,unique=false)
    //private String affiliation;

    /**
     * The fax of the institution.
     */
    //@Column(name="institution_fax",nullable=true,unique=false)
    //private String fax;

    /**
     * The zipcode of the institution.
     */
    @Column(name="institution_zip",nullable=true,unique=false)
    private String zipcode;


    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this institution
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }


    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this institution
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Get the name of this institution.
     *
     * @return the name of the institution.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name the name of the institution.
     */
    public void setName(String name) {
        this.name = name;
    }


    public Site getSite() {
	return site;
    }


    public void setSite(Site site) {
	this.site = site;
    }


    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }


    public String getPhoneNumber() {
	return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }
 
    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }
  
    public Country getCountry() {
	return country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

   
    /*
    public String getWebsite() {
	return website;
    }


    public String getAffiliation() {
	return affiliation;
    }


    public String getFax() {
	return fax;
    }
    */


    public String getZipcode() {
	return zipcode;
    }


    public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
    }

    /**
     * Produce a human-readable representation of the person.
     *
     * @return a textual description of the person.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("name").append("='").append(name).append("' ");
        if (site != null) 
        	builder.append("site").append("='").append(site.getName()).append("' ");
        builder.append("]");
      
        return builder.toString();
    }




}