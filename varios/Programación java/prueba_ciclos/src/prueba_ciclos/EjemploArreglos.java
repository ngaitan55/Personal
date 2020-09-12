package prueba_ciclos;
import java.util.Scanner;
public class EjemploArreglos {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double[] notas;
		notas = new double[5];
		notas[0] = 5;
		notas[1] = 4.5;
		notas[2] = 2.5;
		notas[3] = 4.3;
		notas[4] = 3.8;
		double suma =  notas[1] + notas[4];
		System.out.println(suma);
		notas[2]+=1;
		System.out.println("Nota modificada a: " + notas[2]);
		int cantidadNotas = notas.length;
		System.out.println("Las notas son: " + cantidadNotas);
		
		double [] grades;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Escriba la cantidad de notas");
		String linea = scanner.nextLine();
		int cantidadDeNotas = Integer.parseInt(linea);
		System.out.println(cantidadDeNotas);
		grades = new double[cantidadDeNotas];
		int n = grades.length;
		for(int i = 0; i<n; i+=1){
			System.out.println("Escriba la siguiente nota: ");
			linea = scanner.nextLine();
			System.out.println("Siguiente línea: "+ linea + " contador: " + i + " cantidad notas: " + n);
			grades[i] = Double.parseDouble(linea);
		}
		System.out.println("Finalizada la captura de notas"); 
		double numerador = 0;
		double denominador = grades.length;;
		double promedio = 0;
		for(int i = 0; i<grades.length; i +=1){
			numerador += grades[i];
		}
		if(denominador == 0 || numerador == 0){
			promedio = 0;
		}else{
			promedio = numerador/denominador;
		}
		if(promedio != 0){
		System.out.println("El promedio es: " + promedio);
		}else if(denominador != 0){
			System.out.println("Todos sacaron 0");
		}else{
			System.out.println("No hay notas");
		}

	}

}
