package com.samples;

import javax.jdo.annotations.IdGeneratorStrategy ;
import javax.jdo.annotations.IdentityType ;
import javax.jdo.annotations.PersistenceCapable ;
import javax.jdo.annotations.Persistent ;
import javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable (identityType = IdentityType.APPLICATION)
public class Bus {
	
	@PrimaryKey
	@Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY )
	private Long id;
	private String companyName;
	private Integer seats;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getSeats() {
		return seats;
	}
	public void setSeats(Integer seats) {
		this.seats = seats;
	}

}
