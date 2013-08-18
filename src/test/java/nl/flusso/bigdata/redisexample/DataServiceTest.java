package nl.flusso.bigdata.redisexample;

import com.jayway.jsonpath.JsonPath;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.TextNode;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataServiceTest {

	@Mock
	private ZSetOperations<String, JsonNode> redisMock;

	Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();

	MockHttpResponse response;

	@Before
	public void before(){
		DataService dataService = new DataService();
		dataService.setSetOps(redisMock);
		dispatcher.getRegistry().addSingletonResource(dataService);
		response = new MockHttpResponse();
	}

	@Test
	public void testGet() throws Exception {
		MockHttpRequest request = MockHttpRequest.get("tasks/");

		// create data
		Set<ZSetOperations.TypedTuple<JsonNode>> tuples = new LinkedHashSet<>();
		JsonNode node1 = new TextNode("abc");
		JsonNode node2 = new TextNode("dfg");
		ZSetOperations.TypedTuple<JsonNode> tuple1 = new DefaultTypedTuple<>(node1, 1d);
		ZSetOperations.TypedTuple<JsonNode> tuple2 = new DefaultTypedTuple<>(node2, 2d);
		tuples.add(tuple1);
		tuples.add(tuple2);

		when(redisMock.rangeWithScores(DataService.DUMMY_USER, 0, -1)).thenReturn(tuples);

		dispatcher.invoke(request, response);

		String json = response.getContentAsString();
		assertJson(json, "$.tasks[*].id", 1, 2);
		assertJson(json, "$.tasks[*].node", "abc", "dfg");
	}

	@Test
	public void testUpdate() throws Exception {
		MockHttpRequest request = MockHttpRequest.put("tasks/123456789");

		request.content("{ \"test\" : \"bac\" }".getBytes());
		request.contentType(MediaType.APPLICATION_JSON_TYPE);

		dispatcher.invoke(request, response);

		verify(redisMock).add(eq(DataService.DUMMY_USER), any(JsonNode.class), eq(123456789d));

		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCreate() throws Exception {
		MockHttpRequest request = MockHttpRequest.post("tasks/");

		request.content("{ \"test\" : \"bac\" }".getBytes());
		request.contentType(MediaType.APPLICATION_JSON_TYPE);

		dispatcher.invoke(request, response);

		verify(redisMock).add(eq(DataService.DUMMY_USER), any(JsonNode.class), anyDouble());

		assertEquals(200, response.getStatus());
		assertNotNull(response.getContentAsString());
	}

	private static void assertJson(String json, String path, Object... expected) {
		Object actual = JsonPath.read(json, path);
		if (actual instanceof List)
			assertEquals(json, Arrays.asList(expected), actual);
		else
			assertEquals(json, expected[0], actual);
	}
}
