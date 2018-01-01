package smartbudget.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@XmlRootElement(name = "Response")
public class ExpensesTypeResponse implements Serializable {

    private ExpensesTypeView request;

    private String status;

    @XmlElement(name = "expensesType",required = true)
    public ExpensesTypeView getRequest() {
        return request;
    }

    public void setRequest(ExpensesTypeView request) {
        this.request = request;
    }

    @XmlElement(name = "status", required = true)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
