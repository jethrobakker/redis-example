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
		private Task task;

		TaskItem() {
		}

		TaskItem(Long id, Task task) {
			this.id = id;
			this.task = task;
		}

		Long getId() {
			return id;
		}

		Task getTask() {
			return task;
		}
	}
}
