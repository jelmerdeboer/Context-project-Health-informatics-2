package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Test;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * computations test class.
 */
public class ComputationsTest {

	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/mergeTableFiles/";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();

	/**
	 * method preparing for environment for tests.
	 */
	
	private final int two = 2;
	private final int three = 3;
	private final int four = 4;
	private final int five = 5;
	
	/**
	 * preparing db for test.
	 */
	@org.junit.Before
	public void before() {
		SingletonDb.dropAll(data);
	}
	
	/**
	 * Empty the interpreter.
	 */
	@After
	public void after() {
		SingletonDb.dropAll(data);
		try {
			SingletonInterpreter.getInterpreter().interpret("undoAll");
		} catch (Exception e) {
			Log.info("undoAll failed.");
		}
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}

	/**
	 * Test for computing the difference in time.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 * @throws Exception if the interpreting goes wrong.
	 */
	@Test
	public void computeDifferenceTimeTest() throws IOException, SQLException, Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause, "time");
		SingletonInterpreter.getInterpreter().interpret("filter data where not time is null");
		SingletonInterpreter.getInterpreter().interpret("code(a) data where not time is null");
		SingletonInterpreter.getInterpreter().
		interpret("connect(connectionNote) code = a to code = a");
		SingletonInterpreter.getInterpreter().interpret("compute difference time");
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		final int firstRes = 27;
		final int seccondRes = 1740;
		final int thirdRes = 1603;
		final int fourthRes = 3066;
		assertEquals(isDif(firstRes), chunks.get(0).toArray().get(0));
		assertEquals(isDif(seccondRes), chunks.get(1).toArray().get(0));
		assertEquals(isDif(thirdRes), chunks.get(two).toArray().get(0));
		assertEquals(isDif(fourthRes), chunks.get(three).toArray().get(0));
	}
	
	
	/**
	 * Test for computing the difference in value.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 * @throws Exception if the interpreting goes wrong.
	 */
	@Test
	public void computeDifferenceValueTest() throws IOException, SQLException, Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause, "time");
		SingletonInterpreter.getInterpreter().interpret("filter data where not time is null");
		SingletonInterpreter.getInterpreter().interpret("code(a) data where not time is null");
		SingletonInterpreter.getInterpreter().
		interpret("connect(connectionNote) code = a to code = a");
		SingletonInterpreter.getInterpreter().interpret("compute difference value");
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		final int firstRes = 11;
		final int seccondRes = 44;
		final int thirdRes = 111;
		final int fourthRes = 9;
		assertEquals(isDif(firstRes), chunks.get(0).toArray().get(0));
		assertEquals(isDif(seccondRes), chunks.get(1).toArray().get(0));
		assertEquals(isDif(thirdRes), chunks.get(two).toArray().get(0));
		assertEquals(isDif(fourthRes), chunks.get(three).toArray().get(0));
	}
	
	/**
	 * Test for computations compute chunk.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 * @throws Exception if the interpreting goes wrong.
	 */
	@Test
	public void computeValueTest() throws IOException, SQLException, Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause, "time");
		SingletonInterpreter.getInterpreter().interpret("filter data where not time is null");
		SingletonInterpreter.getInterpreter().interpret("code(a) data where not time is null");
		SingletonInterpreter.getInterpreter().
		interpret("chunk date 1");
		SingletonInterpreter.getInterpreter().interpret("compute chunk value");
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		final int size = 4;
		assertEquals(size, chunks.size());
		for (Chunk c : chunks) {
			assertTrue(c.isCompute());
		}
	}
	

	/**
	 * Test for computations compute chunk.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 * @throws Exception if the interpreting goes wrong.
	 */
	@Test
	public void computeAllValueTest() throws IOException, SQLException, Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause, "time");
		SingletonInterpreter.getInterpreter().interpret("filter data where not time is null");
		SingletonInterpreter.getInterpreter().interpret("code(a) data where not time is null");
		SingletonInterpreter.getInterpreter().interpret("compute all value");
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		final int size = 1;
		assertEquals(size, chunks.size());
		Chunk c  = chunks.get(0);
		assertTrue(c.isCompute());
		ArrayList<String> strings = c.toArray();
		assertEquals(five, strings.size());
		assertEquals("sum of values = 920.0", strings.get(0));
		assertEquals("max of values = 242.0", strings.get(1));
		assertEquals("min of values = 131.0", strings.get(two));
		assertEquals("average of values = 184.0", strings.get(three));
		assertEquals("Childs sum = 5", strings.get(four));
	}
	
	private String isDif(int num) {
		String test = "difference to connection " + num + ".0";
		return test;
	}

}
