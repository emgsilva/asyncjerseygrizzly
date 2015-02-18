package asyncjersey.sigmabees.com.AsyncJerseyGrizzly;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ManagedAsync;

@Path("/composite")
public class ResourceCompletableFuture {

	@GET
	@ManagedAsync
	public void asyncGet(@Suspended final AsyncResponse asyncResponse) {
		asyncResponse.setTimeout(1000, TimeUnit.MILLISECONDS);
		asyncResponse.setTimeoutHandler(ar -> ar.resume(Response
				.status(Response.Status.SERVICE_UNAVAILABLE)
				.entity("Operation timed out").build()));

		ServiceTest service = new ServiceTest();

		CompletableFuture.runAsync(() -> service.veryExpensiveOperaiton())
				.thenApply((result) -> asyncResponse.resume(result));
	}
}
