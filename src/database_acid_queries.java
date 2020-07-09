import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class database_acid_queries {

	public static void create_db() throws SQLException {
		Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "");
		Statement statement = c.createStatement();
		statement.executeUpdate("CREATE DATABASE final_proj_table");
	}
	
	public static void create_tables() throws SQLException{
		
		Connection c = null;
		Statement stmt = null;
		
		try {
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final_proj_table", "postgres", "");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			
			String sql = "CREATE TABLE product(" +
			"prod_id VARCHAR(10), " +
			"pname VARCHAR(20), " +
			"price REAL)";
			
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());

		}
		
		try {
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final_proj_table", "postgres", "");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			
			String sql = "CREATE TABLE depot (" +
			"dep_id VARCHAR(10), " +
			"addr VARCHAR(30), " +
			"volume INT)";
			
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());

		}
		
		try {
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final_proj_table", "postgres", "");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			
			String sql = "CREATE TABLE stock( " +
			"prod_id VARCHAR(10), " +
			"dep_id VARCHAR(30), " +
			"quantity INT)";
			
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());

		}
		
		
	}

	public static void add_constraints() throws SQLException{
		Connection c = null;
		Statement stmt = null;
		String sql = null;
		try {
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final_proj_table", "postgres", "");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			// Alter table product
			
			sql = "ALTER TABLE product ADD CONSTRAINT prod_pk PRIMARY KEY (prod_id)";
			stmt.executeUpdate(sql);
			
			sql = "ALTER TABLE product ADD CONSTRAINT price_non_neg CHECK (price >= 0)";
			stmt.executeUpdate(sql);
			
			// Alter table depot
			sql = "ALTER TABLE depot ADD CONSTRAINT depot_pk PRIMARY KEY (dep_id)";
			stmt.executeUpdate(sql);
			
			sql = "ALTER TABLE depot ADD CONSTRAINT volume_non_neg CHECK (volume >= 0)";
			stmt.executeUpdate(sql);
			
			// ALter table Stock
			sql = "ALTER TABLE stock ADD CONSTRAINT stock_prod_key FOREIGN KEY (prod_id) REFERENCES product(prod_id) ON DELETE CASCADE";
			stmt.executeUpdate(sql);
			sql = "ALTER TABLE stock ADD CONSTRAINT stock_dep_key FOREIGN KEY (dep_id) REFERENCES depot(dep_id) ON DELETE CASCADE";
			stmt.executeUpdate(sql);
			
			sql = "ALTER TABLE stock ADD CONSTRAINT stock_pk PRIMARY KEY (prod_id, dep_id)";
			stmt.executeUpdate(sql);
			
			System.out.println("Finished updates");
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
	}
	
	public static void acid_transaction(String sql) throws SQLException, IOException, ClassNotFoundException {
		Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final_proj_table", "postgres", "");
		
		// Atomocity
		c.setAutoCommit(false);
		
		// Isolation
		c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
		Statement statement = null;
		
		
		try {
			statement = c.createStatement();
			
			System.out.println("Launching statment: "+ sql);
			statement.executeUpdate(sql);
			
			

		}
		catch (SQLException e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Rolling back any changes");
			
			c.rollback();
			statement.close();
			c.close();
			return;			
		}
		c.commit();
		statement.close();
		c.close();
		System.out.println("ACID transaction completed");
		return;
	}
	
	public static void populate_tables() throws SQLException{
		
		// Non-Acid population of tables
		
		Connection c = null;
		Statement stmt = null;
		String sql = null;
		
		
		
		
		try {
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final_proj_table", "postgres", "");
			System.out.println("Opened database successfully");
			stmt = c.createStatement();

			// Create product p1, depot d1, stock
			sql = "INSERT INTO product (prod_id, pname, price) VALUES ('p1', 'Widget', 19.99)";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO depot (dep_id, addr, volume) VALUES ('d1', '111 Mulberry Ln', 3000)";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO stock (prod_id, dep_id, quantity) VALUES ('p1', 'd1', 250)";
			stmt.executeUpdate(sql);

			System.out.println("Data inserted successfully");
			
		
		}catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException{
		boolean init = false;
		
		if (init) {
			// Create a database, tables, and populate
			create_db();
			create_tables();
			add_constraints();
			populate_tables();

		}
		
		// SQL to execute
		String sql = "DELETE FROM product WHERE prod_id = 'p1'";
		acid_transaction(sql);
		
	}
	
	
	
}
