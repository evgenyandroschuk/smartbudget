package smartbudget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import smartbudget.model.Expenses;
import smartbudget.model.ExpensesType;
import smartbudget.repository.ExpensesRepository;
import smartbudget.repository.ExpensesTypeRepository;

import java.util.Iterator;
import java.util.List;

/**
 * Created by evgenyandroshchuk on 21.12.17.
 */
@Service
@EnableAutoConfiguration
public class ExpensesService {

    @Autowired
    private ExpensesTypeRepository expensesTypeRepository;

    @Autowired
    private ExpensesRepository expensesRepository;


    public void saveExpensesType(ExpensesType type) {
        expensesTypeRepository.save(type);
    }

    public ExpensesType getExpensesType(Integer id) {
        return expensesTypeRepository.findOne(id);
    }

    public Iterable<ExpensesType> getExpensesTypeList() {
        return expensesTypeRepository.findAll();
    }

    public List<ExpensesType> findActiveExpensesType() {
        return expensesTypeRepository.findActiveExpensesType();
    }

    public Iterable<ExpensesType> findAllExpensesType() {
        return  expensesTypeRepository.findAll();
    }

    public void saveExpensesList(Iterable<Expenses> expenses) {
        expensesRepository.save(expenses);
    }




}
