package nl.flusso.bigdata.redisexample;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement
public class TaskList {

	@XmlElement
	private Set<TaskItem> tasks;

	public TaskList() {
	}

	public TaskList(Set<TaskItem> tasks) {
		this.tasks = tasks;
	}

	public Set<TaskItem> getTasks() {
		return tasks;
	}

	@XmlRootElement
	static class TaskItem {
		@XmlElement
		private Long id;
		@XmlElement
		private String json;

		TaskItem() {
		}

		TaskItem(Long id, String json) {
			this.id = id;
			this.json = json;
		}

		Long getId() {
			return id;
		}

		String getJson() {
			return json;
		}
	}
}
