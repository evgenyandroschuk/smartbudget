package smartbudget.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@XmlRootElement(name = "ExpensesType")
public class ExpensesTypeRequest implements Serializable {

    private String desc;

    private String descRus;

    private boolean isIncome;

    private boolean isActive;

    @XmlElement(name = "description", required = true)
    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlElement(name = "descriptionRus", required = true)
    public String getDescRus() {
        return descRus;
    }

    public void setDescRus(String descRus) {
        this.descRus = descRus;
    }

    @XmlElement(name = "isIncome")
    public boolean isIncome() {
        return isIncome;
    }

    public void setIsIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    @XmlElement(name = "isActive")
    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
