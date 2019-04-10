package smartbudget.model.expenses;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpensesData {

    private Long id;
    private final int userId;
    private final int month;
    private final int year;
    private final int expensesType;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;

    public ExpensesData(
        Long id,
        int userId,
        int month,
        int year,
        int expensesType,
        String description,
        BigDecimal amount,
        LocalDate date
    ) {
        this.id = id;
        this.userId = userId;
        this.month = month;
        this.year = year;
        this.expensesType = expensesType;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public ExpensesData(
        int userId,
        int month,
        int year,
        int expensesType,
        String description,
        BigDecimal amount,
        LocalDate date
    ) {
        this.userId = userId;
        this.month = month;
        this.year = year;
        this.expensesType = expensesType;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getExpensesType() {
        return expensesType;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpensesData that = (ExpensesData) o;
        return userId == that.userId &&
            month == that.month &&
            year == that.year &&
            expensesType == that.expensesType &&
            Objects.equal(id, that.id) &&
            Objects.equal(description, that.description) &&
            Objects.equal(amount, that.amount) &&
            Objects.equal(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, month, year, expensesType, description, amount, date);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpensesData{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", month=").append(month);
        sb.append(", year=").append(year);
        sb.append(", expensesType=").append(expensesType);
        sb.append(", description='").append(description).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}
