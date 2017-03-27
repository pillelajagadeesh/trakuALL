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
@Table(name="trakeye_casetype_attribute_value")
public class CaseTypeAttributeValue implements Serializable {

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

	public CaseTypeAttribute getCaseTypeAttribute() {
		return caseTypeAttribute;
	}

	public void setCaseTypeAttribute(CaseTypeAttribute caseTypeAttribute) {
		this.caseTypeAttribute = caseTypeAttribute;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@ManyToOne
	private User user;
	
	@ManyToOne
	private CaseTypeAttribute caseTypeAttribute;
	
	@Column(name = "attribute_value")
	private String attributeValue;
	public CaseTypeAttributeValue(){
		
	}
	public CaseTypeAttributeValue(Long id, User user, CaseTypeAttribute caseTypeAttribute,
			String attributeValue) {
		super();
		this.id = id;
		this.user = user;
		this.caseTypeAttribute = caseTypeAttribute;
		this.attributeValue = attributeValue;
	}
	
	

}
