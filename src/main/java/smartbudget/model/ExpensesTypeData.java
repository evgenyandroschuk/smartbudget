package smartbudget.model;

import com.google.common.base.Objects;

public class ExpensesTypeData {

    private Integer id;
    private String desc;
    private String descRus;
    private boolean isIncome;
    private boolean isActive;

    private ExpensesTypeData(Integer id, String desc, String descRus, boolean isIncome, boolean isActive) {
        this.desc = desc;
        this.descRus = descRus;
        this.isIncome = isIncome;
        this.isActive = isActive;
        this.id = id;
    }

    public static ExpensesTypeData of(Integer id, String desc, String descRus, boolean isIncome, boolean isActive) {
        return new ExpensesTypeData(id, desc, descRus, isIncome, isActive);
    }

    public static ExpensesTypeData of(String desc, String descRus, boolean isIncome, boolean isActive) {
        return new ExpensesTypeData(null, desc, descRus, isIncome, isActive);
    }

    public static ExpensesTypeData of( String desc, String descRus, boolean isIncome) {
        return new ExpensesTypeData(null,  desc, descRus, isIncome, true);
    }

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescRus() {
        return descRus;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpensesTypeData data = (ExpensesTypeData) o;
        return isIncome == data.isIncome &&
                isActive == data.isActive &&
                Objects.equal(id, data.id) &&
                Objects.equal(desc, data.desc) &&
                Objects.equal(descRus, data.descRus);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, desc, descRus, isIncome, isActive);
    }

    @Override
    public String toString() {
        return "ExpensesTypeData{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", descRus='" + descRus + '\'' +
                ", isIncome=" + isIncome +
                ", isActive=" + isActive +
                '}';
    }
}
