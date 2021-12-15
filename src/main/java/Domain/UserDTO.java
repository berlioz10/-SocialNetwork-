package Domain;

public class UserDTO {
    private final int id;
    private final String firstName;
    private final String surname;

    public UserDTO(int id, String firstName, String surname) {
        this.id=id;
        this.firstName=firstName;
        this.surname=surname;
    }

    public int getId(){
        return id;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getSurname(){
        return this.surname;
    }

    @Override
    public String toString() {
        return  id +" / "+ firstName + " " + surname ;
    }
}
