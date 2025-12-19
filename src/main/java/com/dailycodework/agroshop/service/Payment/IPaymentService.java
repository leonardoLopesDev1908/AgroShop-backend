package com.dailycodework.agroshop.service.Payment;

import com.dailycodework.agroshop.model.Order;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface IPaymentService {
    
    public String criarPreference(Order pedido) throws MPException, MPApiException;

}
