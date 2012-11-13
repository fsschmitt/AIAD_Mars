package agentes;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Vector;

import estruturas.Mina;


import modelo.Utils;
import agentes.Agente;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.space.Object2DTorus;


public class Produtor extends Agente implements Drawable {

	private boolean produzir = false;
	private int velocidade;
	public ArrayList<Mensagem> minaProduzido;

	//ArrayList<Trabalho> trabalhos;

	public Produtor(int id, int rangeComunicacao,Object2DTorus spaceFloor, Object2DTorus space, Object2DTorus spaceMinas, Image pic, boolean random){
		minasFound = new ArrayList<Mina>();
		minaComunicar = new ArrayList<Mensagem>();
		minaProduzido = new ArrayList<Mensagem>();
		destinos = new ArrayList<Destino>();
		destinosPossiveis = new ArrayList<Destino>();
		this.x = space.getSizeX()/3;
		this.y = space.getSizeY()/2;
		this.color = Color.orange;
		this.spaceFloor = spaceFloor;
		this.spaceAgente=space;
		this.spaceMinas = spaceMinas;
		this.range = 5;
		if(random)
			this.velocidade = 1 + (int) (Math.random()*4 + 0.5);
		else
			this.velocidade = 5;
		this.rangeComunicacao = rangeComunicacao;
		this.setId("p_" + id);
		this.picture = pic;
		initMatrix();
		newDest();

	}

	void setProduzir(boolean produzir) {
		this.produzir = produzir;
	}

	boolean isProduzir() {
		return produzir;
	}

	public void move(){
		if(isProduzir()){
			if(!isRetornar()){
				Mina m = (Mina) spaceMinas.getObjectAt(x, y);
				if(m.getExistente()>=velocidade) m.Produzir(velocidade);
				else m.Produzir(m.getExistente());

				if(m.getExistente() == 0){
					setProduzir(false);
					m.setVazio();
					minaProduzido.add(new Mensagem(this.getId(),m));
					if(destinos.isEmpty())
						newDest();
					else
						newDestQueue();
				}
			}
			else
				doMove();

		}
		else
		{
			if(destinoAtual.getX() != x || destinoAtual.getY() != y){
				doMove();
			}
			else if (!isRetornar()){
				if(!destinoAtual.isRandom()){
					destinos.remove(0);
					setProduzir(true);
					Utils.print(this.getId() + ": Cheguei a "+x+","+y+" e fui enviado por: "+destinoAtual.getCreator());
				}
				else{ //destinoRandom
					destinos.remove(0);
					newDest();
				}
			}
		}
	}

	public ArrayList<Mina> checkMina(){
		minasFound.clear();
		@SuppressWarnings("unchecked")
		Vector<Mina> minasPerto = spaceMinas.getMooreNeighbors(x,y,range,range,false);
		for(Mina m : minasPerto){
			if(!m.isEncontrado()){
				m.setEncontrado();
				minasFound.add(m);
				Utils.print(this.getId() + ": Encontrei a mina: "+m.getX()+","+m.getY());
			}
		}

		if(minasFound.isEmpty())
			return null;
		else
			return minasFound;

	}

	public ArrayList<Destino> getDestinos(){
		return destinos;
	}
	
	private int getTempoResposta(Mensagem msg){
		
		int tempo = 0;
		int x_pos = this.x;
		int y_pos = this.y;
		for(Destino d : destinos){
			if(!d.isRandom()){
				tempo+=getDist(x_pos,y_pos,d.getX(),d.getY());
				x_pos = d.getX();
				y_pos = d.getY();
				tempo+=(int) d.getQuant()/velocidade;
			}
		}
		tempo+= getDist(x_pos,y_pos,msg.getMina().getX(),msg.getMina().getY());
		tempo+= (int) msg.getMina().getExistente()/velocidade;
		return tempo;
	}
	
	public Resposta getResposta(Mensagem m){
		return new Resposta(getTempoResposta(m),this.getId(),m.getId_msg());
	}

	public boolean isBusy(){
		return (isProduzir() || (!destinos.isEmpty() && !destinos.get(0).isRandom()));
	}
	
	public boolean comunicarTransportador(Mensagem msg, int tempo) {
		Transportador t = getTransportador(msg,tempo);
		if(t==null){
			return false;
		}
		else{
			enviaTrans(msg,t);
			minaProduzido.remove(0);
			return true;
		}
	}

	private void enviaTrans(Mensagem msg, Transportador t) {
		t.addDestino(msg);
		Utils.print(this.getId() + ": A enviar o Transportador "+t.getId()+" para: "+msg.getMina().getX()+","+msg.getMina().getY());
		Utils.print(this.getId() + ": Tempo de resposta: "+t.getResposta(msg).getResposta());
	}

}
