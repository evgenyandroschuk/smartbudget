package smartbudget.model;

import com.google.common.base.Objects;

import java.time.LocalDate;

public class ExpensesData {

    private final Long id;
    private final int month;
    private final int year;
    private final int type;
    private final String description;
    private final double amount;
    private final LocalDate date;

    private ExpensesData(Long id, int month, int year, int type, String description, double amount, LocalDate date) {
        this.month = month;
        this.year = year;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.id = id;
    }

    public static ExpensesData of (Long id, int month, int year, int type, String description, double amount, LocalDate date) {
        return new ExpensesData(id, month, year, type, description, amount, date);
    }

    public static ExpensesData of(int month, int year, int type, String description, double amount, LocalDate date) {
        return new ExpensesData(null, month, year, type, description, amount, date);
    }

    public static ExpensesData of(int type, String description, double amount) {
        LocalDate now = LocalDate.now();
        return new ExpensesData(null, now.getMonthValue(), now.getYear(), type, description, amount, now);
    }

    public Long getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpensesData that = (ExpensesData) o;
        return month == that.month &&
                year == that.year &&
                type == that.type &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equal(id, that.id) &&
                Objects.equal(description, that.description) &&
                Objects.equal(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, month, year, type, description, amount, date);
    }

    @Override
    public String toString() {
        return "ExpensesData{" +
                "id=" + id +
                ", month=" + month +
                ", year=" + year +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
