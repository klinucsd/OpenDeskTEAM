
package org.team.sdsc.datamodel;

import java.util.*;

public  class StringComparator implements Comparator {
    
    public int compare(Object o1, Object o2)  {
	if (o1 instanceof String && o2 instanceof String) {

	    String p1 = (String)o1;
	    String p2 = (String)o2;

	    try {
		StringTokenizer st1 = new StringTokenizer(p1, "-");
		st1.nextToken();
		String abbrev1 = st1.nextToken();
		int array1 = Integer.parseInt(st1.nextToken());
		//int number1 = Integer.parseInt(st1.nextToken());
		double number1 = Double.parseDouble(st1.nextToken());

		StringTokenizer st2 = new StringTokenizer(p2, "-");
		st2.nextToken();
		String abbrev2 = st2.nextToken();
		int array2 = Integer.parseInt(st2.nextToken());
		//int number2 = Integer.parseInt(st2.nextToken());
		double number2 = Double.parseDouble(st2.nextToken());

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
	    } catch (Exception ex) {
		return 0;
	    }
	}
	return 0;
    }

}

    
    
