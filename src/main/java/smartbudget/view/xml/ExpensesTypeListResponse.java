package smartbudget.view.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@XmlRootElement(name = "ExpensesTypes")
public class ExpensesTypeListResponse implements Serializable {


    private List<ExpensesTypeView> expensesTypeList;

    @XmlElement(name = "List")
    public List<ExpensesTypeView> getExpensesTypeList() {
        return expensesTypeList;
    }

    public void setExpensesTypeList(List<ExpensesTypeView> expensesTypeList) {
        this.expensesTypeList = expensesTypeList;
    }
}
