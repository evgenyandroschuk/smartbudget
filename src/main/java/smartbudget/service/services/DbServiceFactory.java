package smartbudget.service.services;

import smartbudget.service.CommonRepository;
import smartbudget.service.ExpensesService;
import smartbudget.service.ExpensesTypeService;
import smartbudget.service.VehicleService;

public interface DbServiceFactory {

    ExpensesTypeService getExpensesTypeService();

    ExpensesService getExpensesService();

    CommonRepository getCommonService();

    VehicleService getVehicleService();

    PropertyService getPropertyService();
}
