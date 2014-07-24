import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AddressDb {
    public static final String JDBC_DRIVER = "oracle.jdbc.ThinDriver";
    public static final String DB_CONNECTION_URI = "jdbc:oracle:thin:@prod";
    public static final String DB_CONNECTION_USERNAME = "admin";
    public static final String DB_CONNECTION_PASSWORD = "beefhead";
    public static final String NAME_COLUMN = "name";
    public static final String PHONE_NUMBER_COLUMN = "phoneNumber";
    public static final String ADD_PERSON_QUERY = "insert into AddressEntry values (?, ?, ?)";
    public static final String FIND_PERSON_QUERY = "select * from AddressEntry where name = ?";
    public static final String GET_ALL_PEOPLE_QUERY = "select * from AddressEntry";

    private static AddressDb instance;

    /**
     * @return An instance of the AddressDB DAO
     */
    public static AddressDb getInstance() {
        if (instance == null) {
            synchronized (AddressDb.class) {
                if (instance == null) {
                    instance = new AddressDb();
                }
            }
        }
        return instance;
    }

    private AddressDb() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Store user to the database.
     *
     * @param person have to be not null
     * @throws RuntimeException
     */
    public void addPerson(Person person) {
        if (person == null) return;
        try (
                Connection connection = getDBConnection();
                PreparedStatement statement = connection.prepareStatement(ADD_PERSON_QUERY)
        ) {
            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, person.getName());
            statement.setString(3, person.getPhoneNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks up the given person name, null if not found.
     *
     * @param name name of the person that we are looking for
     * @return Person corresponding to the provided name or null if none
     */
    public Person findPerson(String name) {
        if (name == null) return null;
        try (
                Connection connection = getDBConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_PERSON_QUERY)
        ) {
            ResultSet personResultSet = statement.executeQuery();
            return personResultSet.next() ? getPersonFromResultSet(personResultSet) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks up for all people stored in database
     *
     * @return All people stored in database
     */
    public List<Person> getAll() {
        try (
                Connection connection = getDBConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_PEOPLE_QUERY)
        ) {
            List<Person> entries = new LinkedList<>();
            ResultSet personResultSet = statement.executeQuery();
            while (personResultSet.next()) {
                entries.add(getPersonFromResultSet(personResultSet));
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION_URI, DB_CONNECTION_USERNAME, DB_CONNECTION_PASSWORD);
    }

    private Person getPersonFromResultSet(ResultSet personResultSet) throws SQLException {
        String name = personResultSet.getString(NAME_COLUMN);
        String phoneNumber = personResultSet.getString(PHONE_NUMBER_COLUMN);
        return new Person(name, phoneNumber);
    }


}
