
package org.team.sdsc.datamodel;

import java.util.*;

public  class CameraTrapPhotoComparator implements Comparator {
    
    public int compare(Object o1, Object o2)  {
	if (o1 instanceof CameraTrapPhoto && o2 instanceof CameraTrapPhoto) {
	    CameraTrapPhoto p1 = (CameraTrapPhoto)o1;
	    CameraTrapPhoto p2 = (CameraTrapPhoto)o2;
	    return p1.getRawName().compareTo(p2.getRawName());
	}
	return 0;
    }

}

    
    
