package agentes;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import estruturas.Mina;

import modelo.Floor;
import modelo.Utils;

import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DTorus;
import uchicago.src.sim.util.Random;

public class Agente {

	protected Destino destinoAtual;
	protected ArrayList<Destino> destinos;
	protected ArrayList<Mina> minasFound;
	protected Object2DTorus spaceMinas;
	protected Object2DTorus spaceAgente;
	protected Object2DTorus spaceFloor;
	protected ArrayList<Destino> destinosPossiveis;
	protected int x,y;
	protected Color color;
	protected int range;
	protected int rangeComunicacao;
	private String id;
	private boolean retornar = false;
	protected Image picture;
	public ArrayList<Mensagem> minaComunicar;



	protected void initMatrix(){

		for(int i=0; i<spaceFloor.getSizeX(); i++)
			for(int j=0; j<spaceFloor.getSizeY(); j++)
				destinosPossiveis.add(new Destino(this.getId(),i,j,true));
	}

	public void setNewDest(String id, int x_dest,int y_dest){
		destinos.add(new Destino(id, x_dest,y_dest,false));
		if(destinoAtual.isRandom()){
			destinos.remove(0);
			destinoAtual = destinos.get(0);

		}

	}

	public void setNewDest(String id,int x_dest,int y_dest, int quant){
		destinos.add(new Destino(id,x_dest,y_dest,false,quant));
		if(destinoAtual.isRandom()){
			destinos.remove(0);
			destinoAtual = destinos.get(0);

			if(this.id.contains("t_")){
				if(destinoAtual.getQuant()+((Transportador)this).getQuantidade()>((Transportador)this).capacidade){
					destinos.add(0, new Destino(this.getId(),spaceAgente.getSizeX()/3,spaceAgente.getSizeY()/2,false));
					destinoAtual = destinos.get(0);
				}
			}
		}

	}

	public void retorna(){
		destinos.clear();
		destinoAtual=(new Destino(this.getId(),spaceAgente.getSizeX()/3,spaceAgente.getSizeY()/2,false));
	}

	protected void newDest(){
		if(!destinosPossiveis.isEmpty()){
			destinos.add(destinosPossiveis.remove(Random.uniform.nextIntFromTo(0, destinosPossiveis.size()-1)));
		}
		else 
			destinos.add(new Destino(this.getId(),Random.uniform.nextIntFromTo(0, spaceMinas.getSizeX()-1),
					Random.uniform.nextIntFromTo(0, spaceMinas.getSizeY()-1),true));

		destinoAtual = destinos.get(0);
		//Pinta destino...
		//Floor f = (Floor) spaceFloor.getObjectAt(destinoAtual.getX(), destinoAtual.getY());
		//f.setColor(Color.cyan);
	}

	protected void newDestQueue(){
		if(!(destinos.get(0).getX()==spaceAgente.getSizeX()/3 && destinos.get(0).getY()==spaceAgente.getSizeY()/2)){
			sortDestino(destinos);
		}
		destinoAtual = destinos.get(0);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}



	public void draw(SimGraphics g) {
		if(id.contains("s_") || id.contains("p_") || id.contains("t_")){
			g.drawImage(picture);
			if(id.contains("p_")){
				if(!((Produtor)this).isProduzir())
				{
					g.drawString(id, Color.black);
				}
			}
			else
				g.drawString(id, Color.black);

		}
		else
			g.drawOval(color);
	}

	protected double getDist(int dX,int dY){
		Point2D p = new Point2D.Double(dX,dY);
		return p.distance(new Point2D.Double(x,y));
	}

	protected double getDist(int dX,int dY,int x, int y){
		Point2D p = new Point2D.Double(dX,dY);
		return p.distance(new Point2D.Double(x,y));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void sortDestino(ArrayList<Destino> dest){

		Collections.sort (dest, new Comparator() {  
			public int compare(final Object o1, final Object o2) {
				final Destino d1 = (Destino) o1;  
				final Destino d2 = (Destino) o2;
				final double dist1 = getDist(d1.getX(),d1.getY());
				final double dist2= getDist(d2.getX(),d2.getY());

				return dist1 < dist2 ? -1 : (dist1 > dist2 ? +1 : 0);  
			}  
		});  
	}

	protected void doMove(){
		fillGrid();
		shareGrid();
		spaceAgente.putObjectAt(x, y, null);
		if(x-destinoAtual.getX() !=0){
			if(x>destinoAtual.getX())
				if(x-destinoAtual.getX() <= spaceAgente.getSizeX()-x+destinoAtual.getX())
					x--;
				else{
					if(x==spaceAgente.getSizeX()-1)
						x=0;
					else
						x++;
				}
			else
			{
				if(destinoAtual.getX()-x <= spaceAgente.getSizeX()-destinoAtual.getX()+x)
					x++;
				else{
					if(x==0)
						x=spaceAgente.getSizeX()-1;
					else
						x--;
				}

			}
		}

		if(y-destinoAtual.getY() !=0){
			if(y>destinoAtual.getY())
				if(y-destinoAtual.getY() <= spaceAgente.getSizeY()-y+destinoAtual.getY())
					y--;
				else{
					if(y==spaceAgente.getSizeY()-1)
						y=0;
					else
						y++;
				}
			else{
				if(destinoAtual.getY()-y <= spaceAgente.getSizeY()-destinoAtual.getY()+y)
					y++;
				else{
					if(y==0)
						y=spaceAgente.getSizeY()-1;
					else
						y--;
				}
			}
		}
		spaceAgente.putObjectAt(x, y, this);
	}

	/*
	 * Preenche a "grid" com todas as zonas do espaço já vistos
	 *  
	 */
	private void fillGrid(){
		@SuppressWarnings("unchecked")
		Vector<Object> visao = spaceFloor.getMooreNeighbors(x, y, range,range,false);
		for(Object o : visao){
			try{
				Floor f = (Floor) o;
				if(f!=null && !f.isChecked()){
					for(Destino d : destinosPossiveis)
						if(d.getX()==f.getX() && d.getY() == f.getY())
							destinosPossiveis.remove(d);
							f.setChecked(true);
				}
			}
			catch(Exception e){
			}
		}
	}


	public void matchGrid(ArrayList<Destino> dests){
		destinosPossiveis.retainAll(dests);
	}

	/*
	 * 
	 * Comunica para os agentes perto a sua grelha de locais por visitar
	 * 
	 */
	public void shareGrid(){
		@SuppressWarnings("unchecked")
		Vector<Agente> agentes = spaceAgente.getMooreNeighbors(x, y, range,range,false);
		for(Agente a : agentes){
			a.matchGrid(destinosPossiveis);
		}
	}


	public Spotter getSpotter(Mensagem m, int tempo){
		@SuppressWarnings("unchecked")
		Vector<Agente> agentesPerto = spaceAgente.getMooreNeighbors(x,y,rangeComunicacao,rangeComunicacao,false);
		Vector<Spotter> spotPerto = new Vector<Spotter>();
		for(Agente a: agentesPerto)
			if(a.getId().contains("s_")){
				spotPerto.add((Spotter)a);
			}
		int tempR = 9999;
		Spotter cSpotter = null;
		for(Spotter s : spotPerto){
			int resp = s.getResposta(m).getResposta();
			if(resp < tempo){
				if(resp < tempR){
					tempR = resp;
					cSpotter = s;
				}
			}
		}
		return cSpotter;
	}

	public Produtor getProdutor(Mensagem m, int tempo){
		@SuppressWarnings("unchecked")
		Vector<Agente> agentesPerto = spaceAgente.getMooreNeighbors(x,y,rangeComunicacao,rangeComunicacao,false);
		Vector<Produtor> prodPerto = new Vector<Produtor>();
		ArrayList<String> ids = new ArrayList<String>();
		for(Agente a: agentesPerto)
			if(a.getId().contains("p_")){
				if(!ids.contains(a.getId())){
					ids.add(a.getId());
					prodPerto.add((Produtor)a);
				}
			}
		int tempR = 9999;
		Produtor cProdutor = null;
		for(Produtor p : prodPerto){
			int resp = p.getResposta(m).getResposta();
			if(resp < tempo){
				if(resp < tempR){
					tempR = resp;
					cProdutor = p;
				}
			}
		}
		return cProdutor;

	}

	public Transportador getTransportador(Mensagem m, int tempo){
		@SuppressWarnings("unchecked")
		Vector<Agente> agentesPerto = spaceAgente.getMooreNeighbors(x,y,rangeComunicacao,rangeComunicacao,false);
		Vector<Transportador> transPerto = new Vector<Transportador>();
		ArrayList<String> ids = new ArrayList<String>();
		for(Agente a: agentesPerto)
			if(a.getId().contains("t_")){
				if(!ids.contains(a.getId())){
					ids.add(a.getId());
					transPerto.add((Transportador)a);
				}
			}
		int tempR = 9999;
		Transportador cTransportador = null;
		for(Transportador t : transPerto){
			Utils.print("Tempo que falta: "+ tempo + ", tenho " + m.getMina().getProduzido() + " para carregar ->" + t.getId() + ": demora " + t.getResposta(m).getResposta());
			int resp = t.getResposta(m).getResposta();
			if(resp < tempo){
				if(resp < tempR){
					tempR = resp;
					cTransportador = t;
				}
			}
		}
		return cTransportador;

	}


	public boolean isRetornar() {
		return retornar;
	}

	public void setRetornar(boolean retornar) {
		this.retornar = retornar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
