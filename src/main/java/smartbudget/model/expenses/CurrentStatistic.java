package smartbudget.model.expenses;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentStatistic {

    private BigDecimal expensesTotalAmount;
    private BigDecimal openBalanceAmount;
    private String openBalanceDate;
    private BigDecimal restAmount;

    private BigDecimal fundDollarAmount;
    private BigDecimal fundDollarAmountInRub;

    private BigDecimal fundEuroAmount;
    private BigDecimal fundEuroAmountInRub;

    private BigDecimal fundRubAmount;

    private BigDecimal allAmountInRub;

    private BigDecimal dollarPrice;
    private BigDecimal euroPrice;


    private CurrentStatistic(
        BigDecimal expensesTotalAmount,
        BigDecimal openBalanceAmount,
        String openBalanceDate,
        BigDecimal restAmount,
        BigDecimal fundDollarAmount,
        BigDecimal fundDollarAmountInRub,
        BigDecimal fundEuroAmount,
        BigDecimal fundEuroAmountInRub,
        BigDecimal fundRubAmount,
        BigDecimal allAmountInRub,
        BigDecimal dollarPrice,
        BigDecimal euroPrice

    ) {
        this.expensesTotalAmount = expensesTotalAmount;
        this.openBalanceAmount = openBalanceAmount;
        this.openBalanceDate = openBalanceDate;
        this.restAmount = restAmount;
        this.fundDollarAmount = fundDollarAmount;
        this.fundDollarAmountInRub = fundDollarAmountInRub;
        this.fundEuroAmount = fundEuroAmount;
        this.fundEuroAmountInRub = fundEuroAmountInRub;
        this.fundRubAmount = fundRubAmount;
        this.allAmountInRub = allAmountInRub;
        this.dollarPrice = dollarPrice;
        this.euroPrice = euroPrice;
    }



    public BigDecimal getExpensesTotalAmount() {
        return expensesTotalAmount;
    }

    public BigDecimal getOpenBalanceAmount() {
        return openBalanceAmount;
    }

    public String getOpenBalanceDate() {
        return openBalanceDate;
    }

    public BigDecimal getRestAmount() {
        return restAmount;
    }

    public BigDecimal getFundDollarAmount() {
        return fundDollarAmount;
    }

    public BigDecimal getFundDollarAmountInRub() {
        return fundDollarAmountInRub;
    }

    public BigDecimal getFundEuroAmount() {
        return fundEuroAmount;
    }

    public BigDecimal getFundEuroAmountInRub() {
        return fundEuroAmountInRub;
    }

    public BigDecimal getFundRubAmount() {
        return fundRubAmount;
    }

    public BigDecimal getAllAmountInRub() {
        return allAmountInRub;
    }

    public BigDecimal getDollarPrice() {
        return dollarPrice;
    }

    public BigDecimal getEuroPrice() {
        return euroPrice;
    }

    public static class CurrentStatisticBuilder {

        private BigDecimal expensesTotalAmount;
        private BigDecimal openBalanceAmount;
        private String openBalanceDate;
        private BigDecimal restAmount;

        private BigDecimal fundDollarAmount;
        private BigDecimal fundDollarAmountInRub;

        private BigDecimal fundEuroAmount;
        private BigDecimal fundEuroAmountInRub;

        private BigDecimal fundRubAmount;

        private BigDecimal allAmountInRub;

        private BigDecimal dollarPrice;
        private BigDecimal euroPrice;

        public static CurrentStatisticBuilder builder() {
            return new CurrentStatisticBuilder();
        }



        public CurrentStatisticBuilder setExpensesTotalAmount(BigDecimal expensesTotalAmount) {
            this.expensesTotalAmount = expensesTotalAmount;
            return this;
        }

        public CurrentStatisticBuilder setOpenBalanceAmount(BigDecimal openBalanceAmount) {
            this.openBalanceAmount = openBalanceAmount;
            return this;
        }

        public CurrentStatisticBuilder setOpenBalanceDate(String openBalanceDate) {
            this.openBalanceDate = openBalanceDate;
            return this;
        }

        public CurrentStatisticBuilder setRestAmount(BigDecimal restAmount) {
            this.restAmount = restAmount;
            return this;
        }

        public CurrentStatisticBuilder setFundDollarAmount(BigDecimal fundDollarAmount) {
            this.fundDollarAmount = fundDollarAmount;
            return this;
        }

        public CurrentStatisticBuilder setFundDollarAmountInRub(BigDecimal fundDollarAmountInRub) {
            this.fundDollarAmountInRub = fundDollarAmountInRub;
            return this;
        }

        public CurrentStatisticBuilder setFundEuroAmount(BigDecimal fundEuroAmount) {
            this.fundEuroAmount = fundEuroAmount;
            return this;
        }

        public CurrentStatisticBuilder setFundEuroAmountInRub(BigDecimal fundEuroAmountInRub) {
            this.fundEuroAmountInRub = fundEuroAmountInRub;
            return this;
        }

        public CurrentStatisticBuilder setFundRubAmount(BigDecimal fundRubAmount) {
            this.fundRubAmount = fundRubAmount;
            return this;
        }

        public CurrentStatisticBuilder setAllAmountInRub(BigDecimal allAmountInRub) {
            this.allAmountInRub = allAmountInRub;
            return this;
        }

        public CurrentStatisticBuilder setDollarPrice(BigDecimal dollarPrice) {
            this.dollarPrice = dollarPrice;
            return this;
        }

        public CurrentStatisticBuilder setEuroPrice(BigDecimal euroPrice) {
            this.euroPrice = euroPrice;
            return this;
        }

        public CurrentStatistic build() {
            return new CurrentStatistic(
                expensesTotalAmount,openBalanceAmount, openBalanceDate, restAmount, fundDollarAmount,
                fundDollarAmountInRub, fundEuroAmount, fundEuroAmountInRub, fundRubAmount, allAmountInRub,
                dollarPrice, euroPrice
            );
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CurrentStatistic{");
        sb.append("expensesTotalAmount=").append(expensesTotalAmount);
        sb.append(", openBalanceAmount=").append(openBalanceAmount);
        sb.append(", openBalanceDate='").append(openBalanceDate).append('\'');
        sb.append(", restAmount=").append(restAmount);
        sb.append(", fundDollarAmount=").append(fundDollarAmount);
        sb.append(", fundDollarAmountInRub=").append(fundDollarAmountInRub);
        sb.append(", fundEuroAmount=").append(fundEuroAmount);
        sb.append(", fundEuroAmountInRub=").append(fundEuroAmountInRub);
        sb.append(", fundRubAmount=").append(fundRubAmount);
        sb.append(", allAmountInRub=").append(allAmountInRub);
        sb.append(", dollarPrice=").append(dollarPrice);
        sb.append(", euroPrice=").append(euroPrice);
        sb.append('}');
        return sb.toString();
    }
}
