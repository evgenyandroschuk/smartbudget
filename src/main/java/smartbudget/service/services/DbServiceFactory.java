package smartbudget.service.services;

import smartbudget.service.CommonService;
import smartbudget.service.ExpensesService;
import smartbudget.service.ExpensesTypeService;
import smartbudget.service.VehicleService;

public interface DbServiceFactory {

    ExpensesTypeService getExpensesTypeService();

    ExpensesService getExpensesService();

    CommonService getCommonService();

    VehicleService getVehicleService();

    PropertyService getPropertyService();
}
