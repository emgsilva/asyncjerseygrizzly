package asyncjersey.AsyncJerseyGrizzly;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ManagedAsync;

@Path("/")
public class Resource {

	@GET
	@ManagedAsync
	public void asyncGet(@Suspended final AsyncResponse asyncResponse) {
		asyncResponse.setTimeout(1000, TimeUnit.MILLISECONDS);
		asyncResponse.setTimeoutHandler(ar -> ar.resume(Response
				.status(Response.Status.SERVICE_UNAVAILABLE)
				.entity("Operation timed out").build()));

		ServiceTest service = new ServiceTest();

		String result = service.veryExpensiveOperaiton();
		asyncResponse.resume(result);
	}
}

// expensive operation simulation
class ServiceTest {

	// with timeout faking that the operation takes long time to process
	// - this will test the "async response timeout" handler
	public String veryExpensiveOperaiton() {

		try {
			Thread.sleep(2000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Test passed");
		
		return "Hello";
	}

}