package com.tresbu.trakeye.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
@Entity
@Table(name="trakeye_trcase_images")
public class CaseImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5906097267869305827L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Type(type="org.hibernate.type.BinaryType") 
	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;

	@ManyToOne
	private TrCase trCase;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public TrCase getTrCase() {
		return trCase;
	}

	public void setTrCase(TrCase trCase) {
		this.trCase = trCase;
	}

	
	
	
	

}
