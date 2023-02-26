package com.stefanini.converter;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Base64;

@Converter
public class ConversorCriptoString implements AttributeConverter<String, String> {

    private static final String CHAVE_CRIPTO = "50655368566D59713373367639792442";

    @Override
    public String convertToDatabaseColumn(String atributo) {
        try {
            SecretKeySpec chaveCripto = new SecretKeySpec(CHAVE_CRIPTO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, chaveCripto);
            byte[] bytesCripto = cipher.doFinal(atributo.getBytes());
            return Base64.getEncoder().encodeToString(bytesCripto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            SecretKeySpec chaveCripto = new SecretKeySpec(CHAVE_CRIPTO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, chaveCripto);
            byte[] bytesDecripto = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(bytesDecripto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }
}