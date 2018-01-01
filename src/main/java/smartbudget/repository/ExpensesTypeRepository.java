package smartbudget.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import smartbudget.model.ExpensesType;

import java.util.List;

/**
 * Created by evgenyandroshchuk on 21.12.17.
 */
public interface ExpensesTypeRepository extends CrudRepository<ExpensesType, Integer> {


    @Query("select e from ExpensesType e where isActive = true")
    public List<ExpensesType> findActiveExpensesType();


}
