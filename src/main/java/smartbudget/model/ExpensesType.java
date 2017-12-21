package smartbudget.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@Entity
public class ExpensesType implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String desc;

    private String descRus;

    private boolean isIncome;

    private boolean isActive;

    public ExpensesType() {
        super();
    }

    public ExpensesType(String desc, String descRus, boolean isIncome, boolean isActive) {
        this.desc = desc;
        this.descRus = descRus;
        this.isIncome = isIncome;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescRus() {
        return descRus;
    }

    public void setDescRus(String descRus) {
        this.descRus = descRus;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIsIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String toString() {
        return "[ " + this.id + ": " + this.desc + " isIncome = " + this.isIncome + "; isActive = " + isActive + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpensesType that = (ExpensesType) o;

        if (id != that.id) return false;
        if (isIncome != that.isIncome) return false;
        if (isActive != that.isActive) return false;
        if (!desc.equals(that.desc)) return false;
        return descRus.equals(that.descRus);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + desc.hashCode();
        result = 31 * result + descRus.hashCode();
        result = 31 * result + (isIncome ? 1 : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
