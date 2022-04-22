package com.maiworm.sushi.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.maiworm.sushi.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.maiworm.sushi.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.maiworm.sushi.domain.User.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Authority.class.getName());
            createCache(cm, com.maiworm.sushi.domain.User.class.getName() + ".authorities");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".especiais");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".entradas");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".sushis");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".sashimis");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".makimonos");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".hots");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".harumakis");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".temakis");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".yakisobas");
            createCache(cm, com.maiworm.sushi.domain.Cardapio.class.getName() + ".uramakis");
            createCache(cm, com.maiworm.sushi.domain.Especiais.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Entrada.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Sushi.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Sashimi.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Makimono.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Hot.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Harumaki.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Temaki.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Yakisoba.class.getName());
            createCache(cm, com.maiworm.sushi.domain.Uramaki.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
