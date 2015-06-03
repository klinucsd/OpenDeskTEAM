
package org.team.sdsc.datamodel;

import java.util.*;

public  class BlockComparator implements Comparator {
    
    public int compare(Object o1, Object o2)  {
	if (o1 instanceof Block && o2 instanceof Block) {
	    Block p1 = (Block)o1;
	    Block p2 = (Block)o2;
	    return p1.getName().compareTo(p2.getName());
	}
	return 0;
    }

}

    
    
