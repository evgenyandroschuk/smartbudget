package smartbudget.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 23.12.17.
 */
@XmlRootElement(name = "Expenses")
public class ExpensesRequest implements Serializable {

    private int month;

    private int year;

    private String desc;

    private int type;

    private double amount;

    private String updateDate;

    @XmlElement(name = "month", required = true)
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @XmlElement(name = "year", required = true)
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @XmlElement(name = "updateDate", required = true)
    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
