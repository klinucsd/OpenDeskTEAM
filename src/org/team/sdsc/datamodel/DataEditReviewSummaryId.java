
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.io.*;
import org.apache.commons.lang.builder.*;


/**
 * Represents an edit review summary id.
 *
 * @author Kai Lin
 */
@Embeddable
public class DataEditReviewSummaryId  implements Serializable  {

    private Integer site;
    private String protocol;
    private String block;
    private String event;

    public DataEditReviewSummaryId() {
    }


    public Integer getSite() {
	return site;
    }


    public String getProtocol() {
	return protocol;
    }

    
    public String getBlock() {
	return block;
    }


    public String getEvent() {
	return event;
    }

    
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(site);
        builder.append(protocol);
        builder.append(block);
        builder.append(event);
        return builder.toHashCode();
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(site+"  "+protocol+"   "+block+"   "+event);
	return builder.toString();
    }


}