package estruturas;
import java.awt.Color;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;


public class Mina implements Drawable{
	/**Posição da mina*/
	private int x, y;
	/**Cor representativa da mina*/
	private Color color;
	/**Quantidade de minério existente na mina*/
	private int existente = 0;
	/**Quantidade de minério produzida na mina*/
	private int produzido = 0;
	/**Booleano para o estado da mina**/
	private boolean encontrado = false;
	private boolean encontradoS = false;
	private boolean visitado = false;
	private boolean vazio = false;

	/**
	 * Construtor da mina
	 * 
	 * @param quantidade quantidade de minério existente na mina
	 * @param x coordenada x
	 * @param y coordenada y
	 */
	public Mina(int quantidade, int x, int y){
		setExistente(quantidade);
		this.x = x;
		this.y = y;
		this.color = Color.red;
	}

	/**
	 * Retorna a quantidade de minério existente na mina
	 * 
	 * @return Quantidade de minério existente na mina
	 */
	public int getExistente() {
		return existente;
	}

	/**
	 * Define a quantidade de minério existente na mina
	 * 
	 * @param existente Quantidade de minério existente na mina
	 */
	private void setExistente(int existente) {
		this.existente = existente;
	}

	/**
	 * Retorna a quantidade de minério produzida na mina
	 * 
	 * @return Quantidade de minério produzida na mina
	 */
	public int getProduzido() {
		return produzido;
	}
	
	/**
	 * Define a quantidade de minério produzido na mina
	 * 
	 * @param existente Quantidade de minério produzido na mina
	 */
	public void setProduzido(int produzido) {
		this.produzido = produzido;
	}


	/**
	 * Aumenta a quantidade de minério produzida na mina
	 * 
	 * @param produzido Quantidade de minério produzida durante o intervalo
	 */
	public void Produzir(int produzido) {
		if((existente - produzido) >= 0){
			this.produzido += produzido;
			existente-=produzido;
		}
	}

	/**
	 * Retorna a coordenada x da base
	 * 
	 * @return coordenada x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retorna a coordenada y da base
	 * 
	 * @return coordenada y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Retorna a cor da base
	 * 
	 * @return cor
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Define a mina como encontrada
	 */
	public void setEncontrado(){
		encontrado=true;
		this.color = Color.pink;
	}

	/**
	 * Retorna o estado da mina
	 * @return encontrado
	 */
	public boolean isEncontrado(){
		return encontrado;
	}

	/**
	 * Desenha a mina
	 * 
	 * @param g SimGraphics onde vai ser desenhada a mina
	 */
	public void draw(SimGraphics g) {
		if(!visitado)
			g.drawOval(color);
		else{
			if(existente>0)
				g.drawStringInOval(color, Color.black, ""+existente);
			else if (produzido>0)
				g.drawStringInOval(color, Color.black, ""+produzido);
		}
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado() {
		this.visitado = true;
		this.color = Color.gray;
	}

	public void setVazio() {
		this.vazio = true;
		this.color = Color.yellow;
	}

	public boolean isVazio() {
		return vazio;
	}

	public boolean isEncontradoS() {
		return encontradoS;
	}

	public void setEncontradoS() {
		this.encontradoS = true;
		this.encontrado = true;
		this.color = Color.pink;
	}
}