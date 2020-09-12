package clase;

import java.util.Random;

public class clase1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int[][] numeros = new int[7][5];
		double suma = 0;
		double denominador= 0;
		double promedio = 0;
		for(int i = 0; i < numeros.length; i += 1){
			int columnas = numeros[i].length;
			for(int j = 0; j<columnas; j += 1){
				numeros[i][j] = random.nextInt(20)+1;
				System.out.print(" "+ numeros[i][j]);
			}
			System.out.println();
		}
		
		for(int i = 0; i < numeros.length; i += 1){
			int columnas = numeros[i].length;
			for(int j = 0; j < columnas; j += 1){
				int evaluado = numeros[i][j];
				if(evaluado%2 == 0){
					suma += evaluado;
					denominador+=1;
				}
			}
		}
		if(denominador != 0){
			promedio = (double)suma/denominador;
		}
		System.out.println(promedio);
		//fila con menor promedio//
		double mayorPromedio = -1;
		double promedioFila = 0;
		int mayorFila = -1;
		for(int i = 0; i < numeros.length; i++){
			promedioFila = 0;
			int denom = numeros[i].length;
			for(int j = 0; j < denom; j++){
				promedioFila += numeros[i][j];
			}
			promedioFila /= (double)denom;
			if(promedioFila > mayorPromedio){
				mayorPromedio = promedioFila;
				mayorFila = i;
			}
			
		}
		System.out.println(mayorFila+" Su promedio: " + mayorPromedio);

	}

}
