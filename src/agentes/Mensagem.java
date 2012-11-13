package agentes;

import estruturas.Mina;

public class Mensagem {
	
	private String id_sender;
	public static int id = 0;
	private int id_msg;
	private Mina mina;
	
	public Mensagem(String id_sender,Mina m){
		this.setId_sender(id_sender);
		this.setMina(m);
		this.setId_msg(id++);
	}

	public int getId_msg() {
		return id_msg;
	}

	public void setId_msg(int id_msg) {
		this.id_msg = id_msg;
	}

	public Mina getMina() {
		return mina;
	}

	public void setMina(Mina mina) {
		this.mina = mina;
	}

	public String getId_sender() {
		return id_sender;
	}

	public void setId_sender(String id_sender) {
		this.id_sender = id_sender;
	}

}
