package smartbudget.client.service.impl;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import smartbudget.service.impl.mysql.CommonMySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommonMySQLImplTest {

    @Mock
    private Connection connection;

    @InjectMocks
    private CommonMySQLImpl impl;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "currencyCostProvider")
    public void updateCurrencyCostTest(
            String query,
            int currency,
            double amount
    ) throws SQLException {

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Mockito.doNothing().when(statement).setDouble(1, amount);
        Mockito.doNothing().when(statement).setInt(2, currency);
        when(statement.execute()).thenReturn(true);

        when(connection.prepareStatement(query)).thenReturn(statement);

        impl.updateCurrencyCost(currency, amount);
        verify(connection).prepareStatement(query);
        verify(statement).setDouble(1, amount);
        verify(statement).setInt(2, currency);
        verify(statement).execute();
    }

    @DataProvider
    private static Object[][] currencyCostProvider() {
        return new Object[][] {
                {
                    "update currency set price = ? where id = ?",
                        1,
                        63.10
                }
        };
    }
}
