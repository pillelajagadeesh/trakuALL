package com.tresbu.trakeye.service.dto;

import java.util.List;

public class UserPathDTO {
   private Long id;
   
  

private String userName;
   
   public String getUserName() {
	return userName;
}


public void setUserName(String userName) {
	this.userName = userName;
}

private StrokeDTO stroke;
   
   
   
   public StrokeDTO getStroke() {
	return stroke;
}


public void setStroke(StrokeDTO stroke) {
	this.stroke = stroke;
}


public UserPathDTO(Long userid, String loginUser,List<PathDTO> path,StrokeDTO stroke) {
	this.id = userid;
	this.userName = loginUser;
	this.path = path;
	this.stroke = stroke;
}
   
   
   public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public List<PathDTO> getPath() {
	return path;
}

public void setPath(List<PathDTO> path) {
	this.path = path;
}

private List<PathDTO> path;

}
