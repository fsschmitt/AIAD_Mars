package estruturas;

import java.awt.Color;
import java.awt.Image;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;


public class Base implements Drawable{
	/**Posição da base*/
	private int x, y;
	/**Imagem representativa da base*/
	private Image picture;
	/**Quantidade de minério armazenado na base*/
	private int existente = 0; 


	/**
	 * 
	 * Construtor da base
	 * 
	 * @param x coordenada x
	 * @param y coordenada y
	 */
	public Base(int x, int y, Image pic){
		this.x = x;
		this.y = y;
		this.picture = pic;
	}

	/**
	 * Retorna a quantidade de minério armazenado na base
	 * 
	 * @return Quantidade de minério armazenado na base
	 */
	public int getExistente() {
		return existente;
	}

	/**
	 * Adiciona uma determinada quantidade de minério ao depósito da base
	 * 
	 * @param quant quantidade de minério a adicionar
	 */
	public void armazenar(int quant) {
		existente += quant;
	}

	/**
	 * Retorna a coordenada x da base
	 * 
	 * @return coordenada x
	 */
	public int getX() {
		return x;
	}

	public void draw(SimGraphics g) {
		g.drawImage(picture);
		g.drawString(""+existente,Color.white);
	}

	/**
	 * Retorna a coordenada y da base
	 * 
	 * @return coordenada y
	 */
	public int getY() {
		return y;
	}
}