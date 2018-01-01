package smartbudget.repository;

import org.springframework.data.repository.CrudRepository;
import smartbudget.model.Expenses;

/**
 * Created by evgenyandroshchuk on 23.12.17.
 */
public interface ExpensesRepository extends CrudRepository<Expenses, Long> {
}
