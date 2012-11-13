package agentes;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Vector;

import estruturas.Base;
import estruturas.Mina;


import modelo.Utils;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.space.Object2DTorus;

public class Transportador extends Agente implements Drawable{

	int capacidade;
	private int quantidade;
	private Object2DTorus spaceBase;

	public Transportador(int id, int rangeComunicacao,Object2DTorus space, Object2DTorus spaceFloor, Object2DTorus spaceAgente, Object2DTorus spaceMinas, Image pic, boolean random){
		minaComunicar = new ArrayList<Mensagem>();
		minasFound = new ArrayList<Mina>();
		destinos = new ArrayList<Destino>();
		destinosPossiveis = new ArrayList<Destino>();
		this.x = space.getSizeX()/3;
		this.y = space.getSizeY()/2;
		this.color = Color.white;
		this.spaceFloor = spaceFloor;
		this.spaceAgente=spaceAgente;
		this.spaceMinas = spaceMinas;
		this.spaceBase = space;
		this.range = 5;
		this.rangeComunicacao = rangeComunicacao;
		this.picture = pic;
		this.setId("t_" + id);
		if(random)
			this.setCapacidade(80 + (int) (Math.random()*119 + 0.5));
		else
			this.setCapacidade(100);
		this.setQuantidade(0);
		initMatrix();
		newDest();

	}

	public void move(){
		if(destinoAtual.getX() != x || destinoAtual.getY() != y){
			doMove();
		}
		else if (!isRetornar()){
			if(!destinoAtual.isRandom()){
				if(destinoAtual.getX()==spaceFloor.getSizeX()/3 && destinoAtual.getY() == spaceFloor.getSizeY()/2){
					Utils.print(this.getId() + ": Cheguei à base, vou deixar todo o meu material aqui.");
					Utils.print(this.getId() + ": Antes = "+this.quantidade);
					depositarMinerio();
					Utils.print(this.getId() + ": Depois = "+this.quantidade);
				}
				else{
					Utils.print(this.getId() + ": Cheguei a "+x+","+y+" e fui enviado por "+destinoAtual.getCreator());
					Mina m = (Mina) spaceMinas.getObjectAt(x, y);
					if(m!=null){
						addQuantidade(m);

						if(m.getProduzido()>0){
							destinos.add(destinos.get(0));
						}
					}
				}
			}
			destinos.remove(0);
			if(!destinos.isEmpty()){
				newDestQueue();
			}
			else newDest();
		}
		else
			depositarMinerio();
	}

	private void depositarMinerio() {
		Base b = (Base) spaceBase.getObjectAt(destinoAtual.getX(), destinoAtual.getY());
		if(b!=null){
			b.armazenar(this.quantidade);
			this.quantidade=0;
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
				Utils.print("Encontrei a mina: "+m.getX()+","+m.getY());
			}
		}

		if(minasFound.isEmpty())
			return null;
		else
			return minasFound;

	}

	public void addDestino(Mensagem msg){
		setNewDest(msg.getId_sender(),msg.getMina().getX(),msg.getMina().getY(),msg.getMina().getProduzido());
	}

	private int getTempoResposta(Mensagem msg){

		int tempo = 0;
		int x_pos = this.x;
		int y_pos = this.y;
		int quant = this.quantidade;
		for(Destino d : destinos){
			if(!d.isRandom()){
				if(d.getX()==spaceFloor.getSizeX()/3 && d.getY() == this.spaceFloor.getSizeY()/2)
					quant=0;
				else{
					quant+=d.getQuant();
					if(msg.getMina().getProduzido()+quant > this.getCapacidade()){
						tempo+=getDist(x_pos,y_pos,spaceFloor.getSizeX()/3,this.spaceFloor.getSizeY()/2);
						x_pos= spaceFloor.getSizeX()/3;
						y_pos= spaceFloor.getSizeY()/2;
						quant=0;
					}
					
				}
				tempo+=getDist(x_pos,y_pos,d.getX(),d.getY());
				x_pos = d.getX();
				y_pos = d.getY();
			}
		}

		if(msg.getMina().getProduzido()+quant > this.getCapacidade()){
			tempo+=getDist(x_pos,y_pos,spaceFloor.getSizeX()/3,this.spaceFloor.getSizeY()/2);
			x_pos= spaceFloor.getSizeX()/3;
			y_pos= spaceFloor.getSizeY()/2;
		}
		tempo+= getDist(x_pos,y_pos,msg.getMina().getX(),msg.getMina().getY());
		return tempo;
	}

	public Resposta getResposta(Mensagem m){
		return new Resposta(getTempoResposta(m),this.getId(),m.getId_msg());
	}

	private int getCapacidade() {
		return capacidade;
	}

	private void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public boolean addQuantidade(Mina m) {
		if(m.getProduzido()+this.quantidade <= capacidade){
			this.quantidade+= m.getProduzido();
			m.setProduzido(0);
		}
		else{
			m.setProduzido(m.getProduzido()-(this.capacidade-this.quantidade));
			this.quantidade=this.capacidade;
		}
		return true;
	}




}
