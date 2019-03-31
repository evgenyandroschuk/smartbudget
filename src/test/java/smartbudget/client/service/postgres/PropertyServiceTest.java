package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import smartbudget.model.services.VersionedProperty;
import smartbudget.service.postres.property.PropertyServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropertyServiceTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetProperties() {
        String query = "select id, user_id, property_id, description from t_property";
        List<VersionedProperty> properties = ImmutableList.of(
                new VersionedProperty(1, 1,1, "Property 1"),
                new VersionedProperty(2, 1,1, "Property 2")
        );

        when(namedParameterJdbcTemplate.query(
                eq(query), (ResultSetExtractor<List<VersionedProperty>>) any(ResultSetExtractor.class))
        ).thenReturn(properties);

        List<VersionedProperty> result = propertyService.getProperties();
        Assert.assertEquals(result.get(1).getDescription(), "Property 2");

        verify(namedParameterJdbcTemplate)
                .query(eq(query), (ResultSetExtractor<List<VersionedProperty>>) any(ResultSetExtractor.class));
    }

}
