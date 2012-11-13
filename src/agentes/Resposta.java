package agentes;

public class Resposta {
	
	private int resposta;
	private String prod;
	private int id_msg;
	
	public Resposta(int resposta, String prod, int id_msg){
		this.setResposta(resposta);
		this.setProd(prod);
		this.setId_msg(id_msg);
	}

	public int getResposta() {
		return resposta;
	}

	public void setResposta(int resposta) {
		this.resposta = resposta;
	}

	public String getProd() {
		return prod;
	}

	public void setProd(String prod) {
		this.prod = prod;
	}

	public int getId_msg() {
		return id_msg;
	}

	public void setId_msg(int id_msg) {
		this.id_msg = id_msg;
	}

}
