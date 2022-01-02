package Service;

import Domain.User;
import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;
import Repo.Repository;
import Validate.Validator;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * clasa service specializata pentru operatii cu relatii de prietenie
 */
public class UserService extends AbstractService<Integer, User>{

    /**
     * @param repository este repozitoriul folosit de catre service
     * @param validator este validatorul folosit de catre service
     */
    public UserService(Repository repository, Validator validator) {
        super(repository, validator);
    }

    /**
     * @return genereaza un id nou unic, se recomanda a nu se folosi daca se lucreaza cu memorare in baza de date
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    @Override
    public Integer generateId() throws SQLException {
        if (repository.size() == 0)
            return 1;
        for (int i = 1; i < repository.size(); i++) {
            if (repository.find(i) == null)
                return i;
        }
        return repository.size() + 1;
    }

    /**
     * @param params lista de parametri care caracterizeaza o intergistrare generica
     *               lista de parametri trebuie sa contina doua elemente care vor deveni
     *               prenumele, respectiv numele utilizatorului (String,String)
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie la nivelul de servicii al aplicatiei daca parametri nu pot fi convertiti
     * @throws ValidateException arunca exceptie daca elementele listei de parametri nu sunt valide
     * @throws RepoException arunca exceptie daca exista un element cu id-ul folosit la nivel de repozitoriu
     */
    @Override
    public int createRecord(ArrayList<Object> params) throws SQLException, BusinessException, ValidateException, RepoException {
        if(params.size()!=4)
            throw new BusinessException("Numar invalid de parametri\n");

        String firstName =(String)params.get(0);
        String surname =(String)params.get(1);
        String username = (String)params.get(2);
        String password = (String)params.get(3);

        User user = new User(firstName, surname, username, password);
        validator.validate(user);
        int id = repository.add(user);
        return id;
    }

    /**
     * @param id     este id-ul elementului pe care dorim sa il inlocuim
     * @param params reprezinta noile informatii referitoare la inregistrarea cu id-ul id (care nu va fi schimbat)
     * @throws BusinessException
     * @throws ValidateException
     * @throws SQLException
     * @throws RepoException
     */
    @Override
    public void updateRecord(Integer id, ArrayList<Object> params) throws BusinessException, ValidateException, SQLException, RepoException {
        if(params.size()!=2)
            throw new BusinessException("Numar invalid de parametri\n");

        String newFirstName =(String)params.get(0);
        String newSurname =(String)params.get(1);

        User user = new User(id, newFirstName, newSurname);
        validator.validate(user);
        repository.update(id, user);
    }
}
