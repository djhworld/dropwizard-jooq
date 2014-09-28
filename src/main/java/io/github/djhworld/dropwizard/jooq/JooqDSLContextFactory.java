package io.github.djhworld.dropwizard.jooq;

import ch.qos.logback.classic.Logger;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.LoggerFactory;

public class JooqDSLContextFactory {
    public DSLContext build(Environment environment,
                     DataSourceFactory configuration,
                     SQLDialect dialect) throws ClassNotFoundException {
        final ManagedDataSource dataSource = configuration.build(environment.metrics(), dialect.getName());
        return build(environment, configuration, dataSource, dialect);
    }

    /**
     * Needs better logging and instrumentation
     *
     * @param environment
     * @param configuration
     * @param dataSource
     * @param dialect
     * @return
     */
    public DSLContext build(Environment environment,
                     DataSourceFactory configuration,
                     ManagedDataSource dataSource,
                     SQLDialect dialect) {
        final String validationQuery = configuration.getValidationQuery();
        final DSLContext dslContext = DSL.using(dataSource, dialect);
        environment.lifecycle().manage(dataSource);
        environment.healthChecks().register(dialect.getName(), new JooqDSLContextHealthCheck(dslContext, validationQuery));
        return dslContext;
    }
}
