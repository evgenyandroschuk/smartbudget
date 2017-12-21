package smartbudget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import smartbudget.model.ExpensesType;
import smartbudget.repository.ExpensesTypeRepository;

/**
 * Created by evgenyandroshchuk on 21.12.17.
 */
@Service
@EnableAutoConfiguration
public class ExpensesService {

    @Autowired
    private ExpensesTypeRepository expensesTypeRepository;

    public void saveExpensesType(ExpensesType type) {
        expensesTypeRepository.save(type);
    }

    public ExpensesType getExpensesType(Integer id) {
        return expensesTypeRepository.findOne(id);
    }




}
