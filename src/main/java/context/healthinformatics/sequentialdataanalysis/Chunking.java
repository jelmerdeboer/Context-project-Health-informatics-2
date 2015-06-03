package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.SingletonInterpreter;

/**
 * Class for chunking a list of chunks.
 *
 */
public class Chunking extends Task {

	private Chunk temp;

	/**
	 * Constructor for chunking.
	 */
	public Chunking() {
		
	}
	
	/**
	 * Constructor Chunking.
	 * 
	 * @param chunks
	 *            the list of chunks
	 */
	public Chunking(ArrayList<Chunk> chunks) {
		setChunks(chunks);
		chunks = getChunks();
	}

	/**
	 * Create chunks on constraint data.
	 * @param whereClause for sql query.
	 * @return new Arraylist with chunked chunks.
	 * @throws SQLException if whereclause was not correct.
	 */
	public ArrayList<Chunk> constraintOnData(String whereClause) throws SQLException {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp = new Chunk();
		ArrayList<Integer> ints = getLinesFromData(whereClause);
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsData(ints, curChunk, res);
		}
		addLastElementToChunks(res);
		return res;
	}

	/**
	 * Add chunks with data to new chunks if constraint on data passes.
	 * @param ints the arraylist with integers with correct data.
	 * @param curChunk Chunk that currently is being checked.
	 * @param res arraylist result.
	 */
	public void addChunkOnEqualsData(ArrayList<Integer> ints, Chunk curChunk,
			ArrayList<Chunk> res) {
		if (ints.contains(curChunk.getLine())) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			this.temp = new Chunk();
		}
	}

	/**
	 * Chunking chunks on a code constraint.
	 * @param code Code constraint for a chunk.
	 * @return New ArrayList<Chunk> chunked on code.
	 */
	public ArrayList<Chunk> constraintOnCode(String code) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsCode(curChunk, code, res);
		}
		addLastElementToChunks(res);
		return res;
	}

	/**
	 * Add chunk to new Chunk in new ArrayList if it equals code.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for code constraint.
	 * @param res ArrayList<Chunk> chunked.
	 */
	private void addChunkOnEqualsCode(Chunk curChunk, String c, ArrayList<Chunk> res) {
		if (curChunk.getCode().equals(c)) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			this.temp  = new Chunk();
		}
	}

	/**
	 * Chunking chunks on a comment contains constraint.
	 * @param comment String for comment.
	 * @return new ArrayList<Chunk> chunked on constraint.
	 */
	public ArrayList<Chunk> constraintOnContainsComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnContainsComment(curChunk, comment, res);
		}
		addLastElementToChunks(res);
		return res;
	}
	
	/**
	 * Add chunk to new Chunk in new ArrayList if it contains comment.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for comment constraint.
	 * @param res ArrayList<Chunk> chunked.
	 */
	private void addChunkOnContainsComment(Chunk curChunk, String c, 
			ArrayList<Chunk> res) {
		if (curChunk.getComment().contains(c)) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			temp  = new Chunk();
		}
	}

	/**
	 * Chunking chunks on a comment equals constraint.
	 * @param comment String for comment.
	 * @return new ArrayList<Chunk> chunked on constraint.
	 */
	public ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsComment(curChunk, comment, res);
		}
		addLastElementToChunks(res);
		return res;
	}

	/**
	 * Add chunk to new Chunk in new ArrayList if it equals comment.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for comment constraint.
	 * @param res ArrayList<Chunk> chunked.
	 */
	private void addChunkOnEqualsComment(Chunk curChunk, String c, 
			ArrayList<Chunk> res) {
		if (curChunk.getComment().equals(c)) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			temp  = new Chunk();
		}
	}

	/**
	 * Adds a Chunk to a new chunk if it is part of the Chunk.
	 * @param temp The new Chunk.
	 * @param res The new ArrayList<Chunk> to be returned after chunking.
	 */
	private void addChunkToRes(ArrayList<Chunk> res, Chunk curChunk) {
		if (temp.hasChild()) {
			res.add(temp);
			res.add(curChunk);
		}
		else {
			res.add(curChunk);
		}
	}

	/**
	 * Adds last chunked chunk to result ArrayList.
	 * @param res ArrayList to be returned.
	 */
	private void addLastElementToChunks(ArrayList<Chunk> res) {
		if (temp.hasChild()) {
			res.add(temp);
		}
	}

	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * method that is called to calculate the new changes.
	 * This method is overwritten at runtime to have the correct methods in it.
	 * @throws Exception query input can be wrong.
	 */
	@Override
	public void run(String[] query) throws Exception {
		chunks = SingletonInterpreter.getInterpreter().getChunks();
		if (isData(query)) {
			runData(query);
		}
		else if (isCode(query)) {
			runCode(query);
		}
		else if (isComment(query)) {
			runComment(query);
		}
		else {
			throw new Exception("query input is wrong at: " + query[getQueryPart()]);
		}
	}
	
	/**
	 * Executes constraintOnData with query.
	 * @param query interpreter query.
	 * @throws SQLException iff sql query goes wrong.
	 */
	private void runData(String[] query) throws SQLException {
		StringBuilder q = new StringBuilder();
		increment(2);
		for (int i = getQueryPart(); i < query.length; i++) {
			q.append(query[i]);
			q.append(" ");
		}
		setResult(constraintOnData(q.toString()));
	}
	
	/**
	 * Executes constraintOnCode with query.
	 * @param query interpreter query.
	 */
	private void runCode(String[] query) {
		increment(2);
		if (isEquals(query[getQueryPart()])) {
			inc();
			setResult(constraintOnCode(query[getQueryPart()]));
		}
	}
	
	/**
	 * Executes constraint on contains/equals comment.
	 * @param query interpreter query.
	 */
	private void runComment(String[] query) {
		increment(2);
		if (isEquals(query[getQueryPart()])) {
			inc();
			constraintOnEqualsComment(query[getQueryPart()]);
		}
		if (isContains(query[getQueryPart()])) {
			inc();
			constraintOnContainsComment(query[getQueryPart()]);
		}
	}

}
