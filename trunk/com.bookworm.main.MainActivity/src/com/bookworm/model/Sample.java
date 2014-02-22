package com.bookworm.model;



public class Sample  {
	
	private Long sampleId;
	private String sampleName;
	private String sampleSurname;
	
	public Sample(){
		
	}
	
	public Sample(Long sampleId,String sampleName){
		this.sampleId=sampleId;
		this.sampleName=sampleName;
	}
	
	public Sample(String sampleName,String sampleSurname){
		this.sampleName=sampleName;
		this.sampleSurname=sampleSurname;
	}



public String getSampleSurname() {
	return sampleSurname;
}

public void setSampleSurname(String sampleSurname) {
	this.sampleSurname = sampleSurname;
}


public Long getSampleId() {
        return sampleId;
}
public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
}

public String getSampleName() {
        return sampleName;
}
public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
}

}  