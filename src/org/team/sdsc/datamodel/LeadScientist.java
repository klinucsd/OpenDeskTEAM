package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.AnnotationConfiguration;


/**
 * Represents a TEAM person.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="LEAD_SCIENTIST")
@PrimaryKeyJoinColumn(name = "PERSON_ID") 
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllLeadScientists", 
              query="from LeadScientist as leadScientist order by leadScientist.username"),
  @NamedQuery(name="org.team.sdsc.datamodel.LeadScientistById", 
              query="from LeadScientist as leadScientist where leadScientist.id=:id")                     
})
public class LeadScientist extends Person{

           
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public LeadScientist() {
    	super();
    }


    /**
     * Create a new LeadScientist.
     *
     * @param username the user name of the person.	
     * @param firstName the first name of the person.
     * @param lastName the last name of the person.
     */
    public LeadScientist(String username, String firstName, String lastName) {
        super(username, firstName, lastName);
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
        builder.append("user name").append("='").append(getUserName()).append("' ");
        builder.append("]");
      
        return builder.toString();
    }

}
