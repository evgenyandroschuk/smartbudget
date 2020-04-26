package smartbudget.model.funds;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class Currency {

    private int id;
    private String description;
    private LocalDate updateDate;
    private BigDecimal price;

    public Currency(int id, String description, LocalDate updateDate, BigDecimal price) {
        this.id = id;
        this.description = description;
        this.updateDate = updateDate;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Currency currency = (Currency) o;
        return id == currency.id &&
            Objects.equals(description, currency.description) &&
            Objects.equals(updateDate, currency.updateDate) &&
            Objects.equals(price, currency.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, updateDate, price);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Currency.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("description='" + description + "'")
            .add("updateDate=" + updateDate)
            .add("price=" + price)
            .toString();
    }
}
