package net.codingchallenge.tripgenerator.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.codingchallenge.tripgenerator.enums.TapType;

public class Tap implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="UTC")
	private ZonedDateTime datetimeUTC;
	
	private TapType tapType;
	
	private String stopId;
	
	private String companyId;
	
	private String busId;
	
	private String primaryAccountNumber;
	
	public Tap() {
		
	}

	public Tap(int id, ZonedDateTime datetimeUTC, TapType tapType, String stopId, String companyId, String busId,
			String primaryAccountNumber) {
		super();
		this.id = id;
		this.datetimeUTC = datetimeUTC;
		this.tapType = tapType;
		this.stopId = stopId;
		this.companyId = companyId;
		this.busId = busId;
		this.primaryAccountNumber = primaryAccountNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ZonedDateTime getDatetimeUTC() {
		return datetimeUTC;
	}

	public void setDatetimeUTC(ZonedDateTime datetimeUTC) {
		this.datetimeUTC = datetimeUTC;
	}

	public TapType getTapType() {
		return tapType;
	}

	public void setTapType(TapType tapType) {
		this.tapType = tapType;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getPrimaryAccountNumber() {
		return primaryAccountNumber;
	}

	public void setPrimaryAccountNumber(String primaryAccountNumber) {
		this.primaryAccountNumber = primaryAccountNumber;
	}

	@Override
	public String toString() {
		return "Tap [id=" + id + ", datetimeUTC=" + datetimeUTC + ", tapType=" + tapType + ", stopId=" + stopId
				+ ", companyId=" + companyId + ", busId=" + busId + ", primaryAccountNumber=" + primaryAccountNumber
				+ "]";
	}
}
