
package org.team.sdsc.datamodel;

import java.util.*;

public  class ProtocolComparator implements Comparator {
    
    public int compare(Object o1, Object o2)  {
	if (o1 instanceof Protocol && o2 instanceof Protocol) {
	    Protocol p1 = (Protocol)o1;
	    Protocol p2 = (Protocol)o2;
	    return p1.getName().compareTo(p2.getName());
	}
	return 0;
    }

}

    
    
