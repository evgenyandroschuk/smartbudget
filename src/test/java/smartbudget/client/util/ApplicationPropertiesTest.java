package smartbudget.client.util;

import static org.mockito.Mockito.*;

import static org.testng.Assert.assertNotNull;

import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.util.ApplicationProperties;


public class ApplicationPropertiesTest {


    @Test
    public void testPropertiesSuccess() {

        ApplicationProperties properties = mock(ApplicationProperties.class);
        when(properties.getProperty("test")).thenReturn("testSuccess");

        Assert.assertEquals(properties.getProperty("test"), "testSuccess");
    }

}
