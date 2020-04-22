package smartbudget.model.funds;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class FundData {

    private Long id;
    private int userId;
    private Currency currency;
    private BigDecimal amount;
    private BigDecimal price;
    private String description;
    private LocalDate localDate;

    public FundData(
        Long id,
        int userId,
        Currency currency,
        BigDecimal amount,
        BigDecimal price,
        String description,
        LocalDate localDateTime
    ) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.amount = amount;
        this.price = price;
        this.description = description;
        this.localDate = localDateTime;
    }

    public FundData(int userId, Currency currency, BigDecimal amount, BigDecimal price, String description) {
        this.userId = userId;
        this.currency = currency;
        this.amount = amount;
        this.price = price;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FundData fundData = (FundData) o;
        return id == fundData.id &&
            userId == fundData.userId &&
            Objects.equals(currency, fundData.currency) &&
            Objects.equals(amount, fundData.amount) &&
            Objects.equals(price, fundData.price) &&
            Objects.equals(description, fundData.description) &&
            Objects.equals(localDate, fundData.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, currency, amount, price, description, localDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FundData.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("userId=" + userId)
            .add("currency=" + currency)
            .add("amount=" + amount)
            .add("price=" + price)
            .add("description='" + description + "'")
            .add("localDate=" + localDate)
            .toString();
    }
}
