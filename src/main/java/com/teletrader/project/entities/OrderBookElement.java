package com.teletrader.project.entities;

//recieving sql resul using concept of interface based projections
//Represent one element of OrderBook and contains summed amount of all orders for a particular price
public interface OrderBookElement {
	public double getAmount();
	public double getPrice();
}
