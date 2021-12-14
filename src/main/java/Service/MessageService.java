package Service;

import Domain.Message;
import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;
import Repo.Repository;
import Validate.Validator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class MessageService extends AbstractService<Integer, Message> {
    public MessageService(Repository repository, Validator validator) {
        super(repository, validator);
    }

    /**
     * @depreacted we don't need an id generator anymore
     */
    @Override
    public Integer generateId() throws SQLException {
        // mnu
        return 0;
    }

    /**
     * add a message to the repo
     * @param params the params to form a message
     * @throws SQLException database error
     * @throws BusinessException business error
     * @throws ValidateException validation message error
     * @throws RepoException Repository rules error
     */
    @Override
    public int createRecord(ArrayList<Object> params) throws SQLException, BusinessException, ValidateException, RepoException {
        if(params.size() != 4)
            throw new BusinessException("Numar invalid de parametri\n");

        Integer id_reply;
        Object id_reply_object = params.get(3);
        if(id_reply_object == null)
            id_reply = null;
        else
            id_reply = (Integer) id_reply_object;
        Message message;
        if(params.get(2).getClass().isAssignableFrom(Integer.class))
         message = new Message(
                 (Integer) params.get(2),
                 (Integer) params.get(0),
                 (Integer) params.get(1),
                 null,
                 Timestamp.from(Instant.now()),
                 id_reply
        );
        else
            message = new Message(
                    1,
                    1,
                    (String) params.get(2),
                    Timestamp.from(Instant.now()),
                    null
            );
        validator.validate(message);
        return repository.add(message);
    }

    /**
     * update the message with a specific id ( we don't need it)
     * @param integer the id of the message
     * @param params the components to form a message
     * @throws SQLException database error
     * @throws BusinessException business error
     * @throws ValidateException validation message error
     * @throws RepoException Repository rules error
     */
    @Override
    public void updateRecord(Integer integer, ArrayList<Object> params) throws BusinessException, ValidateException, SQLException, RepoException {

    }
}
