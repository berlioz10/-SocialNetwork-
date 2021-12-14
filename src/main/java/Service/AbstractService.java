package Service;

import Domain.Identifiable;
import Exceptions.RepoException;
import Repo.Repository;
import Validate.Validator;

import java.sql.SQLException;

/**
 * @param <Id> tipul id-ului unic al fiecarui element din service
 * @param <T> tipul elementului care este un identifiable de Id
 */
public abstract class AbstractService<Id, T extends Identifiable<Id>> implements Service<Id, T> {
    protected Repository<Id, T> repository;
    protected Validator<Id, T> validator;

    /**
     * @param repository este repozitoriul folosit de catre service
     * @param validator este validatorul folosit de catre service
     */
    public AbstractService(Repository repository, Validator validator) {
        super();
        this.validator = validator;
        this.repository = repository;
    }

    /**
     * @return numarul de elemente din service
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    @Override
    public int count() throws SQLException {
        return repository.size();
    }

    /**
     * @return un iterable care contine elementele din service
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    @Override
    public Iterable<T> getRecords() throws SQLException {
        return repository.getAll();
    }

    /**
     * @param id este id-ul elementului cautat
     * @return este elementul cu id-ul id sau null daca nu exista in service
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    @Override
    public T findRecord(Id id) throws SQLException {
        return repository.find(id);
    }

    /**
     * @param id este id-ul elementului pe care dorim sa il stergem
     * @return elementul sters cu id-ul id
     * @throws RepoException arunca exceptie daca nu exista un element cu id-ul id la nivel de repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    @Override
    public T deleteRecord(Id id) throws RepoException, SQLException {
        return repository.delete(id);
    }
}
