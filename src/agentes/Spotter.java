package agentes;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Vector;

import estruturas.Mina;


import modelo.Utils;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.space.Object2DTorus;


public class Spotter extends Agente implements Drawable{

	public ArrayList<Mina> minasVisitadas;
	

	public Spotter(int id, int rangeComunicacao,Object2DTorus spaceFloor, Object2DTorus spaceAgente, Object2DTorus spaceMinas, Image pic){
		minasVisitadas = new ArrayList<Mina>();
		minaComunicar = new ArrayList<Mensagem>();
		minasFound = new ArrayList<Mina>();
		destinos = new ArrayList<Destino>();
		destinosPossiveis = new ArrayList<Destino>();
		this.x = spaceFloor.getSizeX()/3;
		this.y = spaceFloor.getSizeY()/2;
		this.color = Color.green;
		this.spaceAgente = spaceAgente;
		this.spaceMinas = spaceMinas;
		this.spaceFloor = spaceFloor;
		this.range = 8;
		this.rangeComunicacao = rangeComunicacao;
		this.setId("s_" + id);
		this.picture = pic;
		initMatrix();
		newDest();

	}

	private boolean chegouDestino(int x, int y){
		return destinoAtual.getX() == x && destinoAtual.getY()== y;	
	}

	private boolean chegouMina(int x, int y){
		return chegouDestino(x,y) && !destinoAtual.isRandom();
	}

	public void move(){

		if(destinoAtual.getX() != x || destinoAtual.getY() != y){
			doMove();
		}
		else if (!isRetornar()){
			if(chegouMina(x,y)){
				destinos.remove(0);
				Mina m = (Mina) spaceMinas.getObjectAt(x, y);
				if(m!=null)	
				{
					minasVisitadas.add(m);
					if(!m.isVisitado()){
						m.setVisitado();
						if(m.getExistente()==0){
							m.setVazio();
						}
						else
							minaComunicar.add(new Mensagem(this.getId(),m));
					}
				}
				if(!destinos.isEmpty()) newDestQueue();
				else newDest();
			}

			else{ //destinoRandom
				destinos.remove(0);
				if(!destinos.isEmpty()) newDestQueue();
				else newDest();
			}
		}
	}

	public boolean comunicarProdutor(Mensagem msg, int tempo) {
		Produtor p = getProdutor(msg,tempo);
		if(p==null){
			return false;
		}
		else{
			enviaProd(msg,p);
			minaComunicar.remove(0);
			return true;
		}
	}

	private void enviaProd(Mensagem msg, Produtor p) {
		p.setNewDest(msg.getId_sender(),msg.getMina().getX(), msg.getMina().getY(), msg.getMina().getExistente());
		Utils.print(this.getId() + ": A enviar o produtor: "+p.getId()+" para: "+msg.getMina().getX()+","+msg.getMina().getY());
		Utils.print(this.getId() + ": Tempo de resposta: "+p.getResposta(msg).getResposta());
	}


	public void checkMina(){
		@SuppressWarnings("unchecked")
		Vector<Mina> minasPerto = spaceMinas.getMooreNeighbors(x,y,range,range,false);
		for(Mina m : minasPerto){
			if(!m.isEncontrado()){
				m.setEncontradoS();
				if(destinoAtual!=null && !destinoAtual.isRandom()){
					destinos.add(new Destino(this.getId(),m.getX(),m.getY(),false));
				}
				else{
					destinoAtual = new Destino(this.getId(),m.getX(),m.getY(), false);
				}
				minasFound.add(m);
				Utils.print(this.getId() + ": Encontrei a mina: "+m.getX()+","+m.getY());
			}
			else if(!m.isVisitado() && !m.isEncontradoS() && !minasFound.contains(m)){
				if(destinoAtual!=null && !destinoAtual.isRandom()){
					destinos.add(new Destino(this.getId(),m.getX(),m.getY(),false));
				}
				else{
					destinoAtual = new Destino(this.getId(),m.getX(),m.getY(), false);
				}
				minasFound.add(m);
			}
		}
	}

	public void recebeNovaMina(Mensagem msg) {
		boolean encontrado = false;
		for(Mina m : minasVisitadas){
			if(m.getX() == msg.getMina().getX() && m.getY() == msg.getMina().getY())
				encontrado = true;
		}
		
		if(!encontrado){
			minasFound.add(msg.getMina());
			setNewDest(msg.getId_sender(),msg.getMina().getX(), msg.getMina().getY());
		}
		
	}
	
	public Resposta getResposta(Mensagem m){
		return new Resposta(getTempoResposta(m),this.getId(),m.getId_msg());
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
			}
		}
		tempo+= getDist(x_pos,y_pos,msg.getMina().getX(),msg.getMina().getY());
		return tempo;
	}

}
