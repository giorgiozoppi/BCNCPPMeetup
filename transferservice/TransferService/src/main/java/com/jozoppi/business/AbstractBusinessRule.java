package com.jozoppi.business;

import com.jozoppi.models.DomainEntity;

/*
 * We need an extensible way to check the validation rules, for
 * being able to add multiple rules to be checked. In this case
 * we implement a chain of responsibility design pattern.
 * So our code is prepared to add further rules, prepared for changing.
 */
public abstract class AbstractBusinessRule implements BusinessRule
{
        private BusinessRule successor;

        public abstract void CheckRequest(DomainEntity account) throws Exception;

        /**
        *  This method set the next business rule to be enforced in the web service.
        * @param successor AbstractBusinessRule to be used.
        */
        public void setSuccessor(AbstractBusinessRule successor) {
            this.successor = successor;
        }
        /**
        *  Pass the domain entity to be used within the rules.
        *  In case of any violation it will trigger an exception since
        *  we have violated a business contract.
        *   @param request Domain entity to be used.
        * */
        public void processRequest(DomainEntity request) throws Exception {

            CheckRequest(request);

            if (successor != null) {
                successor.CheckRequest(request);
            }
        }
}
