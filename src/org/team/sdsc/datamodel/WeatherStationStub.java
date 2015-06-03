package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;


/**
 * Represents a TEAM WeatherStationStub.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="weather_station_start_end")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllWeatherStationStubs", 
              query="FROM WeatherStationStub as record")         
})
public class WeatherStationStub {
	
    /**
     * The synthetic database key associated with this unit once it is
     * persisted to a database.
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name="start",nullable=false,unique=false)
    private Date start;

    @Temporal(TemporalType.DATE)
    @Column(name="end",nullable=false,unique=false)
    private Date end;

    
    public Integer getId() {
    	return id;
    }
    
    
    public Date getStart() {
    	return start;
    }
    
    
    public Date getEnd() {
    	return end;
    }
    
}