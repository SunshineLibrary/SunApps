package com.sunshine.sunresourcecenter.griditem;

public class ResourceGridItem   
{   
	private String ID;
    private String title;   
    private String author;
    private String tags;
    private int progress;
    private int imageId;   
    private int resCount;
	private String description;  
      
    public ResourceGridItem()   
    {   
        super();   
    }   
    
    public ResourceGridItem(String ID, String title, String author, String tags, int imageId, int progress, String description, int resCount)   
    {   
        super();   
        this.ID = ID;
        this.title = title;   
        this.imageId = imageId;   
        this.author = author;
        this.tags = tags;
        this.description = description;  
        this.progress = progress;
        this.resCount = resCount;
    }   
    
    public int getResCount() {
		return resCount;
	}

	public void setResCount(int resCount) {
		this.resCount = resCount;
	}
	
    public String getDescription( )  
    {  
        return description;  
    }  

    public String getTitle()   
    {   
        return title;   
    }   
   
    public int getImageId()   
    {   
        return imageId;   
    }  
    
    public String getAuthor(){
    	return author;
    }
    
    public String getTags(){
    	return tags;
    }
    
    public int getProgress(){
    	if(progress>100) return 100;
    	else if(progress<0) return 0;
    	else return progress;
    }
    
    public String toString(){
		return ID;
	}
}  
