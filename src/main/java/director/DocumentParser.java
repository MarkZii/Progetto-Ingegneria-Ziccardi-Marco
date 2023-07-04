package director;

import builder.GrigliaBuilderIF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class DocumentParser {

	private final String urlstring;

	private BufferedReader br;

	private StringTokenizer st;
	private int lineNr = 0;

	private final GrigliaBuilderIF builder;

	private String token;

	public DocumentParser(GrigliaBuilderIF builder, String urlstring) {
		this.builder = builder;
		this.urlstring = urlstring;
	}

	private String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			String line = null;
			try {
				line = br.readLine();
				lineNr++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (line == null)
				return null;
			st = new StringTokenizer(line);
		}
		return st.nextToken();
	}

	public void build() throws TextParseException {
		//URL url;
		try {
			//url =  new URI(urlstring).toURL();
			br = new BufferedReader(new FileReader(urlstring));
		} catch (IOException e) {
			e.printStackTrace();
			throw new TextParseException("Errore di lettura");
		} /*catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}*/
		readDocument();
	}

	private void readDocument() throws TextParseException {
		token = nextToken();
		if (token == null)
			throw new TextParseException("Atteso token");
		if (!token.equals("<griglia>"))
			throw new TextParseException("line " + lineNr + " Atteso: <griglia>> trovato:" + token);
		token = nextToken();
		if (!token.equals("<dimensione>"))
			throw new TextParseException("line " + lineNr + " Atteso: <griglia>> trovato:" + token);
		String testo = readText();
		builder.createGriglia(testo);
		if (token == null)
			throw new TextParseException("Atteso token");
		if(!token.equals("</dimensione>"))
			throw new TextParseException("Atteso token: </dimensione>");
		token = nextToken();
		while (!token.equals("</griglia>")) {
			if (!token.equals("<gruppo>"))
				throw new TextParseException("line " + lineNr
						+ " Atteso: <gruppo> trovato:" + token);
			readGruppo();
			token = nextToken();
			if (token == null)
				throw new TextParseException("Atteso token");
		}
	}

	private void readGruppo() throws TextParseException {
		String tit = readText();
		String punto = null;
		builder.impostaGruppo();
		builder.impostaVincoli(tit);
		if (token == null)
			throw new TextParseException("Atteso token");
		while (!token.equals("</gruppo>")) {
			if (token.equals("<punto>")){
				punto = readPunto();
				builder.impostaPunto(punto);
			}
			token = nextToken();
			if (token == null)
				throw new TextParseException("Atteso token");
		}
	}

	public String readText() throws TextParseException {
		String par = "";
		token = nextToken();
		if (token == null)
			throw new TextParseException("line " + lineNr + " Atteso token");
		while (token.charAt(0) != '<') {
			par+=token+" ";
			token = nextToken();
			if (token == null)
				throw new TextParseException("line " + lineNr + " Atteso token");
		}
		return par;
	}

	private String readPunto() throws TextParseException {
		StringBuilder par = new StringBuilder();
		token = nextToken();
		if (token == null)
			throw new TextParseException("line " + lineNr + " Atteso token");
		if(!token.equals("<riga>"))
			throw new TextParseException("line " + lineNr + " Atteso: <riga> trovato:" + token);

		token = nextToken();
		par.append(" ").append(token);
		token = nextToken();
		if (token == null)
			throw new TextParseException("line " + lineNr + " Atteso token");
		if(!token.equals("</riga>"))
			throw new TextParseException("line " + lineNr + " Atteso: </riga> trovato:" + token);
		token = nextToken();
		if (token == null)
			throw new TextParseException("line " + lineNr + " Atteso token");
		if(!token.equals("<colonna>"))
			throw new TextParseException("line " + lineNr + " Atteso: </riga> trovato:" + token);
		token = nextToken();
		par.append(" ").append(token);
		token = nextToken();
		if (token == null)
			throw new TextParseException("line " + lineNr + " Atteso token");
		if(!token.equals("</colonna>"))
			throw new TextParseException("line " + lineNr + " Atteso: </colonna> trovato:" + token);
		return par.toString();
	}
}