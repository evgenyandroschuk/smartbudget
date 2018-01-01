package smartbudget.view;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@XmlRootElement(name = "ExpensesType")
public class ExpensesTypeView implements Serializable {

    private Integer id;

    private String description;

    private String descriptionRus;

    @XmlElement(name = "isIncome")
    private Boolean isIncome;

    @XmlElement(name = "isActive")
    private Boolean isActive;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement(name = "description", required = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @XmlElement(name = "descriptionRus", required = true)
    public String getDescriptionRus() {
        return descriptionRus;
    }

    public void setDescriptionRus(String descriptionRus) {
        this.descriptionRus = descriptionRus;
    }

    public Boolean isIncome() {
        return isIncome;
    }

    public void setIsIncome(Boolean isIncome) {
        this.isIncome = isIncome;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
