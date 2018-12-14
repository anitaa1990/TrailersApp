package com.an.trailers.api;

import com.an.trailers.data.Resource;
import com.an.trailers.data.Status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ApiResponseTest {

    @Test
    public void exception() {
        Exception exception = new Exception("test");
        Resource apiResponse = Resource.error(exception.getMessage(), exception);
        Assert.assertEquals("test", apiResponse.message);
        Assert.assertEquals(Status.ERROR, apiResponse.status);
    }

    @Test
    public void success() {
        Resource resource = Resource.success("test");
        Assert.assertEquals("test", resource.data);
        Assert.assertEquals(Status.SUCCESS, resource.status);
    }
}
