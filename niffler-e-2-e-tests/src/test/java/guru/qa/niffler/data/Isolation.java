package guru.qa.niffler.data;

import lombok.Getter;

import java.sql.Connection;

public enum Isolation {
    NONE(Connection.TRANSACTION_NONE),

    UNCOMMITED(Connection.TRANSACTION_READ_UNCOMMITTED),

    READ_COMMITED(Connection.TRANSACTION_READ_COMMITTED),

    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    @Getter
    private final int value;

    Isolation(int isolationLvl) {
        this.value = isolationLvl;
    }
}
