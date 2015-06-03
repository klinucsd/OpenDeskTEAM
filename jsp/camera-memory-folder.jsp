
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

<%

     Set set = new TreeSet();

     File root = null;
     String osName = System.getProperty("os.name");
     System.out.println("os name = "+osName);

     if (osName.toLowerCase().indexOf("windows") > -1) {

        File[] roots = File.listRoots();
        for (int i=0; i<roots.length; i++) {
            try {
                String[] dirs = roots[i].list();
                for (int j=0; j<dirs.length; j++) {   
                    if (dirs[j].equals("DCIM")) {
                        File dir = new File(roots[i], dirs[j]);
                        set.add(dir.getAbsolutePath());
                    }
                }
            } catch (Exception ex) {}
        }

     } else if (osName.toLowerCase().indexOf("mac") > -1) {

        root = new File("/Volumes");	
	String[] dirs = root.list();
	for (int i=0; i<dirs.length; i++) {
	    File dir = new File(root, dirs[i]);
	    if (dir.isDirectory()) {
		String[] files = dir.list();
		for (int j=0; j<files.length; j++) {
		    if (files[j].equals("DCIM")) {
		        set.add(dir.getAbsolutePath());
			break;
		    }
		}
	    }
	}
    
	System.out.println("set = "+set);

     }


%>

{
   results: [

<%
    int k = 0;
    for (Iterator i=set.iterator(); i.hasNext(); ) {
	if (k > 0) {
	    out.println("      ,");
	}
	out.println("      {  name : '"+i.next()+"' }" ); 
	k++;
    }
%>


  ]
}