package br.com.beeahead.smart_config.esptouch.security;

public interface ITouchEncryptor {
    byte[] encrypt(byte[] src);
}
