package Repo;

import Domain.User;

/**
 *  repozitoriu specializat care retine date despre utilizatori in memorie ca si HashMap
 */
public class InMemoryUserRepository extends InMemoryRepository<Integer, User> {
    /**
     *  constructor standard
     */
    public InMemoryUserRepository() {
        super();
    }
}
