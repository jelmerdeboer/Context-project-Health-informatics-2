package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The Class Codes.
 */
public class Codes extends Task {

	private ArrayList<Chunk> chunks;
	
	/**
	 * Constructor for codes without an argument.
	 */
	public Codes() { }

	/**
	 * Constructor for codes.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	public Codes(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Set the code on line line with the string code.
	 * 
	 * @param line
	 *            the line of the code
	 * @param code
	 *            the code to be set
	 * @throws Exception
	 *             thrown if you couldn't set the code.
	 */
	public void setCodeOfLine(int line, String code) throws Exception {
		try {
			getChunkByLine(line, chunks).setCode(code);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Set the code on the chunk at index in the chunks ArrayList with the
	 * string code.
	 * 
	 * @param indexInChunks
	 *            the index of the chunk in the ArrayList chunks
	 * @param code
	 *            the code to be set
	 * @throws Exception
	 */
	public void setCodeOfChunks(int indexInChunks, String code) {
		try {
			Chunk c = chunks.get(indexInChunks);
			c.setCode(code);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Method which sets the code for every chunk in a list.
	 * @param list of chunks.
	 * @param code code to be set.
	 */
	public void setCodeOfListOfChunks(ArrayList<Chunk> list, String code) {
		for (Chunk c : list) {
			c.setCode(code);
		}
	}
	
	/**
	 * Method which sets the code of every chunk with the comment : comment, to code.
	 * @param code c
	 * @param comment c
	 */
	public void setCodeOnComment(String code, String comment) {
		try {
			for (Chunk c : chunks) {
				if (c.getComment().equals(comment)) {
					c.setCode(code);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Method which replaces the code of every chunk with the code : previousCode, to code.
	 * @param code new code.
	 * @param previousCode old code.
	 */
	public void setCodeOnCode(String code, String previousCode) {
			for (Chunk c : chunks) {
				if (c.getCode().equals(previousCode)) {
					c.setCode(code);
				}
			}
	}
	
	/**
	 * Method which sets the code for all.
	 * @param code to be set.
	 * @param whereClause to be met.
	 * @throws Exception e
	 */
	public void setCodeOnData(String code, String whereClause) throws Exception {
		ArrayList<Integer> list = getLinesFromData(whereClause);
		for (Integer i : list) {
			setCodeOfLine(i, code);
		}
	}

	/**
	 * Retrieves the Chunk out of the Arraylist with a specific line.
	 * 
	 * @param line
	 *            the line that is needed.
	 * @param chunk the list which will be searched.
	 * @return the Chunk with the corresponding line.
	 * @throws Exception e
	 *             thrown if there is no Chunk with this line.
	 */
	public Chunk getChunkByLine(int line, ArrayList<Chunk> chunk) throws Exception {
		Chunk curChunk = null;
		for (int i = 0; i < chunk.size(); i++) {
			curChunk = chunk.get(i);
			if (curChunk.getLine() == line) {
				return curChunk;
			}
			else if (curChunk.hasChild()) {
				return getChunkByLine(line, curChunk.getChunks());
			}
		}
		throw new Exception();
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}
}