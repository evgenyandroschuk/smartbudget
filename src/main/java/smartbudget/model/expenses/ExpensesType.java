package smartbudget.model.expenses;

import com.google.common.base.Objects;

public class ExpensesType {

    private final int id;
    private final int userId;
    private final int expensesTypeId;
    private final String description;
    private final boolean isIncome;

    public ExpensesType(int id, int userId, int expensesTypeId, String description, boolean isIncome) {
        this.id = id;
        this.userId = userId;
        this.expensesTypeId = expensesTypeId;
        this.description = description;
        this.isIncome = isIncome;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getExpensesTypeId() {
        return expensesTypeId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isIncome() {
        return isIncome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpensesType that = (ExpensesType) o;
        return id == that.id &&
            userId == that.userId &&
            expensesTypeId == that.expensesTypeId &&
            isIncome == that.isIncome &&
            Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, expensesTypeId, description, isIncome);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpensesType{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", expensesTypeId=").append(expensesTypeId);
        sb.append(", description='").append(description).append('\'');
        sb.append(", isIncome=").append(isIncome);
        sb.append('}');
        return sb.toString();
    }
}
