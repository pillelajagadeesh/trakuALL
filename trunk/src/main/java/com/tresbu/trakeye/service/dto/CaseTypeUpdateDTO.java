package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.CaseTypeAttribute;

import io.swagger.annotations.ApiModelProperty;

public class CaseTypeUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
    @ApiModelProperty(required = true, value="Mandatory")
    private Long id;
    
    @NotNull
    @ApiModelProperty(required = true, value="Mandatory")
    private String name;

    private String description;

    @NotNull
    @ApiModelProperty(required = true, value="Mandatory")
    private Set<CaseTypeAttribute> caseTypeAttribute ;

    @ApiModelProperty(required = true, notes="Case type id is mandatory and should be long value. ex: 1006")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, notes="Case type name is mandatory and should be string value. ex: Aerial cable issue")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(notes="Case type description is not mandatory and should be string value. ex: Aerial cable issue near bus stop")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(required = true, notes="Case type attributes is mandatory and should be set of case type attributes value. ex: Road extension on cable route")
	public Set<CaseTypeAttribute> getCaseTypeAttribute() {
		return caseTypeAttribute;
	}

	public void setCaseTypeAttribute(Set<CaseTypeAttribute> caseTypeAttribute) {
		this.caseTypeAttribute = caseTypeAttribute;
	}

}
