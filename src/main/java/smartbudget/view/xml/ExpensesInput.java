package smartbudget.view.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 05.01.2018.
 */
public class ExpensesInput implements Serializable {

    private String description;

    private int type;

    private double amount;

    @XmlElement(name = "desc")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "type", required = true)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @XmlElement(name = "amount", required = true)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
