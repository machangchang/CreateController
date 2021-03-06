package MasterJunit;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;

import static org.junit.Assert.*;

public class TestDefaultController {
	
	private DefaultController controller;
	private Request request;
	private RequestHandler handler;
	
	@Before
	public void instaniate() throws Exception {
		controller = new DefaultController();
		request = new SampleRequest();
		handler = new SampleHandler();
		
		controller.addHandler(request, handler);
	}
	
	@Test
	public void testMethod() {
		throw new RuntimeException("implement me");
	}
	
	private class SampleRequest implements Request {
		private static final String DEFAULT_NAME = "Test";
		private String name;
		
		public SampleRequest(String name) {
			this.name = name;
		}
		public SampleRequest() {
			this(DEFAULT_NAME);
		}
		
		public String getName() {
			return this.name; 
		}
	}
	
	private class SampleHandler implements RequestHandler {
		public Response process(Request request) throws Exception {
			return new SampleResponse();
		}
	}
	
	private class SampleResponse implements Response {
		private static final String NAME = "Test";
		public String getName(){
			return NAME;
		}
		public boolean equals(Object object){
			boolean result = false;
			if (object instanceof SampleResponse) {
				result = ((SampleResponse)object).getName().equals(getName());
			}
			return result;
		}
		public int hashCode() {
			return NAME.hashCode();
		}
	}
	
	@Test
	public void testAddHandler() {
		
		RequestHandler handler2 = controller.getHandler(request);
		assertSame("Should be the same", handler2, handler);
	}
	
	@Test
	public void testProcessRequest() {
		
		Response response = controller.processRequest(request);
		assertNotNull("Must not return a null", response);
		assertEquals("Response should be of type SampleResopnse", 
				SampleResponse.class,response.getClass());
	}
	
	private class SampleExceptionHandler implements RequestHandler {
		
		public Response process (Request request) throws Exception {
			throw new Exception("error processing request");
		}
	}
	
	@Test
	public void testProcessRequestAnswerErrorResponse() {
		SampleRequest request = new SampleRequest("testError");
		SampleExceptionHandler handler = new SampleExceptionHandler();
		controller.addHandler(request, handler);
		Response response = controller.processRequest(request);
		
		assertNotNull("Must not null", response);
		assertEquals(ErrorResponse.class, response.getClass());
	}
	
	@Test(expected=RuntimeException.class)
	public void testGethanderNotDefined() {
		SampleRequest request = new SampleRequest("testNotDefined");
		controller.getHandler(request);
	}
	
	@Test(expected=RuntimeException.class)
	public void testAddRequestDuplicateName() {
		SampleRequest request = new SampleRequest();
		SampleHandler handler = new SampleHandler();
		
		controller.addHandler(request, handler);
		
	}
	
	@Test(timeout=13)
	@Ignore(value="Ignore...")
	public void testProcessMultipleRequestTimeout() {
		Request request;
		Response response = new SampleResponse();
		RequestHandler handler = new SampleHandler();
		
		for (int i = 0; i < 9999; i++) {
			request = new SampleRequest(String.valueOf(i));
			controller.addHandler(request,handler);
			response = controller.processRequest(request);
			assertNotNull(response);
			assertNotSame(ErrorResponse.class, response.getClass());
			
		}
	}
}
