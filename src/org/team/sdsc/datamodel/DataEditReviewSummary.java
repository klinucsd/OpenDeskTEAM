
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.text.*;

/**
 * Represents an edit review summary.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="edit_review_summary")
public class DataEditReviewSummary {


    @Id 
    @AttributeOverrides({ 
    	@AttributeOverride(name   = "site", 
                       	   column = @Column(name="site", unique=false) ), 
        @AttributeOverride(name   = "protocol", 
                           column = @Column(name="protocol", unique=false) ), 
        @AttributeOverride(name   = "block", 
                           column = @Column(name="block", unique=false) ),
        @AttributeOverride(name   = "event", 
                           column = @Column(name="event", unique=false) )
                           
    }) 
    private DataEditReviewSummaryId id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="edited_at")
    private Date editDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="reviewed_at")
    private Date reviewDate;


    public Integer getSite() {
	return id.getSite();
    }


    public String getProtocol() {
	return id.getProtocol();
    }

    
    public String getBlock() {
	return id.getBlock();
    }


    public String getEvent() {
	return id.getEvent();
    }


    public Date getEditDate() {
	return editDate;
    }


    public Date getReviewDate() {
	return reviewDate;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("edit at").append("='").append(editDate).append("' ");
        builder.append("review at").append("='").append(reviewDate).append("' ");
        builder.append("]");
        return builder.toString();
    }



}