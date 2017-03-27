package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
public class CaseImageDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5906097267869305827L;
	
    private Long id;
	
	private byte[] image;

	private TrCaseDTO trCase;

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

	public TrCaseDTO getTrCase() {
		return trCase;
	}

	public void setTrCase(TrCaseDTO trCase) {
		this.trCase = trCase;
	}

	
	
	
	

}
