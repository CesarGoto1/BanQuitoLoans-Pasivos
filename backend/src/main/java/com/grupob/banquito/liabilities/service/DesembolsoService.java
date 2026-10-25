package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.DesembolsoRequest;
import com.grupob.banquito.liabilities.dto.DesembolsoResultado;

public interface DesembolsoService {

    DesembolsoResultado credit(DesembolsoRequest request);
}
