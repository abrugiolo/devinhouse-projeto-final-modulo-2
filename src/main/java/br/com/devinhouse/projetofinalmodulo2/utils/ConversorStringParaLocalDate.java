package br.com.devinhouse.projetofinalmodulo2.utils;

import org.modelmapper.AbstractConverter;

import java.time.LocalDate;

public class ConversorStringParaLocalDate extends AbstractConverter<String, LocalDate> {
    @Override
    protected LocalDate convert(String s) {
        return LocalDate.parse(s);
    }
}
