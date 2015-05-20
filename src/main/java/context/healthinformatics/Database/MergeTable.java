package context.healthinformatics.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import context.healthinformatics.Parser.Column;

/**
 * 
 *
 */
public class MergeTable {
	
	private Db data = SingletonDb.getDb();
	
	/**
	 * 
	 */
	public MergeTable() {
		
	}
	
	/**
	 * 
	 * @param clause merge where clause.
	 */
	public void mergeTablesView(String clause) {
		HashMap<String, ArrayList<Column>> tables = data.getTables();
		StringBuilder sql = new StringBuilder();
		String prefix = "";
		sql.append("CREATE VIEW result AS SELECT * FROM ");
		for (String key : tables.keySet()) {
			sql.append(prefix);
			prefix = ", ";
			sql.append(key);
		}
		sql.append(" WHERE ");
		sql.append(clause);
		System.out.println(sql.toString());
		data.executeUpdate(sql.toString());
	}
	
	public void mergeTables(String[] clause) throws SQLException {
		HashMap<String, ArrayList<Column>> tables = data.getTables();
		ArrayList<Column> columns = new ArrayList<Column>();
		Set<String> allTables = new TreeSet<String>();
		allTables.addAll(tables.keySet());
		
		for (String key : tables.keySet()) {
			columns.addAll(tables.get(key));
		}
		
		data.createTable("result", columns);
		
		for (String key : allTables) {
			String tableClause = "";
			for (int i = 0; i < clause.length; i++) {
				if (clause[i].contains(key)) {
					tableClause = clause[i];
				}
			}
			insertTable(key, tables.get(key), tableClause);
		}
		
	}
	
	private void insertTable(String key, ArrayList<Column> cols, String clause) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO result (");
		appendColumns(cols, sql);
		sql.append(") SELECT ");
		appendColumns(cols, sql);
		sql.append("FROM ").append(key);
		if (clause.length() > 0) {
			sql.append(" WHERE ").append(clause);
		}
		System.out.println(sql.toString());
		data.executeUpdate(sql.toString());
	}
	
	public void appendColumns(ArrayList<Column> columns, StringBuilder sql) {
		String prefix = "";
		for (int i = 0; i < columns.size(); i++) {
			sql.append(prefix);
			prefix = ", ";
			String name = columns.get(i).getColumnName();
			sql.append(name);
		}
		sql.append(" ");
	}
	
	public void appendTables(Set<String> tables, StringBuilder sql) {
		String prefix = "";
		for (String key : tables) {
			sql.append(prefix);
			prefix = ", ";
			sql.append(key);
		}
	}
	
	public void dropView(String viewName) {
		String sql = "DROP VIEW " + viewName;
		data.executeUpdate(sql);
	}
}
