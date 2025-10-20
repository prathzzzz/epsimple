package com.eps.module.common.seeder.base;

import java.util.concurrent.CompletableFuture;

public interface BaseSeeder {
    CompletableFuture<Void> seed();
    String getSeederName();
    boolean isEnabled();
}
