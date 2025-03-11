package com.crayon.common.data.transaction;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Consumer;

/**
 * @author Mengdl
 * @date 2025/03/11
 */
@Configuration
public class NoticeTransaction {

    private final static String FLAG = "message";


    public void transMessage(Consumer<String> consumer) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            consumer.accept(FLAG);
            return;
        }
        //有事务，注册事务同步器
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    consumer.accept(FLAG);
                }
            }
        });
    }

}
