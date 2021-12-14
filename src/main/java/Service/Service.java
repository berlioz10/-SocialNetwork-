package Service;

import Domain.Identifiable;
import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @param <Id> tipul id-ului unic al fiecarui element din service
 * @param <T> tipul elementului care este un identifiable de Id
 */
public interface Service<Id, T extends Identifiable<Id>> {
    /**
     * @return numarul de elemente din service
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    int count() throws SQLException;

    /**
     * @return un iterable care contine elementele din service
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    Iterable<T> getRecords() throws SQLException;

    /**
     * @param id este id-ul elementului cautat
     * @return este elementul cu id-ul id sau null daca nu exista in service
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    T findRecord(Id id) throws SQLException;

    /**
     * @param id este id-ul elementului pe care dorim sa il stergem
     * @return elementul sters cu id-ul id
     * @throws RepoException arunca exceptie daca nu exista un element cu id-ul id la nivel de repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    T deleteRecord(Id id) throws RepoException, SQLException;

    /**
     * @return genereaza un id nou unic, se recomanda a nu se folosi daca se lucreaza cu memorare in baza de date
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    Id generateId() throws SQLException;

    /**
     * @param params lista de parametri care caracterizeaza o intergistrare generica
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie la nivelul de servicii al aplicatiei daca parametri nu pot fi convertiti
     * @throws ValidateException arunca exceptie daca elementele listei de parametri nu sunt valide
     * @throws RepoException arunca exceptie daca exista un element cu id-ul folosit la nivel de repozitoriu
     */
    int createRecord(ArrayList<Object> params) throws SQLException, BusinessException, ValidateException, RepoException;

    /**
     * @param id este id-ul elementului pe care dorim sa il inlocuim
     * @param params reprezinta noile informatii referitoare la inregistrarea cu id-ul id (care nu va fi schimbat)
     * @throws BusinessException arunca exceptie la nivelul de servicii al aplicatiei daca parametri nu pot fi convertiti
     * @throws ValidateException arunca exceptie daca elementele listei de parametri nu sunt valide
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws RepoException arunca exceptie daca exista un element cu id-ul folosit la nivel de repozitoriu
     */
    void updateRecord(Id id, ArrayList<Object> params) throws BusinessException, ValidateException, SQLException, RepoException;
}
