package net.codingchallenge.tripgenerator.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.codingchallenge.tripgenerator.enums.TripStatus;

public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private ZonedDateTime started;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private ZonedDateTime finished;

	private Long durationSecs;

	private String fromStopId;

	private String toStopId;

	private Double chargeAmount;

	private String companyId;

	private String busId;

	private String primaryAccountNumber;

	private TripStatus status;

	public Trip() {

	}

	public Trip(ZonedDateTime started, ZonedDateTime finished, long durationSecs, String fromStopId, String toStopId, Double chargeAmount,
			String companyId, String busId, String primaryAccountNumber, TripStatus status) {
		super();
		this.started = started;
		this.finished = finished;
		this.durationSecs = durationSecs;
		this.fromStopId = fromStopId;
		this.toStopId = toStopId;
		this.chargeAmount = chargeAmount;
		this.companyId = companyId;
		this.busId = busId;
		this.primaryAccountNumber = primaryAccountNumber;
		this.status = status;
	}

	public ZonedDateTime getStarted() {
		return started;
	}

	public void setStarted(ZonedDateTime started) {
		this.started = started;
	}

	public ZonedDateTime getFinished() {
		return finished;
	}

	public void setFinished(ZonedDateTime finished) {
		this.finished = finished;
	}

	public Long getDurationSecs() {
		return durationSecs;
	}

	public void setDurationSecs(Long durationSecs) {
		this.durationSecs = durationSecs;
	}

	public String getFromStopId() {
		return fromStopId;
	}

	public void setFromStopId(String fromStopId) {
		this.fromStopId = fromStopId;
	}

	public String getToStopId() {
		return toStopId;
	}

	public void setToStopId(String toStopId) {
		this.toStopId = toStopId;
	}

	public Double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
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

	public TripStatus getStatus() {
		return status;
	}

	public void setStatus(TripStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Trip [started=" + started + ", finished=" + finished + ", durationSecs=" + durationSecs
				+ ", fromStopId=" + fromStopId + ", toStopId=" + toStopId + ", chargeAmount=" + chargeAmount
				+ ", companyId=" + companyId + ", busId=" + busId + ", primaryAccountNumber=" + primaryAccountNumber
				+ ", status=" + status + "]";
	}
}
