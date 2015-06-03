package org.team.sdsc.datamodel;

import java.util.*;
import javax.xml.bind.annotation.*;

@XmlAccessorType ( XmlAccessType.FIELD )
@XmlRootElement
@XmlSeeAlso(TreeRecord.class)
//@XmlSeeAlso(LianaRecord.class)
public class VegetationData {

    private List treeData;
    private List lianaData;
   
    public VegetationData() {
    }

    public void setTreeData(List treeData) {
	this.treeData = treeData;
    }

    public void setLianaData(List lianaData) {
	this.lianaData = lianaData;
    }


}
