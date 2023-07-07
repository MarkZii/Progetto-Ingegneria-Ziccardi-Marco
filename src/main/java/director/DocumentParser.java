package director;

import builder.GrigliaBuilderIF;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DocumentParser {
	private final String string;
	private Scanner scanner;
	private StringTokenizer st;
	private final GrigliaBuilderIF builder;
	private String token;

	public DocumentParser(GrigliaBuilderIF builder, String string) {
		this.builder = builder;
		this.string = string;
	}

	private String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			String line = scanner.nextLine();
			if (line == null)
				return null;
			st = new StringTokenizer(line);
		}
		return st.nextToken();
	}

	public void build() throws Exception {
		File file = new File(string);
		scanner = new Scanner(file);
		readDocument();
	}

	private void readDocument() throws Exception {
		System.out.println("read document");
		token = nextToken();
		if (token == null)
			throw new Exception();
		if (!token.equals("<griglia>"))
			throw new Exception();
		token = nextToken();
		if (!token.equals("<dimensione>"))
			throw new Exception();
		String testo = readText();
		builder.createGriglia(testo);
		if (token == null)
			throw new Exception();
		if(!token.equals("</dimensione>"))
			throw new Exception();
		token = nextToken();
		while (!token.equals("</griglia>")) {
			if (!token.equals("<gruppo>"))
				throw new Exception();
			readGruppo();
			token = nextToken();
			if (token == null)
				throw new Exception();
		}
	}

	private void readGruppo() throws Exception {
		String tit = readText();
		String punto = null;
		builder.impostaGruppo();
		builder.impostaVincoli(tit);
		if (token == null)
			throw new Exception();
		while (!token.equals("</gruppo>")) {
			if (token.equals("<punto>")){
				punto = readPunto();
				builder.impostaPunto(punto);
			}
			token = nextToken();
			if (token == null)
				throw new Exception();
		}
	}

	public String readText() throws Exception {
		String par = "";
		token = nextToken();
		if (token == null)
			throw new Exception();
		while (token.charAt(0) != '<') {
			par+=token+" ";
			token = nextToken();
			if (token == null)
				throw new Exception();
		}
		return par;
	}

	private String readPunto() throws Exception {
		StringBuilder par = new StringBuilder();
		token = nextToken();
		if (token == null)
			throw new Exception();
		if(!token.equals("<riga>"))
			throw new Exception();

		token = nextToken();
		par.append(" ").append(token);
		token = nextToken();
		if (token == null)
			throw new Exception();
		if(!token.equals("</riga>"))
			throw new Exception();
		token = nextToken();
		if (token == null)
			throw new Exception();
		if(!token.equals("<colonna>"))
			throw new Exception();
		token = nextToken();
		par.append(" ").append(token);
		token = nextToken();
		if (token == null)
			throw new Exception();
		if(!token.equals("</colonna>"))
			throw new Exception();
		return par.toString();
	}
}