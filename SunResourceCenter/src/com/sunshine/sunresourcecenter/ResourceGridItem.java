package com.sunshine.sunresourcecenter;

public class ResourceGridItem   
{   
    private String title;   
    private String author;
    private String tags;
    private int progress;
    private int imageId;   
    private int state;
    private String description;  
      
    public ResourceGridItem()   
    {   
        super();   
    }   
    
    public ResourceGridItem(String title, String author, String tags, int imageId, int progress, String description)   
    {   
        super();   
        this.title = title;   
        this.imageId = imageId;   
        this.author = author;
        this.tags = tags;
        this.description = description;  
        this.progress = progress;
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
}  
