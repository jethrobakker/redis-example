package nl.flusso.bigdata.redisexample;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Task {

	private String description;

	private DateTime startDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
}
