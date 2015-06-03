package org.team.sdsc.datamodel;

import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM plot metadata.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="plot_metadata")
public class PlotMetadata {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="plot_meta_seq_gen")
    @SequenceGenerator(name="plot_meta_seq_gen", sequenceName="plotmetaserial")
    private Integer id;

    @Column(name="site_id")
    private Integer siteId;

    @Column(name="plot_no")
    private Integer plotNumber;

    @Column(name="date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="comments")
    private String comments;


    public PlotMetadata() {
    }


    public void setSiteId(Integer siteId) {
	this.siteId = siteId;
    }


    public void setPlotNumber(Integer plotNumber) {
	this.plotNumber = plotNumber;
    }


    public void setDate(Date date) {
	this.date = date;
    }


    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }


    public void setLastName(String lastName) {
	this.lastName = lastName;
    }


    public void setComments(String comments) {
	this.comments = comments;
    }


    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(getClass().getName()).append("@");
	builder.append("id").append("='").append(id).append("' ");
	builder.append("site id").append("='").append(siteId).append("' ");
	builder.append("plot no").append("='").append(plotNumber).append("' ");
	builder.append("date").append("='").append(date).append("' ");
	builder.append("first name").append("='").append(firstName).append("' ");
	builder.append("last name").append("='").append(lastName).append("' ");
	builder.append("comments").append("='").append(comments).append("' ");
	builder.append("]");
      
	return builder.toString();
    }


}