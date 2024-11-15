package com.test.simplecrud.constants;

import lombok.Getter;

@Getter
public class ValidatorMessages {
    private ValidatorMessages() {}

    public static final String MODEL_CANNOT_BE_NULL = "Modelo não pode estar vazio";
    public static final String COLOR_CANNOT_BE_NULL = "A cor do veículo não pode estar vazia";
    public static final String MANUFACTURER_CANNOT_BE_NULL ="Fabricante não pode estar vazio";
    public static final String FABRICATION_YEAR_CANNOT_BE_NULL = "Ano de fabricação não pode estar vazio";
    public static final String MODEL_YEAR_CANNOT_BE_NULL = "Ano do modelo não pode estar vazio";
    public static final String IDENTIFIER_CANNOT_BE_NULL = "A identificação do veículo não pode estar vazia";
    public static final String NAME_CANNOT_BE_EMPTY = "O Nome não pode estar vazio";
    public static final String MAIL_CANNOT_BE_EMPTY = "O email não pode estar vazio";
    public static final String PHONE_CANNOT_BE_EMPTY = "O telefone não pode estar vazio";
    public static final String PASSWORD_CANNOT_BE_EMPTY = "A senha não pode estar vazia";


}
