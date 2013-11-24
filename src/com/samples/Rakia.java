package com.samples;

import javax.jdo.annotations.IdGeneratorStrategy ;
import javax.jdo.annotations.IdentityType ;
import javax.jdo.annotations.PersistenceCapable ;
import javax.jdo.annotations.Persistent ;
import javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable (identityType = IdentityType.APPLICATION)
public class Rakia {
	
	@PrimaryKey
	@Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY )
	private Long id;
	private String name;
    private String location;
    private Double degrees;
    private String description;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Double getDegrees() {
		return degrees;
	}
	public void setDegrees(Double degrees) {
		this.degrees = degrees;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
