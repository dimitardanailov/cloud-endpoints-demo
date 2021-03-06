package com.samples;

import javax.jdo.annotations.IdGeneratorStrategy ;
import javax.jdo.annotations.IdentityType ;
import javax.jdo.annotations.PersistenceCapable ;
import javax.jdo.annotations.Persistent ;
import javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable (identityType = IdentityType.APPLICATION)
public class Car {

	@PrimaryKey
	@Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY )
	private Long id;
	private String name;
	
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
}
