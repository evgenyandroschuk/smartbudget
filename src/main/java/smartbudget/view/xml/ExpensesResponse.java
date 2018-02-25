package smartbudget.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 23.12.17.
 */
@XmlRootElement(name = "Expenses")
public class ExpensesResponse implements Serializable {

    private ExpensesRequest request;

    private String status;

    @XmlElement(name = "items", required = true)
    public ExpensesRequest getRequest() {
        return request;
    }

    public void setRequest(ExpensesRequest request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
