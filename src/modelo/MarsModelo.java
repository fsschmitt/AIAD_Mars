package modelo;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import estruturas.Base;
import estruturas.Mina;

import agentes.Mensagem;
import agentes.Produtor;
import agentes.Spotter;
import agentes.Transportador;


import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DTorus;
import uchicago.src.sim.util.Random;


public class MarsModelo extends SimModelImpl {
	/** ArrayList com as minas existentes */
	private ArrayList<Mina> minas;
	/** ArrayList com as bases existentes */
	private ArrayList<Base> base;
	/** ArrayList com os spotters existentes*/
	private ArrayList<Spotter> spotters;
	/** ArrayList com os produtores existentes*/
	private ArrayList<Produtor> produtores;
	/** ArrayList com os transportadores existentes*/
	private ArrayList<Transportador> transportadores;
	private ArrayList<Floor> floors;
	/** Schedule que controla o programa */
	private Schedule schedule;
	/** Superfície onde estão os elementos*/
	private DisplaySurface dsurf;
	/** Espaço, do tipo Torus */
	private Object2DTorus space;
	private Object2DTorus spaceFloor;
	private Object2DTorus spaceMinas;
	private Object2DTorus spaceAgente;
	/** Intervalo entre cada ação dos agentes*/
	private int speed;
	/** Número de minas existentes*/
	private int numeroMinas;
	/** Dimensão do espaço (spaceSize x spaceSize)*/
	private int spaceSize;
	/** Número de spotters existentes*/
	private int numSpotters;
	/** Número de produtores existentes*/
	private int numProdutores;
	/** Número de transportadores existentes*/
	private int numTransportadores;
	/** Tempo disponível para explorar o espaço */
	private int tempoLimite;
	private int tempTempoLimite;
	private boolean Limite;
	private static ArrayList<Image> pictures;
	private int rangeComunicacao;
	private OpenSequenceGraph graph;
	private boolean random;
	private int minerioTotal;
	private boolean display;
	

	/**
	 * 
	 * Construtor do modelo
	 * 
	 */
	public MarsModelo() {
		this.setNumeroMinas(10);
		this.setSpaceSize(50);
		this.setRangeComunicacao(8);
		this.setSpeed(10);
		this.setNumSpotters(1);
		this.setNumProdutores(1);
		this.setNumTransportadores(1);
		this.setTempoLimite(100);
		this.setLimite(true);
		this.setRandom(false);
		this.setDisplay(false);
	}

	/**
	 * Retorna o nome do modelo
	 * 
	 * @return Nome do modelo
	 */
	public String getName() {
		return "Pesquisa em Marte";
	}

	/**
	 * Retorna os parâmetros iniciais
	 * 
	 * @return Parâmetros iniciais
	 */
	public String[] getInitParam() {
		return new String[] { "numeroMinas", "spaceSize", "speed", "numSpotters", "numProdutores", "numTransportadores", "rangeComunicacao" ,"tempoLimite", "Limite", "random", "minerioTotal", "display"};
	}

	/**
	 * Retorna o schedule que controla o programa
	 * 
	 * @return Schedule que controla o programa
	 */
	public Schedule getSchedule() {
		return schedule;
	}

	/**
	 * Define o número de Spotters
	 * 
	 * @param numeroMinas Número de Spotters
	 */
	public void setNumSpotters(int numSpotters) {
		this.numSpotters = numSpotters;
	}

	/**
	 * Retorna o número de Spotters
	 * 
	 * @return Número de Spotters
	 */
	public int getNumSpotters() {
		return numSpotters;
	}

	/**
	 * Define o número de Produtores
	 * 
	 * @param numeroMinas Número de Produtores
	 */
	public void setNumProdutores(int numProdutores) {
		this.numProdutores = numProdutores;
	}

	/**
	 * Retorna o número de Produtores
	 * 
	 * @return Número de Produtores
	 */
	public int getNumProdutores() {
		return numProdutores;
	}

	/**
	 * Define o número de Transportadores
	 * 
	 * @param numeroMinas Número de Transportadores
	 */
	public void setNumTransportadores(int numTransportadores) {
		this.numTransportadores = numTransportadores;
	}

	/**
	 * Retorna o número de Transportadores
	 * 
	 * @return Número de Transportadores
	 */
	public int getNumTransportadores() {
		return numTransportadores;
	}

	/**
	 * Retorna o número de minas
	 * 
	 * @return Número de minas
	 */
	public int getNumeroMinas() {
		return numeroMinas;
	}

	/**
	 * Define o número de minas
	 * 
	 * @param numeroMinas Número de Minas
	 */
	public void setNumeroMinas(int numeroMinas) {
		this.numeroMinas = numeroMinas;
	}

	/**
	 * Retorna o intervalo de ação dos agentes
	 * 
	 * @return Intervalo de ação
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Define o intervalo de ação dos agentes
	 * 
	 * @param speed Intervalo de ação
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Retorna a dimensão do espaço
	 * 
	 * @return Dimensão do espaço
	 */
	public int getSpaceSize() {
		return spaceSize;
	}

	/**
	 * Define a dimensão do espaço (SpaceSize x SpaceSize)
	 * 
	 * @param spaceSize Dimensão do espaço
	 */
	public void setSpaceSize(int spaceSize) {
		this.spaceSize = spaceSize;
	}

	/**
	 * Configura o display
	 */
	public void setup() {
		this.setMinerioTotal(0);
		this.setTempoLimite(this.tempTempoLimite);
		schedule = new Schedule(speed);
		if (dsurf != null) dsurf.dispose();
		if(graph !=null) graph.dispose();
		graph = new OpenSequenceGraph("Data",this);
		dsurf = new DisplaySurface(this, "Pesquisa em Marte Display");
		registerDisplaySurface("Pesquisa em Marte Display", dsurf);
		registerMediaProducer("Graph", graph);
		System.gc();
	}

	/**
	 * Inicializa o programa
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SimInit init = new SimInit();
		init.loadModel(new MarsModelo(), null, false);
	}

	/**
	 * Inicializa o modelo
	 */
	public void begin() {
		loadImages();
		buildModel();
		buildDisplay();
		buildSchedule();
		
		
	}
	
	/**
	 * Faz load das imagens
	 */
	private static void loadImages() {
		pictures = new ArrayList<Image>();
		java.net.URL marsURL = MarsModelo.class.getResource("mars.png");
		java.net.URL marsSeenURL = MarsModelo.class.getResource("marsSeen.png");
		java.net.URL homebaseURL = MarsModelo.class.getResource("homebase.png");
		java.net.URL spotterURL = MarsModelo.class.getResource("sentryagent.png");
		java.net.URL produtorURL = MarsModelo.class.getResource("productionagent.png");
		java.net.URL transportadorURL = MarsModelo.class.getResource("carryagent.png");
		pictures.add(new ImageIcon(marsURL).getImage());
		pictures.add(new ImageIcon(marsSeenURL).getImage());
		pictures.add(new ImageIcon(homebaseURL).getImage());
		pictures.add(new ImageIcon(spotterURL).getImage());
		pictures.add(new ImageIcon(produtorURL).getImage());	
		pictures.add(new ImageIcon(transportadorURL).getImage());	
	}
	/**
	 * Constrói a base
	 */
	private void buildBase(){	
		// a base é constituída por quatro células adjacentes, formando um quadrado maior
		int x=space.getSizeX()/3;
		int y=space.getSizeY()/2;
		for(int j=0;j<spaceFloor.getSizeX();j++){
			for(int i=0;i<spaceFloor.getSizeY();i++) {
					Floor f = new Floor(j,i,pictures.get(0),pictures.get(1));
					spaceFloor.putObjectAt(j, i, f);
					floors.add(f);
			}
		}
		Base b = new Base(x,y,pictures.get(2));
		space.putObjectAt(x, y, b);
		base.add(b);
	}

	/**
	 * Constrói os agentes spotters
	 */
	public void buildSpotters(){
		for(int i=0;i < getNumSpotters() ; i++){
			Spotter s = new Spotter(i,getRangeComunicacao(),spaceFloor,spaceAgente,spaceMinas,pictures.get(3));
			spotters.add(s);
			spaceAgente.putObjectAt(spaceAgente.getSizeX()/3, spaceAgente.getSizeY()/2, s);
		}
	}

	public void buildProdutores(){
		for(int i=0;i < getNumProdutores() ; i++){
			Produtor p = new Produtor(i,getRangeComunicacao(),spaceFloor, spaceAgente, spaceMinas,pictures.get(4), random);
			produtores.add(p);
			spaceAgente.putObjectAt(spaceAgente.getSizeX()/3, spaceAgente.getSizeY()/2, p);
		}
	}

	public void buildTransportadores(){
		for(int i=0;i < getNumTransportadores() ; i++){
			Transportador t = new Transportador(i,getRangeComunicacao(),space, spaceFloor, spaceAgente, spaceMinas, pictures.get(5),random);
			transportadores.add(t);
			spaceAgente.putObjectAt(spaceAgente.getSizeX()/3, spaceAgente.getSizeY()/2, t);
		}
	}


	/**
	 * Constrói o modelo
	 */
	public void buildModel() {
		minas = new ArrayList<Mina>();
		base = new ArrayList<Base>();
		spotters = new ArrayList<Spotter>();
		produtores = new ArrayList<Produtor>();
		transportadores = new ArrayList<Transportador>();
		floors = new ArrayList<Floor>();
		space = new Object2DTorus(spaceSize, spaceSize);
		spaceFloor = new Object2DTorus(spaceSize, spaceSize);
		spaceMinas = new Object2DTorus(spaceSize, spaceSize);
		spaceAgente = new Object2DTorus(spaceSize, spaceSize);
		buildBase();

		
		if(random)
			random_minas();
		else
			custom_minas();
	

		buildSpotters();
		buildProdutores();
		buildTransportadores();
	}

	private void custom_minas() {
		int x,y;
		x=10;
		y=10;
		Mina m1 = new Mina(90, x, y);
		spaceMinas.putObjectAt(x, y, m1);
		minas.add(m1);
		x=10;
		y=40;
		Mina m2 = new Mina(80, x, y);
		spaceMinas.putObjectAt(x, y, m2);
		minas.add(m2);
		x=10;
		y=25;
		Mina m3 = new Mina(70, x, y);
		spaceMinas.putObjectAt(x, y, m3);
		minas.add(m3);
		x=25;
		y=10;
		Mina m4 = new Mina(60, x, y);
		spaceMinas.putObjectAt(x, y, m4);
		minas.add(m4);
		x=25;
		y=25;
		Mina m5 = new Mina(50, x, y);
		spaceMinas.putObjectAt(x, y, m5);
		minas.add(m5);
		x=25;
		y=40;
		Mina m6 = new Mina(60, x, y);
		spaceMinas.putObjectAt(x, y, m6);
		minas.add(m6);
		x=40;
		y=10;
		Mina m7 = new Mina(70, x, y);
		spaceMinas.putObjectAt(x, y, m7);
		minas.add(m7);
		x=40;
		y=25;
		Mina m8 = new Mina(80, x, y);
		spaceMinas.putObjectAt(x, y, m8);
		minas.add(m8);
		x=40;
		y=40;
		Mina m9 = new Mina(90, x, y);
		spaceMinas.putObjectAt(x, y, m9);
		minas.add(m9);
		x=20;
		y=30;
		Mina m10 = new Mina(100, x, y);
		spaceMinas.putObjectAt(x, y, m10);
		minas.add(m10);
		
	}

	private void random_minas() {
		int x,y;
		for (int i = 0; i<numeroMinas; i++) {
			do {
				x = Random.uniform.nextIntFromTo(0, space.getSizeX() - 1);
				y = Random.uniform.nextIntFromTo(0, space.getSizeY() - 1);
			} while (space.getObjectAt(x, y) != null);
			int quant = Random.uniform.nextIntFromTo(0, 100);
			Mina m = new Mina(quant, x, y);
			spaceMinas.putObjectAt(x, y, m);
			minas.add(m);
		}
		
	}

	/**
	 * Constrói o display
	 */
	private void buildDisplay() {
		// space and display surface
		Object2DDisplay displayF = new Object2DDisplay(spaceFloor);
		Object2DDisplay displayM = new Object2DDisplay(spaceMinas);
		Object2DDisplay displayB = new Object2DDisplay(space);
		Object2DDisplay displayS = new Object2DDisplay(spaceAgente);
		Object2DDisplay displayP = new Object2DDisplay(spaceAgente);
		Object2DDisplay displayT = new Object2DDisplay(spaceAgente);
		displayF.setObjectList(floors);
		displayM.setObjectList(minas);
		displayB.setObjectList(base);
		displayS.setObjectList(spotters);
		displayP.setObjectList(produtores);
		displayT.setObjectList(transportadores);
		dsurf.addDisplayableProbeable(displayF, "Floor Space");
		dsurf.addDisplayableProbeable(displayM, "Minas Space");
		dsurf.addDisplayableProbeable(displayB, "Base Space");
		dsurf.addDisplayableProbeable(displayS, "Spotter Space");
		dsurf.addDisplayableProbeable(displayP, "Produtor Space");
		dsurf.addDisplayableProbeable(displayT, "Transportador Space");
		dsurf.setBackground(Color.black);
		if(isDisplay()){
			graph.display();
			dsurf.display();
		}
		
		graph.addSequence("Exploração em marte", new Sequence(){
			public double getSValue(){
				double total=0;
				for(Transportador t : transportadores){
					total+=t.getQuantidade();
				}
				return total+(double) ((Base)space.getObjectAt(space.getSizeX()/3, space.getSizeY()/2)).getExistente();
			}
		});
		
		graph.setXRange(0, 200);
		graph.setYRange(0, 200);
		graph.setAxisTitles("time", "Num Minério");
	}

	/**
	 * Constrói o schedule que controla o programa
	 */
	private void buildSchedule() {
		schedule.scheduleActionBeginning(0, new MainAction());
		if(isDisplay())
			schedule.scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
	}


	private void actionSpotter(){

		for(Spotter s : spotters){
			s.move();
			//Caso tenha minas a delegar
			if(!s.minaComunicar.isEmpty()){
				s.comunicarProdutor(s.minaComunicar.get(0), tempoLimite);
			}
			s.checkMina();
		}

	}

	private void actionProdutor(){

		for(Produtor p : produtores){
			p.move();
			
			if(!p.minaProduzido.isEmpty()){
				p.comunicarTransportador(p.minaProduzido.get(0), tempoLimite);
			}
			
			ArrayList<Mina> checkMinas = p.checkMina();
			if(checkMinas != null)
				for(Mina m : checkMinas){
					p.minaComunicar.add(new Mensagem(p.getId(),m));
				}
			if(!p.minaComunicar.isEmpty()){
				for(int i=0;i<p.minaComunicar.size();i++){
					Spotter cSpotter = p.getSpotter(p.minaComunicar.get(i),tempoLimite);
					if(cSpotter!=null){
						Utils.print(p.getId() + ": A enviar o spotter "+cSpotter.getId()+" para: "+p.minaComunicar.get(i).getMina().getX()+" ,y:"+p.minaComunicar.get(i).getMina().getY());
						cSpotter.recebeNovaMina(p.minaComunicar.get(i));
						p.minaComunicar.remove(i);
						i--;
					}
				}
			}
		}

	}

	private void actionTransportador() {
		for(Transportador t : transportadores){
			t.move();
			ArrayList<Mina> found = t.checkMina();
			if(found != null && !found.isEmpty()){
				for(Mina m : found){
					t.minaComunicar.add(new Mensagem(t.getId(),m));
				}
				if(!t.minaComunicar.isEmpty()){
					for(int i=0;i<t.minaComunicar.size();i++){
						Spotter cSpotter = t.getSpotter(t.minaComunicar.get(i),tempoLimite);
						if(cSpotter!=null){
							Utils.print(t.getId() + ": Sending spotter to x:"+t.minaComunicar.get(i).getMina().getX()+" ,y:"+t.minaComunicar.get(i).getMina().getY());
							cSpotter.recebeNovaMina(t.minaComunicar.get(i));
							t.minaComunicar.remove(i);
							i--;
						}
					}
					
				}
			}
		}

	}


	public int getTempoLimite() {
		return tempoLimite;
	}

	public void setTempoLimite(int tempoLimite) {
		this.tempoLimite = tempoLimite;
		this.tempTempoLimite = tempoLimite;
	}


	public boolean isLimite() {
		return Limite;
	}

	public void setLimite(boolean limite) {
		Limite = limite;
	}
	
	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean Random) {
		random = Random;
	}
	
	public int getRangeComunicacao() {
		return rangeComunicacao;
	}

	public void setRangeComunicacao(int rangeComunicacao) {
		this.rangeComunicacao = rangeComunicacao;
	}


	public int getMinerioTotal() {
		return minerioTotal;
	}

	public void setMinerioTotal(int minerioTotal) {
		this.minerioTotal = minerioTotal;
	}


	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean Display) {
		Utils.display=Display;
		this.display = Display;
	}


	class MainAction extends BasicAction {

		/**
		 * Executa as ações definidas para cada agente
		 */
		public void execute() {
			//Utils.print(tempoLimite);
			if(display)
				graph.step();
			if(tempoLimite>0){
				actionSpotter();
				actionProdutor();
				actionTransportador();
				if(Limite)
					tempoLimite--;
			}
			else if (tempoLimite==0){
				retornar();
				tempoLimite--;
			}
			else {
				for(Spotter s : spotters){
					s.move();
				}
				for(Produtor p : produtores){
					p.move();
				}
				for(Transportador t : transportadores){
					t.move();
				}
				tempoLimite--;
			}
			if(tempoLimite==-spaceSize/2-10){
				for(Transportador t : transportadores){
					setMinerioTotal(getMinerioTotal()+t.getQuantidade());
				}
				setMinerioTotal(getMinerioTotal()+(int) ((Base)space.getObjectAt(space.getSizeX()/3, space.getSizeY()/2)).getExistente());
				stop();
			}
		}

		private void retornar() {
			for(Spotter s : spotters){
				s.retorna();
				s.setRetornar(true);
			}
			for(Produtor p : produtores){
				p.retorna();
				p.setRetornar(true);
			}
			for(Transportador t : transportadores){
				t.retorna();
				t.setRetornar(true);
			}
		}

	}
}
