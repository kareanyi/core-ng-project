package core.framework.impl.web.service;

import core.framework.api.http.HTTPStatus;
import core.framework.api.util.Lists;
import core.framework.api.util.Types;
import core.framework.api.web.Controller;
import core.framework.api.web.Request;
import core.framework.api.web.Response;
import core.framework.api.web.ResponseImpl;
import core.framework.impl.web.response.BeanBody;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author neo
 */
public class ServiceControllerBuilderTest {
    private Request request;
    private TestWebServiceImpl serviceImpl;

    @Before
    public void prepare() {
        serviceImpl = new TestWebServiceImpl();
        request = Mockito.mock(Request.class);
    }

    @Test
    public void get() throws Exception {
        when(request.pathParam("id", Integer.class)).thenReturn(1);

        Controller controller = new ServiceControllerBuilder<>(TestWebService.class,
            serviceImpl,
            TestWebService.class.getDeclaredMethod("get", Integer.class))
            .build();

        Response response = controller.execute(request);
        assertEquals(HTTPStatus.OK, response.status());
        assertEquals(2, (int) ((TestWebService.TestResponse) ((BeanBody) ((ResponseImpl) response).body).bean).intField);
    }

    @Test
    public void create() throws Exception {
        TestWebService.TestRequest requestBean = new TestWebService.TestRequest();
        requestBean.stringField = "value";

        when(request.pathParam("id", Integer.class)).thenReturn(1);
        when(request.bean(TestWebService.TestRequest.class)).thenReturn(requestBean);

        Controller controller = new ServiceControllerBuilder<>(TestWebService.class,
            serviceImpl,
            TestWebService.class.getDeclaredMethod("create", Integer.class, TestWebService.TestRequest.class))
            .build();
        Response response = controller.execute(request);
        assertEquals(HTTPStatus.CREATED, response.status());
    }

    @Test
    public void batch() throws Exception {
        TestWebService.TestRequest requestBean = new TestWebService.TestRequest();
        requestBean.stringField = "value";

        when(request.bean(Types.list(TestWebService.TestRequest.class))).thenReturn(Lists.newArrayList(requestBean));

        Controller controller = new ServiceControllerBuilder<>(TestWebService.class,
            serviceImpl,
            TestWebService.class.getDeclaredMethod("batch", List.class))
            .build();
        Response response = controller.execute(request);
        assertEquals(HTTPStatus.OK, response.status());
    }

    public static class TestWebServiceImpl implements TestWebService {
        @Override
        public TestResponse get(Integer id) {
            assertEquals(1, (int) id);

            TestResponse response = new TestResponse();
            response.intField = 2;
            return response;
        }

        @Override
        public void create(Integer id, TestRequest request) {
            assertEquals(1, (int) id);
            assertEquals("value", request.stringField);
        }

        @Override
        public void delete(String id) {

        }

        @Override
        public List<TestResponse> batch(List<TestRequest> requests) {
            assertEquals(1, requests.size());
            return Lists.newArrayList();
        }
    }
}