package smartbudget.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@Entity
public class Expenses implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;

    int month;

    int year;

    String description;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "expenses_type_id", nullable=false)
    ExpensesType expensesType;

    double amount;

    LocalDate date;

    public Expenses() {
        super();
    }

    public Expenses(int month, int year, String description, ExpensesType expensesType, double amount, LocalDate date) {
        this.month = month;
        this.year = year;
        this.description = description;
        this.expensesType = expensesType;
        this.amount = amount;
        this.date = date;
    }

    public Expenses(long id, int month, int year, String description, ExpensesType expensesType, double amount, LocalDate date) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.description = description;
        this.expensesType = expensesType;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpensesType getExpensesType() {
        return expensesType;
    }

    public void setExpensesType(ExpensesType expensesType) {
        this.expensesType = expensesType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expenses expenses = (Expenses) o;

        if (id != expenses.id) return false;
        if (month != expenses.month) return false;
        if (year != expenses.year) return false;
        if (Double.compare(expenses.amount, amount) != 0) return false;
        if (!description.equals(expenses.description)) return false;
        if (!expensesType.equals(expenses.expensesType)) return false;
        return date.equals(expenses.date);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + month;
        result = 31 * result + year;
        result = 31 * result + description.hashCode();
        result = 31 * result + expensesType.hashCode();
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + date.hashCode();
        return result;
    }
}
