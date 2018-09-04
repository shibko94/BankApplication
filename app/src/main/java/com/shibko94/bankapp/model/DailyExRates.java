package com.shibko94.bankapp.model;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "DailyExRates", strict = false)
public class DailyExRates {
    @ElementList(inline = true)
    public List<Currency> rates;

}

