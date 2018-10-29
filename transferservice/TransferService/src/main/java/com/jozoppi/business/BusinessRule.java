package com.jozoppi.business;
import com.jozoppi.models.DomainEntity;
/*
 * This implement a common interface for the business rules that can be
 * enforced by the web service. I know that in this case
 * it is more complexity than needed, but we should
 */
public interface BusinessRule
{
    /**
     * Interface for checking the business rules.
     * @param account A entity of the domain.
     * @throws Exception In case the business validation fails.
     */
    void CheckRequest(DomainEntity account) throws Exception;
}

