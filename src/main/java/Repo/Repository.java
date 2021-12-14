package Repo;

import Domain.Identifiable;
import Exceptions.RepoException;

import java.sql.SQLException;

/**
 * @param <Id> id-ul generic al unui element din repozitoriu
 * @param <T>  instanta a clasei Identifiable
 *             permite efectuarea CRUD operations
 */

public interface Repository<Id, T extends Identifiable<Id>> {

    /**
     * @param t element de tip generic care va fi adaugat in repozitoriu
     * @throws RepoException arunca exceptie specifica contextului
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @return
     */
    Id add(T t) throws RepoException, SQLException;

    /**
     * @param id id-ul elementului de tip generic care va fi sters din repozitoriu
     * @return returneaza elementul generic sters daca a fost gasit
     * @throws RepoException arunca exceptie specifica contextului
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    T delete(Id id) throws RepoException, SQLException;

    /**
     * @param id id-ul elementului care va fi modificat
     * @param t  element de tip generic care va inlocui elementul cu id-ul id din repozitoriu
     * @throws RepoException arunca exceptie specifica contextului
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    void update(Id id, T t) throws RepoException, SQLException;

    /**
     * @param id id-ul elementului care va fi cautat in repozitoriu
     * @return un element generic relativ la id-ul dat ca parametru
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    T find(Id id) throws SQLException;

    /**
     * @return returneaza o lista iterabila de inregistrari generice
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    Iterable<T> getAll() throws SQLException;

    /**
     * @return returneaza numarul de inregistrari din repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    int size() throws SQLException;
}
