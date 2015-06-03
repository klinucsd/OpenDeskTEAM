
package org.team.sdsc.datamodel;

import java.util.*;

public  class SamplingUnitNameComparator implements Comparator {
    
    public int compare(Object o1, Object o2)  {
	if (o1 instanceof SamplingUnit && o2 instanceof SamplingUnit) {

	    SamplingUnit p1 = (SamplingUnit)o1;
	    SamplingUnit p2 = (SamplingUnit)o2;

	    StringTokenizer st1 = new StringTokenizer(p1.getName(), "-");
	    st1.nextToken();
	    String abbrev1 = st1.nextToken();
	    int array1 = Integer.parseInt(st1.nextToken());
	    //int number1 = Integer.parseInt(st1.nextToken());
	    Double number1 = Double.parseDouble(st1.nextToken());

	    StringTokenizer st2 = new StringTokenizer(p2.getName(), "-");
	    st2.nextToken();
	    String abbrev2 = st2.nextToken();
	    int array2 = Integer.parseInt(st2.nextToken());
	    //int number2 = Integer.parseInt(st2.nextToken());
	    Double number2 = Double.parseDouble(st2.nextToken());

	    if (array1 < array2) {
		return -1;
	    } else if (array1 == array2) {
		if (number1 < number2) {
		    return -1;
		} else if (number1 == number2) {
		    return 0;
		} else {
		    return 1;
		}
	    } else if (array1 > array2) { 
		return 1;
	    }
	}
	return 0;
    }

}

    
    
