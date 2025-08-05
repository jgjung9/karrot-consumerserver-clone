package karrot.chat.consumer.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "karrot.chat.consumer.domain.chat.repository",
        entityManagerFactoryRef = "chatEntityManager",
        transactionManagerRef = "chatTransactionManager"
)
public class ChatDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties chatDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource chatDataSource() {
        return chatDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean chatEntityManager(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("karrot.chat.consumer.domain.chat.entity")
                .persistenceUnit("chat")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager chatTransactionManager(
            EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    @Primary
    public JPAQueryFactory chatQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
