package com.tresbu.trakeye.service.dto;

public class StrokeDTO {
private String color ;
private int weight ;

public StrokeDTO(String color) {
	this.color=color;
	this.weight=2;
}
public String getColor() {
	return color;
}
public void setColor(String color) {
	this.color = color;
}
public int getWeight() {
	return weight;
}
public void setWeight(int weight) {
	this.weight = weight;
}

}
