package com.example.devopsapp.config;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableCaching
public class MemcachedConfig {

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new XMemcachedClient("memcached", 11211);
    }

    @Bean
    public CacheManager cacheManager() {
        return new SimpleCacheManager();
    }
}
