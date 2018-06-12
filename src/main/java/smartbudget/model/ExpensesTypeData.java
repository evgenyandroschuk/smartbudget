package smartbudget.model;

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

}
