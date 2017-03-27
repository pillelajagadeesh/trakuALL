package com.tresbu.trakeye.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Entity
@Table(name = "trakeye_servicetype_attribute")
public class ServiceTypeAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5143641643353630218L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	
	@NotNull
	@Size(min = 0, max = 50)
	@Column(name = "name", nullable = false)
	private String name;
	
	public ServiceTypeAttribute(){
		
	}
	public ServiceTypeAttribute(String name){
		this.name = name;
	}

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
