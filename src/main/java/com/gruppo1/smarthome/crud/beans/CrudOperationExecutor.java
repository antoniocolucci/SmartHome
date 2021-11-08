package com.gruppo1.smarthome.crud.beans;

import com.gruppo1.smarthome.crud.api.CrudOperation;
import org.springframework.stereotype.Component;

@Component
public class CrudOperationExecutor {

    public Object execute(CrudOperation operation, Object homeItem) {
        return operation.execute(homeItem);
    }

    public Object execute(CrudOperation operation, String itemName, Object s) {
        return operation.execute(s, itemName);
    }
}
