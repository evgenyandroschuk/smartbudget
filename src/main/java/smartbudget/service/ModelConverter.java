package smartbudget.service;

import smartbudget.model.ExpensesType;
import smartbudget.view.ExpensesTypeView;

/**
 * Created by evgenyandroshchuk on 01.01.2018.
 */
public class ModelConverter {

    public static ExpensesTypeView convertToExpensesTypeView(ExpensesType expensesType) {

        ExpensesTypeView expensesTypeView = new ExpensesTypeView();

        expensesTypeView.setDescription(expensesType.getDesc());
        expensesTypeView.setDescriptionRus(expensesType.getDescRus());
        expensesTypeView.setIsActive(expensesType.isActive());
        expensesTypeView.setIsIncome(expensesType.isIncome());
        expensesTypeView.setId(expensesType.getId());

        return expensesTypeView;
    }
}
