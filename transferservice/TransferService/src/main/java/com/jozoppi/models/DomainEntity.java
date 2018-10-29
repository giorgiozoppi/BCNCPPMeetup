package com.jozoppi.models;

/**
 * This is a marker interface for each entity of the domain
 * to make more convenient the validation.
 *
 */
public interface DomainEntity
{
    /*
    *  It validate an entity and return an exception in case of validation error.
    *
    */
    void Validate() throws Exception;
}
