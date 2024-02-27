package com.example.springehcache.config;

import com.example.springehcache.entity.Member;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@EnableCaching
@Configuration
public class EhcacheConfig {

    @Bean
    public CacheManager cacheManger() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        CacheConfiguration<Object, Object> configuration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Object.class, Object.class,
                        ResourcePoolsBuilder.heap(100) // Define size of heap
                                .offheap(10, MemoryUnit.MB))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
                .build();
        javax.cache.configuration.Configuration<Object, Object> cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(configuration);
        cacheManager.createCache("allMember", cacheConfiguration);
        cacheManager.createCache("pageMember", cacheConfiguration);
        return cacheManager;
    }

//    @Bean
//    public CacheManager ehCacheManager() {
//        CacheConfiguration<Long, Member> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
//                Long.class, Member.class,
//                ResourcePoolsBuilder.heap(100) // Define size of heap
//        ).build();
//
//        return CacheManagerBuilder.newCacheManagerBuilder()
//                .withCache("allMember", cacheConfiguration)
//                .build(true); // `true` means the cache manager will be initialized
//    }
}
