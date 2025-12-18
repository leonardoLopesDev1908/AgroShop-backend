package com.dailycodework.agroshop.service.Pagamento;

import com.dailycodework.agroshop.model.Pedido;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface IPagamentoService {
    
    public String criarPreference(Pedido pedido) throws MPException, MPApiException;

}
