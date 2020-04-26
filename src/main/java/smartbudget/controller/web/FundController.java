package smartbudget.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import smartbudget.model.funds.Currency;
import smartbudget.model.funds.FundData;
import smartbudget.service.postres.DateProvider;
import smartbudget.service.postres.fund.CurrencyDao;
import smartbudget.service.postres.fund.FundDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class FundController {

    private final FundDao fundDao;
    private final DateProvider dateProvider;
    private static final int DEFAULT_USER = 1;
    private final CurrencyDao currencyDao;

    @Autowired
    public FundController(FundDao fundDao, DateProvider dateProvider, CurrencyDao currencyDao) {
        this.fundDao = fundDao;
        this.dateProvider = dateProvider;
        this.currencyDao = currencyDao;
    }

    @RequestMapping(value = "/fund", method = RequestMethod.GET)
    public String getFund(Model model) {
        LocalDate startDate = dateProvider.now().minusYears(50);
        LocalDate endDate = dateProvider.now().plusDays(1);

        model.addAttribute("funds", fundDao.getFundData(DEFAULT_USER, startDate, endDate));
        return "fund/fund";
    }

    @RequestMapping(value = "/fund/change", method = RequestMethod.GET)
    public String changeFundBalance(Model model) {
        model.addAttribute("currencies", currencyDao.getCurrencies());
        return "fund/fund_change";
    }

    @RequestMapping(value = "/fund/change/response", method = RequestMethod.GET)
    public String responseChangeFundBalance(
        Model model,
        @RequestParam String currencyId,
        @RequestParam String description,
        @RequestParam String amount,
        @RequestParam String price,
        @RequestParam String date
    ) {
        FundData fundData = new FundData(
            null,
            DEFAULT_USER,
            currencyDao.getCurrencyById(Integer.parseInt(currencyId)),
            new BigDecimal(amount),
            new BigDecimal(price),
            description,
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        fundDao.addFund(fundData);
        model.addAttribute("fund", fundData);
        return "fund/fund_change_response";
    }


    @RequestMapping(value = "/currency/change", method = RequestMethod.GET)
    public String updateCurrency(Model model) {
        model.addAttribute("currencies", currencyDao.getCurrencies());
        return "fund/currency_change";
    }

    @RequestMapping(value = "/currency/change/response", method = RequestMethod.GET)
    public String updateCurrencyResponse(
        Model model,
        @RequestParam String currencyId,
        @RequestParam String price
    ) {
        Currency currency = currencyDao.getCurrencyById(Integer.parseInt(currencyId));
        currency.setPrice(new BigDecimal(price));
        currencyDao.setCurrency(currency);
        model.addAttribute("currency", currency);
        return "fund/currency_change_response";
    }
}
