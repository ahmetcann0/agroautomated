import java.sql.*;

public class RelationalDatabaseInteractionJDBC {
	private String dbUrl = "jdbc:mysql://localhost:3306/mydatabase";
	private String username = "root";
	private String password = "mypassword";
	private Connection connection;
	private Statement statement;
    private static RelationalDatabaseInteractionJDBC singleton_jdbc = null;

	
	private RelationalDatabaseInteractionJDBC() {
		
	}
	
	public static synchronized RelationalDatabaseInteractionJDBC getInstance()
    {
        if (singleton_jdbc == null)
        	singleton_jdbc = new RelationalDatabaseInteractionJDBC();
 
        return singleton_jdbc;
    }
	
	public int connect() {
		// Connect to the database
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        try(Connection conn = DriverManager.getConnection(dbUrl, username, password); Statement stmt = conn.createStatement();){
        	System.out.println("Database Connection Established!");
        }
        catch (SQLException e) {
            e.printStackTrace();
         } 
	}
	
	public int createTable() {
		String sql = "CREATE TABLE REGISTRATION " +
                "(id INTEGER not NULL, " +
                " first VARCHAR(255), " + 
                " last VARCHAR(255), " + 
                " age INTEGER, " + 
                " PRIMARY KEY ( id ))"; 

		statement.executeUpdate(sql);
		System.out.println("Created table in given database...");   
	}

    public static void main(String[] args) {
        try {
            // Load the MySQL JDBC driver (optional)
            Class.forName("com.mysql.cj.jdbc.Driver");

            

            // Insert a new user
            Statement insertStatement = connection.createStatement();
            String insertSql = "INSERT INTO users (name, age) VALUES ('John Doe', 30)";
            int numRowsInserted = insertStatement.executeUpdate(insertSql);
            System.out.println("Number of rows inserted: " + numRowsInserted);
            insertStatement.close();

            // Retrieve all users
            Statement selectStatement = connection.createStatement();
            String selectSql = "SELECT id, name, age FROM users";
            ResultSet resultSet = selectStatement.executeQuery(selectSql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
            }
            resultSet.close();
            selectStatement.close();

            // Update a user's age
            int userIdToUpdate = 1;
            int newAge = 35;
            Statement updateStatement = connection.createStatement();
            String updateSql = "UPDATE users SET age = " + newAge + " WHERE id = " + userIdToUpdate;
            int numRowsUpdated = updateStatement.executeUpdate(updateSql);
            System.out.println("Number of rows updated: " + numRowsUpdated);
            updateStatement.close();

            // Close the connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}