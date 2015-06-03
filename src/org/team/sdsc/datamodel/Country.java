package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;

/**
 * Represents a TEAM country.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="COUNTRIES")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllCountries", 
              query="from Country as country order by country.name")
})
public class Country {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="country_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    @Column(name="country_name")
    private String name;

    @Column(name="country_code")
    private String code;


    public Integer getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getCode() {
	return code;
    }

}
