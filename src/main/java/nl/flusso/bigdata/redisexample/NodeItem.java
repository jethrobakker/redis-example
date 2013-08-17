package nl.flusso.bigdata.redisexample;

import org.codehaus.jackson.JsonNode;

public class NodeItem {

	private Long id;

	private JsonNode node;

	public NodeItem(Long id, JsonNode node) {
		this.id = id;
		this.node = node;
	}

	public Long getId() {
		return id;
	}

	public JsonNode getNode() {
		return node;
	}
}
