package com.tresbu.trakeye.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="trakeye_servicetype_attribute_value")
public class ServiceTypeAttributeValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3913598919701382714L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ServiceTypeAttribute getServiceTypeAttribute() {
		return serviceTypeAttribute;
	}

	public void setServiceTypeAttribute(ServiceTypeAttribute serviceTypeAttribute) {
		this.serviceTypeAttribute = serviceTypeAttribute;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
/**
 * Created User id
 */
	@ManyToOne
	private User user;
	
	@ManyToOne()
	private ServiceTypeAttribute serviceTypeAttribute;
	
	@Column(name = "attribute_value")
	private String attributeValue;

	public  ServiceTypeAttributeValue() {

	}
	
	public ServiceTypeAttributeValue(Long id, User user, ServiceTypeAttribute serviceTypeAttribute,
			String attributeValue) {
		super();
		this.id = id;
		this.user = user;
		this.serviceTypeAttribute = serviceTypeAttribute;
		this.attributeValue = attributeValue;
	}
	
	

}
