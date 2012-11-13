package agentes;

public class Destino {

	private String creator;
	private int x,y;
	private boolean random;
	private int quant;

	Destino(String creator,int x,int y, boolean random){

		this.setCreator(creator);
		this.setX(x);
		this.setY(y);
		this.random = random;

	}

	Destino(String creator,int x,int y, boolean random, int quant){

		this.setCreator(creator);
		this.setX(x);
		this.setY(y);
		this.random = random;
		this.quant = quant;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isRandom(){
		return this.random;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


}
