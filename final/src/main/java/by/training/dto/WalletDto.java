package by.training.dto;

import by.training.constant.ValuesContainer;
import by.training.entity.Wallet;

public class WalletDto {

    private long id;
    private double balance = 0;
    private Wallet.CurrencyType currency = ValuesContainer.DEFAULT_CURRENCY;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Wallet.CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(Wallet.CurrencyType currency) {
        this.currency = currency;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static final class Builder {
        private long id;
        private double balance = 0;
        private Wallet.CurrencyType currency = ValuesContainer.DEFAULT_CURRENCY;
        private long userId;

        private Builder() {
        }

        public static Builder aWalletDto() {
            return new Builder();
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder balance(double balance) {
            this.balance = balance;
            return this;
        }

        public Builder currency(Wallet.CurrencyType currency) {
            this.currency = currency;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public WalletDto build() {
            WalletDto walletDto = new WalletDto();
            walletDto.setId(id);
            walletDto.setBalance(balance);
            walletDto.setCurrency(currency);
            walletDto.setUserId(userId);
            return walletDto;
        }
    }
}