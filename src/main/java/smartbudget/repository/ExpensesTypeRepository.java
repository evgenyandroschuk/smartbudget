package smartbudget.repository;

import org.springframework.data.repository.CrudRepository;
import smartbudget.model.ExpensesType;

/**
 * Created by evgenyandroshchuk on 21.12.17.
 */
public interface ExpensesTypeRepository extends CrudRepository<ExpensesType, Integer> {


}
