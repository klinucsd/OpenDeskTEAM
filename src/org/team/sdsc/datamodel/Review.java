
package org.team.sdsc.datamodel;

import org.team.sdsc.datamodel.annotation.*;


public class Review {

    private String reviewerName;
    private String reviewComment;
    private String reviewResult;

    public Review(String reviewerName, String reviewComment, String reviewResult) {
	this.reviewerName = reviewerName;
	this.reviewComment = reviewComment;
	this.reviewResult = reviewResult;
    }


    @Display(title="Review Result", var="ReviewResult", index=1, width=50)
    public String getResult() {
	return this.reviewResult;
    }

    @Display(title="Review Comment", var="ReviewComment", index=2, width=50)
    public String getComment() {
	return this.reviewComment;
    }


}