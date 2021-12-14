package Control;

import Domain.Friendship;
import Domain.Message;
import Domain.User;
import Exceptions.BusinessException;
import Exceptions.RepoException;
import Exceptions.ValidateException;
import Repo.DatabaseFriendshipRepository;
import Repo.DatabaseMessageRepository;
import Repo.DatabaseUserRepository;
import Repo.Repository;
import Service.Service;
import Utils.Graph;
import Validate.FriendshipValidator;
import Validate.MessageValidator;
import Validate.UserValidator;
import Validate.Validator;
import Service.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *  clasa care extrage informatii de la nivelul de business din diferentele sectiuni ale aplicatiei
 */
public class Controller {
    private static Controller controller = new Controller();
    private Service<Integer, User> userService;
    private Service<Integer, Friendship> friendshipService;
    private Service<Integer, Message> messageService;

    private Controller() {
        String url = "jdbc:postgresql://localhost:5432/mesagerie";
        Repository<Integer, User> userRepository = new DatabaseUserRepository(
                url, "postgres", "1234");
        Validator<Integer, User> userValidator = new Validator<>(new UserValidator());
        userService = new UserService(userRepository, userValidator);

        Repository<Integer, Friendship> friendshipRepository = new DatabaseFriendshipRepository(
                url, "postgres", "1234");
        Validator<Integer, Friendship> friendshipValidator = new Validator<>(new FriendshipValidator());
        friendshipService = new FriendshipService(friendshipRepository, friendshipValidator);

        Repository<Integer, Message> messageRepository = new DatabaseMessageRepository(
                url, "postgres", "1234");
        Validator<Integer, Message> messageValidator = new Validator<>(new MessageValidator());
        messageService = new MessageService(messageRepository, messageValidator);
    }

    public static Controller getInstance() {
        return controller;
    }


    /**
     * @deprecated Controller-ul va fi de tip singleton, deci mai bine nu ati folosi aceasta clasa
     * @param userService service care contine date despre utilizatorii aplicatiei
     * @param friendshipService service care contine date despre relatiile de prietenie dintre utilizatori
     * @param messageService service care contine date despre mesajele trimise intre utilizatorii aplicatiei
     */
    @Deprecated
    public Controller(Service<Integer, User> userService, Service<Integer, Friendship> friendshipService, Service<Integer, Message> messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
    }

    /**
     * @deprecated remains only for tests, use Controller(userService, friendshipService, messageService) instead
     * @param userService user's service
     * @param friendshipService friendship's service
     */
    @Deprecated
    public Controller(Service userService, Service friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     * @param firstName prenumele utilizatorului care va fi adaugat
     * @param surname numele utilizatorului care va fi adaugat
     * @throws ValidateException arunca exceptie daca numele/prenumele nu sunt valide
     * @throws RepoException arunca exceptie daca apare o coliziune de id in repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     */
    public void addUser(String firstName, String surname) throws ValidateException, RepoException, SQLException, BusinessException, SQLException {
        ArrayList<Object> params = new ArrayList<>();
        params.add(firstName);
        params.add(surname);
        userService.createRecord(params);
    }

    /**
     * @param id este id-ul utilizatorului caruia dorim sa ii aflam relatiile de prietenie (potentiale)
     * @return un vector de id-uri reprezentand id-urile relatiilor de prietenie ale utilizatorului in cauza
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    private Vector<Integer> friendsOf(int id) throws SQLException {
        Vector<Integer> friendshipVector = new Vector<>();
        for (Friendship friendship : friendshipService.getRecords()) {
            if (friendship.getTwo() == id || friendship.getOne() == id)
                friendshipVector.add(friendship.getId());
        }
        return friendshipVector;
    }

    /**
     * @param id este id-ul utilizatorului pe care dorim sa il stergem din aplicatie
     * @return utilizatorul sters din aplicatie
     * @throws RepoException arunca exceptie daca nu exista elementul cu id-ul dat in repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public User deleteUser(int id) throws RepoException, SQLException {
        return userService.deleteRecord(id);
    }

    /**
     * @param id este id-ul utilizatorului pe care il cautam
     * @return utilizatorul cu id-ul id
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public User findUser(int id) throws SQLException {
        return userService.findRecord(id);
    }

    /**
     * @return o lista iterabila cu toti utilizatorii
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<User> getUsers() throws SQLException {
        return userService.getRecords();
    }

    /**
     * @return returneaza numarul de utilizatori din aplicatie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public int numberOfUsers() throws SQLException {
        return userService.count();
    }

    /**
     * @param id este id-ul utilizatorului pe care il modificam
     * @param newFirstName este noul prenume al utilizatorului
     * @param newSurname este noul nume al utilizatorului
     * @throws ValidateException arunca exceptie daca id-ul/numele/prenumele nu sunt valide
     * @throws RepoException arunca exceptie daca nu exista utilizatorul cu id-ul id in aplicatie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     */
    public void updateUser(int id, String newFirstName, String newSurname) throws ValidateException, RepoException, SQLException, BusinessException {
        ArrayList<Object> params = new ArrayList<>();
        params.add(newFirstName);
        params.add(newSurname);
        userService.updateRecord(id, params);
    }

    /**
     * @param one id-ul unuia dintre utilizatorii prieteni
     * @param two id-ul celuilalt utilizator prieten
     * @throws ValidateException arunca exceptie daca id-urile utilizatorilor nu sunt valide
     * @throws RepoException arunca exceptie daca exista coliziune de id la nivel de repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     */
    public void addFriendship(int one, int two) throws ValidateException, RepoException, SQLException, BusinessException {
        if (userService.findRecord(one) == null || userService.findRecord(two) == null || one == two)
            throw new ValidateException("Pereche id invalida\n");

        ArrayList<Object> params = new ArrayList<>();
        params.add(one);
        params.add(two);

        friendshipService.createRecord(params);
    }

    /**
     * @param id este id-ul relatiei de prietenie care va fi stearsa
     * @return relatie de prietenie stearsa
     * @throws RepoException arunca exceptie daca nu exista realtia cu id-ul id la nivel de repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Friendship deleteFriendship(int id) throws RepoException, SQLException {
        return friendshipService.deleteRecord(id);
    }

    /**
     * @param id este id-ul relatiei de prietenie pe care o cautam
     * @return relatia de prietenie cu id-ul id
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Friendship findFriendship(int id) throws SQLException {
        return friendshipService.findRecord(id);
    }

    /**
     * @return un obiect iterabil care contine toate relatiile de prietenie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<Friendship> getFriendships() throws SQLException {
        return friendshipService.getRecords();
    }

    /**
     * @return numarul de relatii de prietenie din aplicatie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public int numberOfFriendships() throws SQLException {
        return friendshipService.count();
    }

    /**
     * @param id este id-ul relatiei de prietenie care va fi modificata
     * @param newOne este id-ul unuia dintre utilizatorii prieteni
     * @param newTwo este id-ul celuilalt utilizator prieten
     * @throws ValidateException daca id-urile utilizatorilor nu sunt valide
     * @throws RepoException arunca exceptie daca prietenia cu id-ul id nu exista la nivel de repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     */
    public void updateFriendship(int id, int newOne, int newTwo) throws ValidateException, RepoException, SQLException, BusinessException {
        if (userService.findRecord(newOne) == null || userService.findRecord(newTwo) == null)
            throw new ValidateException("Pereche id invalida\n");

        ArrayList<Object> params = new ArrayList<>();
        params.add(newOne);
        params.add(newTwo);

        friendshipService.updateRecord(id, params);
    }

    /**
     * @return numarul de comunitati din aplicatie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public int numberOfCommunities() throws SQLException {
        Graph graph = new Graph(userService, friendshipService);
        return graph.connected();
    }

    /**
     * @return cea mai sociabila comunitate ca si lista de utilizatori
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public ArrayList<User> mostSocial() throws SQLException {
        Graph graph = new Graph(userService, friendshipService);
        return graph.socialComponent();
    }

    /**
     * @param id_from este id-ul utilizatorului care trimite mesajul
     * @param id_to este id-ul utiliatorului spre care vs fi trimis mesajul
     * @param message este corpul mesajului
     * @param id_reply este id-ul mesajului la care da reply noul mesaj ( daca este un reply)
     * @throws ValidateException daca datele nu sunt valide
     * @throws RepoException arunca exceptie daca mesajul exista la nivel de repozitoriu
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     */
    public void sendMessage(int id_from, int id_to, String message, Integer id_reply) throws SQLException, ValidateException, BusinessException, RepoException {
        if (userService.findRecord(id_from) == null || userService.findRecord(id_to) == null)
            throw new ValidateException("Id utilizatori invalid\n");

        ArrayList<Object> params = new ArrayList<>();
        params.add(id_from);
        params.add(id_to);
        params.add(message);
        params.add(id_reply);
        List<Friendship> friendships = (List<Friendship>) friendshipService.getRecords();
        boolean ok = friendships.stream().anyMatch((x) ->
                                ((x.getOne() == id_from && x.getTwo() == id_to) ||
                                (x.getOne() == id_to && x.getTwo() == id_from)) &&
                                x.getFriendship_request() == 2);
        if(!ok)
            throw new BusinessException("Nu exista prietenie dintre cei doi utilizatori\n");
        if(id_reply != null)
        {
            Message message1 = messageService.findRecord(id_reply);
            if((message1.getTo() != id_from || message1.getFrom() != id_to) &&
                    (message1.getTo() != id_to || message1.getFrom() != id_from)) {
                // System.out.println(message1.getTo() + " " + id_to + " " + message1.getFrom() + " " + id_from);
                throw new BusinessException("Mesajul replied nu apartine acestei conversatii\n");
            }
        }
        int id_message = messageService.createRecord(params);
        params = new ArrayList<>();
        params.add(id_from);
        params.add(id_to);
        params.add(id_message);
        params.add(id_reply);
        messageService.createRecord(params);
    }

    /**
     * @return un iterable care contine toate mesajele din aplicatie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<Message> getMessages() throws SQLException {
        List<Message> messages = (List<Message>) messageService.getRecords();

        return messages.stream().sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toList());
    }

    /**
     * @param id este id-ul mesajului care va fi sters
     * @return mesajul cu id-ul id sters din aplicatie
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws RepoException arunca exceptie daca mesajul cu id-ul id nu exista la nivel de repozitoriu
     */
    public Message deleteMessage(int id) throws SQLException, RepoException {
        return messageService.deleteRecord(id);
    }

    /**
     * @param id1 este id-ul unuia dintre cei doi utilizatori
     * @param id2 este id-ul celuilalt utilizator
     * @return un iterable care contine corpul mesajelor
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<String> getMessagesBy2Users(int id1, int id2) throws SQLException {
        List<Message> messages = (List<Message>) messageService.getRecords();
        ArrayList<String> messages_filtered = new ArrayList<>();
        User user1 = findUser(id1);
        User user2 = findUser(id2);
        messages = messages.stream().sorted(
                Comparator.comparing(Message::getDate)).collect(Collectors.toList());
        for(Message message : messages) {
            if(message.getFrom() == id1 && message.getTo() == id2) {
                if(message.getId_reply() != null) {
                    Message message1 = messageService.findRecord(message.getId_reply());
                    messages_filtered.add("Reply la " + message1.getMessage());
                }
                messages_filtered.add(user1.getFirstName() + ": " + message.getMessage());
            }
            else if(message.getFrom() == id2 && message.getTo() == id1) {
                if(message.getId_reply() != null) {
                    Message message1 = messageService.findRecord(message.getId_reply());
                    messages_filtered.add("Reply la " + message1.getMessage());
                }
                messages_filtered.add(user2.getFirstName() + ": " + message.getMessage());
            }
        }

        return messages_filtered;
    }

    /**
     * @param id este id-ul utilizatorului caruia dorim sa ii aflam prietenii
     * @return o lista de relatii de prietenie cu prietenii utilizatorului cu id-ul id
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<Friendship> getFriendshipsOf(int id) throws SQLException {
        return StreamSupport.stream(friendshipService.getRecords().spliterator(), false)
                .filter((x)-> (x.getOne()==id || x.getTwo() == id) && x.getFriendship_request() == 2).toList();
    }

    /**
     * @param id este id-ul utilizatorului caruia dorim sa ii aflam prietenii
     * @param monthNumber este luna in care s-a stabilit relatia de prietenie (numar de la 1 la 12)
     * @return o lista de relatii de prietenie cu prietenii utilizatorului cu id-ul id din luna monthNumber
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<Friendship> getFriendshipsWithMonth(int id,int monthNumber) throws SQLException {
        return StreamSupport.stream(friendshipService.getRecords().spliterator(), false)
                .filter((x)-> (x.getOne()==id || x.getTwo() == id)
                        && x.getFriendship_request() ==2
                        && (x.getStringDate().contains("-"+ monthNumber +"-")
                        || x.getStringDate().contains("-0"+monthNumber+"-"))
                ).toList();
    }

    /**
     * @param id este id-ul utilizatorului caruia dorim sa ii aflam prieteniile in asteptare
     * @return o lista de relatii de prietenie cu cererile care il includ pe utilizatorul cu id-ul id
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public Iterable<Friendship> getPendingFriendshipsOf(int id) throws SQLException {
        return StreamSupport.stream(friendshipService.getRecords().spliterator(), false)
                .filter((x)-> x.getTwo() == id && x.getFriendship_request() == 0).toList();
    }

    /**
     * @param id_from este id-ul utilizatorului care a initiat cererea de prietenie
     * @param id_to este id-ul utilizatorului caruia ii este adresata cererea de prietenie
     * @throws ValidateException arunca exceptie daca id-urile nu sunt valide
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws RepoException arunca exceptie daca id-urile nu exista la nivel de repozitoriu
     */
    public void sendFriendship(int id_from, int id_to) throws ValidateException, BusinessException, SQLException, RepoException {
        if (userService.findRecord(id_from) == null || userService.findRecord(id_to) == null || id_from == id_to)
            throw new ValidateException("Pereche id invalida\n");
        int request = 0;

        List<Friendship> friendships = (List<Friendship>) friendshipService.getRecords();
        for(Friendship friendship: friendships)
            if(friendship.getOne() == id_from && friendship.getTwo() == id_to) {
                if(friendship.getFriendship_request() == 0) {
                    throw new BusinessException("A fost deja trimisa o cerere de prietenie");
                }
                else if(friendship.getFriendship_request() == 1) {
                    friendshipService.deleteRecord(friendship.getId());
                }
                else if(friendship.getFriendship_request() == 2) {
                    throw new BusinessException("Sunteti deja prieteni");
                }
            }
            else if(friendship.getOne() == id_to && friendship.getTwo() == id_from) {
                if(friendship.getFriendship_request() == 0) {
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(id_to);
                    params.add(id_from);
                    params.add(2);
                    friendshipService.updateRecord(friendship.getId(), params);
                    return;
                }
                else if(friendship.getFriendship_request() == 1) {
                    friendshipService.deleteRecord(friendship.getId());
                }
                else if(friendship.getFriendship_request() == 2) {
                    throw new BusinessException("Sunteti deja prieteni");
                }
            }

        ArrayList<Object> params = new ArrayList<>();
        params.add(id_from);
        params.add(id_to);

        friendshipService.createRecord(params);
    }

    /**
     * accept a friendship
     * @param id friendship's id
     * @param id_user the user who accept it
     * @throws SQLException database error
     * @throws ValidateException validate friendship error
     * @throws BusinessException business error
     * @throws RepoException repo rules error
     */
    public void acceptFriendship(int id, int id_user) throws SQLException, ValidateException, BusinessException, RepoException {
        Friendship friendship = friendshipService.findRecord(id);
        if(friendship == null) {
            throw new ValidateException("Nu exista aceasta prietenie!");
        }
        if(friendship.getTwo() == id_user) {
            if(friendship.getFriendship_request() == 1) {
                throw new BusinessException("A fost deja refuzata!\n");
            }
            else if(friendship.getFriendship_request() == 2) {
                throw new BusinessException("Deja sunteti prieteni!\n");
            }
            else {
                friendship.setFriendship_request(2);
                ArrayList<Object> params = new ArrayList<>();
                params.add(friendship.getOne());
                params.add(friendship.getTwo());
                params.add(friendship.getFriendship_request());
                friendshipService.updateRecord(friendship.getId(), params);
            }
        }
        else {
            throw new ValidateException("Nu poate fi acceptata de acest utilizator!");
        }
    }

    /**
     * reject a friendship
     * @param id friendship's id
     * @param id_user the user who reject it
     * @throws SQLException database error
     * @throws ValidateException validate friendship error
     * @throws BusinessException business error
     * @throws RepoException repo rules error
     */
    public void rejectFriendship(int id, int id_user) throws SQLException, ValidateException, BusinessException, RepoException {
        Friendship friendship = friendshipService.findRecord(id);
        if(friendship == null) {
            throw new ValidateException("Nu exista aceasta prietenie!");
        }
        if(friendship.getTwo() == id_user) {
            if(friendship.getFriendship_request() == 1) {
                throw new BusinessException("A fost deja refuzata!\n");
            }
            else if(friendship.getFriendship_request() == 2) {
                throw new BusinessException("Deja sunteti prieteni!\n");
            }
            else {
                friendship.setFriendship_request(1);
                ArrayList<Object> params = new ArrayList<>();
                params.add(friendship.getOne());
                params.add(friendship.getTwo());
                params.add(1);
                friendshipService.updateRecord(friendship.getId(), params);
            }
        }
        else {
            throw new ValidateException("Nu poate fi acceptata de acest utilizator!");
        }
    }

    /**
     * @param id_from este id-ul utilizatorului care trimite mesajul
     * @param message este corpul mesajului
     * @param ids este o lista de id-uri ale utilizatorilor carora le sunt destinate mesajele
     * @throws ValidateException arunca exceptie daca mesajul nu este valid
     * @throws BusinessException arunca exceptie daca apar probleme la nivelul de servicii al aplicatiei
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     * @throws RepoException arunca exceptie daca id-urile nu exista la nivel de repozitoriu
     */
    public void sendMessageToIds(int id_from, String message, ArrayList<Integer> ids) throws ValidateException, BusinessException, SQLException, RepoException {
        if (userService.findRecord(id_from) == null)
            throw new ValidateException("Id utilizator trimitator invalid\n");

        ArrayList<Object> params = new ArrayList<>();
        params.add(id_from);
        params.add(1);
        params.add(message);
        params.add(null);
        List<Friendship> friendships = (List<Friendship>) friendshipService.getRecords();

        int id_message = messageService.createRecord(params);

        for(Integer id_to: ids) {

            boolean ok = friendships.stream().anyMatch((x) ->
                    ((x.getOne() == id_from && x.getTwo() == id_to) ||
                            (x.getOne() == id_to && x.getTwo() == id_from)) &&
                            x.getFriendship_request() == 2);
            if(!ok)
                throw new BusinessException("Nu exista prietenie cu " + id_to + "\n");

            params = new ArrayList<>();
            params.add(id_from);
            params.add(id_to);
            params.add(id_message);
            params.add(null);
            messageService.createRecord(params);
        }
    }

    /**
     * @param id este id-ul utilizatorului care a trimis cererea
     * @return returneaza cererile de prietenie trimise de utilizatorul cu id-ul id
     * @throws SQLException arunca exceptie daca apar probleme cu conexiunea bazei de date
     */
    public ArrayList<Friendship> getSentFriendships(int id) throws SQLException {
        ArrayList<Friendship> friendships = new ArrayList<>();
        List<Friendship> friendshipList = (List<Friendship>) friendshipService.getRecords();
        for(Friendship friendship: friendshipList) {
            if(friendship.getOne() == id)
                friendships.add(friendship);
        }

        return friendships;
    }
}

