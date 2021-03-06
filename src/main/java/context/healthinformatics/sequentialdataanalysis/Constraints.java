package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class Constraints.
 */
public class Constraints extends Task {
	private String columnName;
	private int indexcheck = 0;
	private int indexres = -1;
	private ArrayList<Chunk> oldList;
	/**
	 * Constructor Constraints.
	 * @param chunks
	 *            the list of chunks
	 */
	public Constraints(ArrayList<Chunk> chunks) {
		setChunks(chunks);
	}

	/**
	 * Constructor Constraints without workspace previously set.
	 */
	public Constraints() { }

	/**
	 * Constructor Constraints.
	 * 
	 * @param columnName
	 *            the columnName of the value
	 * @param chunks
	 *            the list of chunks
	 */
	public Constraints(ArrayList<Chunk> chunks, String columnName) {
		setChunks(chunks);
		this.columnName = columnName;
	}

	/**
	 * Recursive method which find all chunks with code code.
	 * 
	 * @param code
	 *            the code we're looking for
	 * @param chunk
	 *            the list of chunks
	 * @param res
	 *            the result list of chunks
	 * @return the result of chunks.
	 */
	protected ArrayList<Chunk> hasCode(String code, ArrayList<Chunk> chunk,
			ArrayList<Chunk> res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getCode().equals(code)) {
				res.add(curChunk);
			}
			if (curChunk.hasChild()) {
				hasCode(code, curChunk.getChildren(), res);
			}
		}
		return res;
	}

	/**
	 * Check if a chunk has a substring of the specified comment.
	 * 
	 * @param comment
	 *            the comment
	 * @param chunk
	 *            the list of chunks
	 * @param res
	 *            the resulting chunks
	 * @return the chunks which contains the comment
	 */
	protected ArrayList<Chunk> containsComment(String comment,
			ArrayList<Chunk> chunk, ArrayList<Chunk> res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getComment().contains(comment)) {
				res.add(curChunk);
			}
			if (curChunk.hasChild()) {
				containsComment(comment, curChunk.getChildren(), res);
			}
		}
		return res;
	}

	/**
	 * Check if a chunk has a comment which equals the specified comment.
	 * 
	 * @param comment
	 *            the comment
	 * @param chunk
	 *            the chunks
	 * @param res
	 *            the result
	 * @return the resulting Chunks which have the comment
	 */
	protected ArrayList<Chunk> equalsComment(String comment,
			ArrayList<Chunk> chunk, ArrayList<Chunk> res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getComment().equals(comment)) {
				res.add(curChunk);
			}
			if (curChunk.hasChild()) {
				equalsComment(comment, curChunk.getChildren(), res);
			}
		}
		return res;
	}

	/**
	 * Get the column name.
	 * 
	 * @return the name of the column
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * Method which returns the chunk at line.
	 * -1 because the list starts at 1 when we show it to the user.
	 * @param line in the intermediate result.
	 * @return list with one chunk.
	 */
	public ArrayList<Chunk> getChunksByLine(String line) {
		int i = Integer.parseInt(line);
		ArrayList<Chunk> list = new ArrayList<Chunk>();
		list.add(getChunks().get(i - 1));
		return list;
	}

	/**
	 * Checks current arraylist on constraint on data.
	 * @param whereClause sql clause over data.
	 * @return filtered arraylist.
	 * @throws SQLException iff sql could not be executed.
	 */
	@Override
	public ArrayList<Chunk> constraintOnData(String whereClause) 
			throws SQLException {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		ArrayList<Integer> ints = getLinesFromData(whereClause);
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			checkConstraintOnData(curChunk, ints, res);
		}
		return res;
	}

	/**
	 * Checks if chunk passes constraint.
	 * @param curChunk Chunk to be checked.
	 * @param ints list of ints of chunks that should pass.
	 * @param res arrayList to return.
	 */
	public void checkConstraintOnData(Chunk curChunk, ArrayList<Integer> ints, 
			ArrayList<Chunk> res) {
		if (indexcheck < ints.size() && curChunk.getLine() == ints.get(indexcheck)) {
			res.add(curChunk);
			indexcheck++;
			indexres++;
		}
		else {
			if (curChunk.hasChild()) {
				Chunk temp = curChunk.copy();
				checkChildsOnData(temp, temp.getChildren(), ints, res);
			}	
		}
	}

	/**
	 * Remove childs from resul arraylist if they do not pass constraint.
	 * @param curChunk Chunk that has to be added to res if it passes constraint.
	 * @param childs childs of currentChunk that are being checked.
	 * @param ints list of ints of Chunks that should pass constraint.
	 * @param res result arrayList to be returned.
	 */
	public void checkChildsOnData(Chunk curChunk, ArrayList<Chunk> childs,
			ArrayList<Integer> ints, ArrayList<Chunk> res) {
		ArrayList<Chunk> kids = new ArrayList<Chunk>();
		for (Chunk c : childs) {
			if (indexcheck < ints.size() && c.getLine() == ints.get(indexcheck)) {
				if (indexres == -1) {
					addKids(res, kids, curChunk, c);
				}
				else if (indexres < res.size() && !res.get(indexres).equals(curChunk)) {
					addKids(res, kids, curChunk, c);
				}
				else {
					kids.add(c);
					indexcheck++;
				}
			}
			else {
				if (c.hasChild()) {
					checkChildsOnData(curChunk, c.getChildren(), ints, res);
				}
			}
		}
		if (kids.size() != 0) {
			curChunk.setChunks(kids);
		}
	}
	
	/**
	 * 
	 * @param res Arraylist to return.
	 * @param kids children from curChunk.
	 * @param curChunk Parent chunk.
	 * @param kid kid to be added.
	 */
	public void addKids(ArrayList<Chunk> res, ArrayList<Chunk> kids, Chunk curChunk, Chunk kid) {
		res.add(curChunk);
		kids.add(kid);
		indexcheck++;
		indexres++;
	}
	
	@Override
	protected void setChunks(ArrayList<Chunk> c) {
		super.setChunks(c);
		oldList = c;
	}

	@Override
	public ArrayList<Chunk> undo() {
		return oldList;
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		return hasCode(code, getChunks(), new ArrayList<Chunk>());
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		return equalsComment(comment, getChunks(), new ArrayList<Chunk>());
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		return containsComment(comment, getChunks(), new ArrayList<Chunk>());
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		return getChunksByLine(line);
	}
}
