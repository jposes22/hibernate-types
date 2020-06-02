package com.vladmihalcea.hibernate.type.util;

import org.hibernate.Filter;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.AbstractQueryImpl;

import javax.persistence.Query;
import java.util.Collections;

/**
 * The {@link com.vladmihalcea.hibernate.type.util.SQLExtractor} allows you to extract the
 * underlying SQL query generated by a JPQL or JPA Criteria API query.
 * <p>
 * For more details about how to use it, check out <a href="https://vladmihalcea.com/get-sql-from-jpql-or-criteria/">this article</a> on <a href="https://vladmihalcea.com/">vladmihalcea.com</a>.
 *
 * @author Vlad Mihalcea
 * @since 2.9.11
 */
public class SQLExtractor {

    private SQLExtractor() {
        throw new UnsupportedOperationException("SQLExtractor is not instantiable!");
    }

    /**
     * Get the underlying SQL generated by the provided JPA query.
     *
     * @param query JPA query
     * @return the underlying SQL generated by the provided JPA query
     */
    public static String from(Query query) {
        AbstractQueryImpl abstractQuery = query.unwrap(AbstractQueryImpl.class);
        SessionImplementor session = ReflectionUtils.getFieldValue(abstractQuery, "session");
        String[] sqls = session.getFactory()
            .getQueryPlanCache()
            .getHQLQueryPlan(abstractQuery.getQueryString(), false, Collections.<String, Filter>emptyMap())
            .getSqlStrings();

        return sqls.length > 0 ? sqls[0] : null;
    }
}
