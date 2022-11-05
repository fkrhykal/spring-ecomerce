package com.example.ecomerce.core;

public interface CommandHandler<T extends  Command, P> {
    public P handle(T command);
}
