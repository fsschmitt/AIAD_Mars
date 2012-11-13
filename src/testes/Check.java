package testes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Check {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String filename = "localSpot1";
		int numRun = 50;
		BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Felipe Schmitt\\workspace\\aiad_mars\\Testes\\"+filename+".out")); 
		FileWriter fstream = new FileWriter("C:\\Users\\Felipe Schmitt\\workspace\\aiad_mars\\Testes\\"+filename+".res");
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("Teste "+filename+"\n");
		String dummy;
		int index = 0;
		int it = 1;
		do{
			dummy = in.readLine();
		}while(!dummy.contains("\""));
		String text;
		String[] temp = new String[15];
		int count=0;
		double media = 0;
		double tempM = 0;
		String [] tempLine = new String[15];
		do{
			text = in.readLine();
			temp = text.split(",");
			
			if(!text.equals("")){
				index++;
				count+=Integer.parseInt(temp[12].split("\\.")[0]);
				if(index == numRun){
					tempM = count/numRun;
					out.write("Realizadas "+numRun+" runs do tipo: "+it+"\n"+"Num Spotters: "+temp[7]+"\n"+"Num Produtores: "+temp[8]+"\n"+"Num Transportadores: "+temp[9]);
					System.out.println("Realizadas "+numRun+" runs do tipo: "+it+"\n"+"Num Spotters: "+temp[7]+"\n"+"Num Produtores: "+temp[8]+"\n"+"Num Transportadores: "+temp[9]);
					out.write("Media: "+tempM+"\n");
					System.out.println("Media: "+tempM);
					if(tempM > media){
						tempLine = temp;
						media = tempM;
					}
					count=0;
					tempM=0;
					index=0;
					it++;
				}
			}
		}while(!text.equals(""));
		
		in.close();
		System.out.println("Média máxima: "+media+"\n"+"Num Spotters: "+tempLine[7]+"\n"+"Num Produtores: "+tempLine[8]+"\n"+"Num Transportadores: "+tempLine[9]);
		out.write("Média máxima: "+media+"\n"+"Num Spotters: "+tempLine[7]+"\n"+"Num Produtores: "+tempLine[8]+"\n"+"Num Transportadores: "+tempLine[9]);
		out.close();
		//System.out.println("O melhor resultado foi:"+"\n"+"Run: "+best[0]+"\n"+"Numero de Spotters: "+best[7]+"\n"+"Numero de Produtores: "+best[8]+"\n"+"Numero de Transportadores: "+best[9]+"\n"+"Numero total de minério: "+best[12]);
	}

}
