package br.com.devinhouse.projetofinalmodulo2.utils;

import org.modelmapper.AbstractConverter;

import java.time.LocalDate;

public class ConversorLocalDateParaString extends AbstractConverter<LocalDate, String> {
    @Override
    protected String convert(LocalDate localDate) {
        return localDate.toString();
    }
}
