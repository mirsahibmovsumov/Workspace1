package com.exchange;

public class Valute {
	
	String _name;
	double _value;
	
	public Valute(){
		
	}
	
	public Valute(String name, double value){
		this._name = name;
		this._value = value;
	}
	
	public String getName(){
		return this._name;
	}
	
	public void setName(String name){
		this._name = name;
	}
	
	public double getValue(){
		return this._value;
	}
	
	public void setValue(double value){
		this._value = value;
	}
	
}
