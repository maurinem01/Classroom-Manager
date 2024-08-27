package model;

public class Appointment {

	private long id, appointmentTypeID, classID, calendarID;
	private long[] addonIDs;
	private String firstName, lastName, phone, email, date, time, endTime, dateCreated,
			datetimeCreated, datetime, price, priceSold, paid, amountPaid, type, category,
			duration, calendar, certificate, confirmationPage, confirmationPagePaymentLink,
			location, notes, timezone, calendarTimezone, labels;
	private boolean canceled, canClientCancel, canClientReschedule;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return id + " " + firstName + " " + lastName + " " + phone + " " + email + " " + date + " " + time + " "
				+ endTime;
	}

	public long getAppointmentTypeID() {
		return appointmentTypeID;
	}

	public void setAppointmentTypeID(long appointmentTypeID) {
		this.appointmentTypeID = appointmentTypeID;
	}

	public long getClassID() {
		return classID;
	}

	public void setClassID(long classID) {
		this.classID = classID;
	}

	public long[] getAddonIDs() {
		return addonIDs;
	}

	public void setAddonIDs(long[] addonIDs) {
		this.addonIDs = addonIDs;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDatetimeCreated() {
		return datetimeCreated;
	}

	public void setDatetimeCreated(String datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceSold() {
		return priceSold;
	}

	public void setPriceSold(String priceSold) {
		this.priceSold = priceSold;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCalendarID() {
		return calendarID;
	}

	public void setCalendarID(long calendarID) {
		this.calendarID = calendarID;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCalendar() {
		return calendar;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getConfirmationPage() {
		return confirmationPage;
	}

	public void setConfirmationPage(String confirmationPage) {
		this.confirmationPage = confirmationPage;
	}

	public String getConfirmationPagePaymentLink() {
		return confirmationPagePaymentLink;
	}

	public void setConfirmationPagePaymentLink(String confirmationPagePaymentLink) {
		this.confirmationPagePaymentLink = confirmationPagePaymentLink;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getCalendarTimezone() {
		return calendarTimezone;
	}

	public void setCalendarTimezone(String calendarTimezone) {
		this.calendarTimezone = calendarTimezone;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public boolean isCanClientCancel() {
		return canClientCancel;
	}

	public void setCanClientCancel(boolean canClientCancel) {
		this.canClientCancel = canClientCancel;
	}

	public boolean isCanClientReschedule() {
		return canClientReschedule;
	}

	public void setCanClientReschedule(boolean canClientReschedule) {
		this.canClientReschedule = canClientReschedule;
	}
}
