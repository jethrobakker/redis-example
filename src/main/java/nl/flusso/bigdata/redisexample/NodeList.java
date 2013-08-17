package nl.flusso.bigdata.redisexample;

import java.util.Set;

public class NodeList {

	private Set<NodeItem> tasks;

	public NodeList(Set<NodeItem> tasks) {
		this.tasks = tasks;
	}

	public Set<NodeItem> getTasks() {
		return tasks;
	}
}
