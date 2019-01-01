package smartbudget.model.services;

import com.google.common.base.Objects;

public abstract class AbstractPropertyType {

    private int id;
    private String description;

    public AbstractPropertyType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPropertyType that = (AbstractPropertyType) o;
        return id == that.id &&
                Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, description);
    }
}
