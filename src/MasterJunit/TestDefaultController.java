package MasterJunit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDefaultController {
	
	private DefaultController controller;
	
	@Before
	public void instaniate() throws Exception {
		controller = new DefaultController();
	}
	
	@Test
	public void testMethod() {
		throw new RuntimeException("implement me");
	}
	
	private class SampleRequest implements Request {
		public String getName() {
			return "Test";
		}
	}
	
	private class SampleHandler implements RequestHandler {
		public Response process(Request request) throws Exception {
			return new SampleResponse();
		}
	}
	
	private class SampleResponse implements Response {
		//empty
	}
	
	@Test
	public void testAddHandler() {
		
		Request request = new SampleRequest();
		
		RequestHandler handler = new SampleHandler();
		
		controller.addHandler(request, handler);
		
		RequestHandler handler2 = controller.getHandler(request);
		assertSame("Should be the same", handler2, handler);
	}
}































