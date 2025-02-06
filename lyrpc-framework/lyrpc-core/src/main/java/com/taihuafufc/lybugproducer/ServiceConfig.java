package com.taihuafufc.lybugproducer;

import lombok.Data;
import lombok.Setter;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:35
 */
@Data
public class ServiceConfig<T> {

    private Class<T> interfaceClass;
    private T reference;

}
