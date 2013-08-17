package nl.flusso.bigdata.redisexample;

import org.codehaus.jackson.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Path("/tasks")
@Controller
public class DataService {

	protected static final String DUMMY_USER = "dummy_user";

	@Inject
	private RedisOperations<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private ZSetOperations<String, JsonNode> setOps;

	@GET
	@Produces("application/json")
	public NodeList get() {
		Set<ZSetOperations.TypedTuple<JsonNode>> typedTuples = setOps.rangeWithScores(DUMMY_USER, 0, -1);

		Set<NodeItem> nodeItems = new LinkedHashSet<>();
		for (ZSetOperations.TypedTuple<JsonNode> tuple : typedTuples)
			nodeItems.add(new NodeItem(tuple.getScore().longValue(), tuple.getValue()));

		return new NodeList(nodeItems);
	}

	@PUT
	@Path("{createdAt}")
	@Consumes("application/json")
	public void update(@PathParam("createdAt") long createdAt, JsonNode node) {
		setOps.add(DUMMY_USER, node, createdAt);
	}

	@POST
	@Consumes("application/json")
	public Long create(JsonNode node) {
		long timestamp = new DateTime(DateTimeZone.UTC).getMillis();
		setOps.add(DUMMY_USER, node, timestamp);
		return timestamp;
	}

	public void setSetOps(ZSetOperations<String, JsonNode> setOps) {
		this.setOps = setOps;
	}
}
