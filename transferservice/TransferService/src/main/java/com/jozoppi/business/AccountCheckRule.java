package com.jozoppi.business;
import com.jozoppi.models.DomainEntity;

/**
 *  In the java world typically we might use JBoss Drools for enforcing business rules, especially
 *  when we have a lot of them. It will use the RETE algorithm, which decouple the rules from the facts.
 *  In this case for simplicity we provide a simple interface.
 *
 */
public class AccountCheckRule extends AbstractBusinessRule
{
        @Override
        /**
         * Validate the domain entity
         * @param account. Account to be used.
         */
        public void CheckRequest(DomainEntity account) throws Exception {

            // precondition: a  not empty or null domain entity
            // postcondition: a validated domain entity.

              if (account == null)
              {
                  throw new  IllegalArgumentException("Argument cannot be null");

              }
            /**
             *  We have in out domain an account object as object we shall avoid
             *  that our domain will turned in anemic domain, see Martin Fowler post.
             *  Each object shall have an identity, a state, and a behaviour. It s
             *  shall be able to validate itself.
             *  In an anemic domain we see a lot of services, objects with just state
             *  and no behaviour.
             */
            account.Validate();
        }
}
