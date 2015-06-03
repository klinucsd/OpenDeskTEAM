package org.team.sdsc.datamodel;

import org.hibernate.validator.*;
import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;
import javax.xml.bind.annotation.*;

/**
 * Represents a TEAM person.
 *
 * @author Kai Lin
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
@Table(name="PERSON")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllPersons", 
              query="from Person as person order by person.username"),
  @NamedQuery(name="org.team.sdsc.datamodel.PersonByUsername", 
              query="from Person as person where upper(person.username)=upper(:username)"),
  @NamedQuery(name="org.team.sdsc.datamodel.PersonById", 
              query="from Person as person where person.id=:id")
})
public class Person {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="PERSON_ID")
    //@GeneratedValue(strategy=GenerationType.AUTO, generator="person_seq_gen")
    //@SequenceGenerator(name="person_seq_gen", sequenceName="PERSONSERIAL")
    private Integer id;
 
     /**
     * The user name of the person.
     */
    //@NotNull
    @Length(min = 2)
    @Column(name="USERNAME")
    private String username;
 
    /**
     * The first name of the person.
     */    
    @Column(name="FIRST_NAME",nullable=false,unique=false)
    private String firstName;
  
    /**
     * The last name of this person.
     */  
    @Column(name="LAST_NAME",nullable=false,unique=false)
    private String lastName;
  
    /**
     * The address of this person.
     */
    @XmlTransient
    @Column(name="ADDRESS",nullable=true,unique=false)
    private String address;
  
    /**
     * The phone number of this person.
     */
    @XmlTransient
    @Column(name="PHONE_NUMBER",nullable=true,unique=true)
    private String phoneNumber;
 
    /**
     * The email of this person.
     */
    @Email
    @Column(name="EMAIL",nullable=true,unique=true)
    private String email; 
 
     /**
     * The status of this person.
     */
    @XmlTransient
    @Column(name="ACTIVE",nullable=true,unique=false)
    private boolean active;
 
    /**
     * The role of the person.
     */
    @XmlTransient
    @Column(name="WEB_ROLE",nullable=true,unique=false)
    private String role; 
 
     /**
     * The city of the person.
     */
    @XmlTransient
    @Column(name="CITY",nullable=true,unique=false)
    private String city;
 
     /**
     * The province of the person.
     */
    @XmlTransient
    @Column(name="PROVINCE",nullable=true,unique=false)
    private String province;
 
    /**
     * The postal code of the person.
     */
    @XmlTransient
    @Column(name="POSTAL_CODE",nullable=true,unique=false)
    private String postalCode;
                 
    /**
     * The country of the person.
     */
    @XmlTransient
    @Column(name="COUNTRY",nullable=true,unique=false)
    private String country;

    /**
     * The timestamp of the person.
     */
    @XmlTransient
    @Column(name="TIMESTAMP",nullable=true,unique=false)
    private Date timestamp;


    /**
     * All sites by this person which are found in the database.
     */
    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="PERSON_SITE", 
               joinColumns={@JoinColumn(name="PERSON_ID")},
               inverseJoinColumns={@JoinColumn(name="SITE_ID")})
    private Set<Site> sites;

    /**
     * All protocol families by this person which are found in the database.
     */
    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="PERSON_PROTOCOL", 
               joinColumns={@JoinColumn(name="PERSON_ID")},
               inverseJoinColumns={@JoinColumn(name="PROTOCOL_ID")})
    private Set<ProtocolFamily> protocolFamilies;

        
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public Person() {}

    /**
     * Create a new Person.
     *
     * @param username the user name of the person.	
     * @param firstName the first name of the person.
     * @param lastName the last name of the person.
     */
    public Person(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this person
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this person
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the first name of this person.
     *
     * @return the first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name.
     *
     * @param name the first name of the person.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of this person.
     *
     * @return the last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name.
     *
     * @param name the last name of the person.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the user name of this person.
     *
     * @return the user name of the person.
     */
    public String getUserName() {
        return username;
    }

    /**
     * Set the user name.
     *
     * @param name the user name of the person.
     */
    public void setUserName(String userName) {
        this.username = userName;
    }


    /**
     * Get the status of this site.
     *
     * @return the status of the site.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the status of this site.
     *
     * @param status the status of the site.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the address of this person.
     *
     * @return the address of the person.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address.
     *
     * @param address the address of the person.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the email of this person.
     *
     * @return the email of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email.
     *
     * @param email the email of the person.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the phone number of this person.
     *
     * @return the phone number of the person.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number.
     *
     * @param phone the phone number of the person.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Get the country of this person.
     *
     * @return the country of the person.
     */
    public String getCountry() {
	return country;
    }


    /**
     * Get the province of this person.
     *
     * @return the province of the person.
     */
    public String getProvince() {
	return province;
    }


    /**
     * Get the city of this person.
     *
     * @return the city of the person.
     */
    public String getCity() {
	return city;
    }


    /**
     * Get the postal code of this person.
     *
     * @return the postal code of the person.
     */
    public String getPostalCode() {
	return postalCode;
    }


    /**
     * Get the time stamp of this person.
     *
     * @return the time stamp of the person.
     */
    public Date getTimestamp() {
	return timestamp;
    }



    /**
     * Get the web role of this person.
     *
     * @return the web role of the person.
     */
    public String getWebRole() {
	return role;
    }



    public Set<Site> getSites() {
    	return sites;
    }


    public void setSites(Set<Site> sites) {
	this.sites = sites;
    }

    
    /*
     * Get a list of roles assigned to this person
     *
     */
    public String[] getRoles() {
	ArrayList<String> roles = new ArrayList<String>();
	if (role != null) {
	    StringTokenizer st = new StringTokenizer(role, "/");
	    while (st.hasMoreTokens()) {
		String token = st.nextToken().trim().toLowerCase();
		roles.add(token);
	    }
	}
	String[] results = new String[roles.size()];
        roles.toArray(results);
	return results;
    }


    public Set<ProtocolFamily> getProtocolFamilies() {
    	return protocolFamilies;
    }


    /*
     * Get a list of protocols this person is working on
     *
     */
    public Set<Protocol> getProtocols(Site site) {
	Set<Protocol> result = new TreeSet<Protocol>(new ProtocolComparator());
	for (Protocol protocol : site.getProtocols()) {
	    // check this protocol is in the protocol list assigned to this user
	    boolean found = false;
	    for (ProtocolFamily family : protocolFamilies) {
		if (family.getName().equals(protocol.getName()) && !family.getName().endsWith("Maintenance")) {
		    found = true;
		    break;
		}
	    }
	    if (found) result.add(protocol);
	}
	return result;
    }


    @Display(title="Protocol Lead Scientist", var="ProtocolLeadScientist", index=1, width=100)
    public String getFullNameForLeader() {
	if (firstName != null && lastName != null) {
	    return firstName+"  "+lastName;
	} else {
	    return null;
	}
    }


    @Display(title="Protocol Lead Scientist Institution", var="ProtocolLeadScientistInstitution", index=2, width=100)
    public String getInstitutionNameForLeader() {
	return null;
    }


    public boolean hasRole(String role) {
	if (role != null) {
	    String[] roles = this.getRoles();
	    for (int i=0; i<roles.length; i++) {
		if (roles[i].toLowerCase().equals(role.toLowerCase())) {
		    return true;
		}
	    }
	}
	return false;
    }


    public void setRole(String role) {
	this.role = role;
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
        builder.append("user name").append("='").append(username).append("' ");
        builder.append("first name").append("='").append(firstName).append("' ");
        builder.append("last name").append("='").append(lastName).append("' ");
        builder.append("active").append("='").append(active).append("' "); 
        builder.append("email").append("='").append(email).append("' "); 
        if (phoneNumber != null) 
        	builder.append("phone number").append("='").append(phoneNumber).append("' ");
        if (address != null)
	        builder.append("address").append("='").append(address).append("' ");
        if (city != null)
	        builder.append("city").append("='").append(city).append("' ");
	    if (province != null)
	        builder.append("province").append("='").append(province).append("' ");
	    if (postalCode != null) 
        	builder.append("postalCode").append("='").append(postalCode).append("' ");
        if (country != null)	
  	        builder.append("country").append("='").append(country).append("' ");
        if (role != null) 
            builder.append("role").append("='").append(role).append("' ");
        if (sites != null) {
            builder.append("sites").append("='"); 
        	for (Iterator i=sites.iterator(); i.hasNext(); ) {
        		Site aSite = (Site)i.next();
        		builder.append(aSite.getName()).append(", ");
        	}
        	builder.append("' ");	
        }
        if (protocolFamilies != null) {
            builder.append("protocol families").append("='"); 
        	for (Iterator i=protocolFamilies.iterator(); i.hasNext(); ) {
        		ProtocolFamily aProtocolFamily = (ProtocolFamily)i.next();
        		builder.append(aProtocolFamily.getName()).append(", ");
        	}
        	builder.append("' ");	
        }
        
        builder.append("]");
      
        return builder.toString();
    }

}
