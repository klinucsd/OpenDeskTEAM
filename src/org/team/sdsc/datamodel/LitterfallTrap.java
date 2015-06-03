package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM person.
 *
 * @author Kai Lin
 */
@Entity
@DiscriminatorValue("Litterfall Trap")
public class LitterfallTrap extends SamplingUnit {

           
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public LitterfallTrap() {
    	super();
    }


    /**
     * Get the name by which this sampling unit is known.
     *
     * @return the name of the sampling unit.
     */
    @Display(title="Sampling Unit Name", var="SamplingUnitName", index=1, width=100)  
    public String getName() {
        return super.getName();
    }

    
}