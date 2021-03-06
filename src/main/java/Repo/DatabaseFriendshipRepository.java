package Repo;

import Domain.Friendship;
import Exceptions.RepoException;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseFriendshipRepository implements Repository<Integer, Friendship> {

    private final String url;
    private final String username;
    private final String password;

    /**
     * constructor
     * @param url the url of the database
     * @param username the username of the database
     * @param password the password of the database
     */
    public DatabaseFriendshipRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * add a friendship
     * @param friendship the friendship we want to add
     * @throws SQLException database error
     * @return
     */
    @Override
    public Integer add(Friendship friendship) throws RepoException, SQLException {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO friendships (id_user_1, id_user_2, friendship_made, friendship_request) VALUES ("
                            + friendship.getOne() + "," +
                            friendship.getTwo() + ",'" +
                            friendship.getStringDate() + "'," +
                            friendship.getFriendship_request() + ") RETURNING id");

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
    }

    /**
     * delete a friendship by id
     * @param id the friendship with that specific id
     * @return the friendship deleted
     * @throws RepoException if the friendship with that id doesnt exist
     * @throws SQLException database error
     */
    @Override
    public Friendship delete(Integer id) throws RepoException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM friendships WHERE id =" + id + " RETURNING *");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            throw new RepoException("Element inexistent\n");
        return new Friendship(resultSet.getInt("id"),
                resultSet.getInt("id_user_1"),
                resultSet.getInt("id_user_2"),
                resultSet.getDate("friendship_made"),
                resultSet.getInt("friendship_request"));
    }

    /**
     * @param id the friendship with that specific id
     * @param friendship the new friendship
     * @throws RepoException if the friendship with that id doesnt exist
     * @throws SQLException database error
     */
    @Override
    public void update(Integer id, Friendship friendship) throws RepoException, SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE friendships " +
                "SET id_user_1 = " + friendship.getOne() +
                ",id_user_2 = " + friendship.getTwo() +
                ",friendship_request = " + friendship.getFriendship_request() +
                " WHERE id=" + id);
        if (preparedStatement.executeUpdate() == 0)
            throw new RepoException("Element inexistent\n");
    }


    /**
     * @param id the friendship with that specific id
     * @return the friendship with that id or null if it doesnt exist
     * @throws SQLException database error
     */
    @Override
    public Friendship find(Integer id) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM friendships WHERE id = " + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            return null;
        return new Friendship(resultSet.getInt("id"),
                resultSet.getInt("id_user_1"),
                resultSet.getInt("id_user_2"),
                resultSet.getDate("friendship_made"),
                resultSet.getInt("friendship_request"));
    }


    /**
     * @return all friendships
     * @throws SQLException database error
     */
    @Override
    public Iterable<Friendship> getAll() throws SQLException {
        ArrayList<Friendship> friendshipArrayList = new ArrayList<>();
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friendships");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            friendshipArrayList.add(new Friendship(resultSet.getInt("id"),
                    resultSet.getInt("id_user_1"),
                    resultSet.getInt("id_user_2"),
                    resultSet.getDate("friendship_made"),
                    resultSet.getInt("friendship_request")
            ));
        }
        return friendshipArrayList;

    }

    /**
     * @return the number of friendships in total
     * @throws SQLException database error
     */
    @Override
    public int size() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) as COUNT FROM friendships");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT");

    }
}

