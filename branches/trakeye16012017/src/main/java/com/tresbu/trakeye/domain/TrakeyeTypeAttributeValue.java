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
@Table(name = "trakeye_type_attribute_value")
public class TrakeyeTypeAttributeValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User user;

	@ManyToOne
	private TrakeyeTypeAttribute trakeyeTypeAttribute;

	@Column(name = "attribute_value")
	private String attributeValue;
	public TrakeyeTypeAttributeValue(){
		
	}
	public TrakeyeTypeAttributeValue(Long id, User user, TrakeyeTypeAttribute tarkeyeTypeAttribute,
			String attributeValue) {
		super();
		this.id = id;
		this.user = user;
		this.trakeyeTypeAttribute = tarkeyeTypeAttribute;
		this.attributeValue = attributeValue;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TrakeyeTypeAttribute getTrakeyeTypeAttribute() {
		return trakeyeTypeAttribute;
	}
	public void setTrakeyeTypeAttribute(TrakeyeTypeAttribute trakeyeTypeAttribute) {
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
	}
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TrakeyeTypeAttributeValue [userId=" + user + ", trakeyeTypeAttributeId=" + trakeyeTypeAttribute
				+ ", attributeValue=" + attributeValue + "]";
	}

}
