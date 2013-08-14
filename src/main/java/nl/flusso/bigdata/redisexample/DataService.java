package nl.flusso.bigdata.redisexample;

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

	public static final String DUMMY_USER = "dummy_user";

	@Inject
	private RedisOperations<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private ZSetOperations<String, Task> setOps;

	@GET
	@Produces("application/json")
	public TaskList get() {
		Set<ZSetOperations.TypedTuple<Task>> typedTuples = setOps.rangeWithScores(DUMMY_USER, 0, -1);

		Set<TaskList.TaskItem> taskItems = new LinkedHashSet<>();
		for (ZSetOperations.TypedTuple<Task> tuple : typedTuples)
			taskItems.add(new TaskList.TaskItem(tuple.getScore().longValue(), tuple.getValue()));

		return new TaskList(taskItems);
	}

	@PUT
	@Path("{createdAt}")
	@Consumes("application/json")
	public void update(@PathParam("createdAt") long createdAt, Task task) {
		setOps.add(DUMMY_USER, task, createdAt);
	}

	@POST
	@Consumes("application/json")
	public Long create(Task task) {
		long timestamp = new DateTime(DateTimeZone.UTC).getMillis();
		setOps.add(DUMMY_USER, task, timestamp);
		return timestamp;
	}

}
