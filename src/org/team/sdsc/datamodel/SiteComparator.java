
package org.team.sdsc.datamodel;

import java.util.*;

public  class SiteComparator implements Comparator {
    
    public int compare(Object o1, Object o2)  {
	if (o1 instanceof Site && o2 instanceof Site) {
	    Site p1 = (Site)o1;
	    Site p2 = (Site)o2;
	    return p1.getName().compareTo(p2.getName());
	}
	return 0;
    }

}

    
    
