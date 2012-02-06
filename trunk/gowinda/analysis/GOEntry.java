package gowinda.analysis;

public class GOEntry implements Comparable<GOEntry> {
	private final String goid;
	private final String description;

	
	public GOEntry(String goid, String description)
	{	
		this.goid=goid;
		this.description=description;

	}
	public String goID()
	{
		return this.goid;
	}
	public String description()
	{
		return this.description;
	}
	
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof GOEntry)){return false;}
        GOEntry tc=(GOEntry)o; 
        if(tc.goid.equals(this.goid)) return true;
        return false;
    }
    
    @Override
    public int hashCode()
    {
    	return goid.hashCode();
    }
    
    
    @Override
    public int compareTo(GOEntry tc)
    {
    	// Sort by the goid string
    	return this.goid.compareTo(tc.goid);
    }
	
	
	//GO:0000001      mitochondrion inheritance       FBGN0261618
	//GO:0000002      mitochondrial genome maintenance        FBGN0004407 FBGN0010438 FBGN0032154 FBGN0039930

}
