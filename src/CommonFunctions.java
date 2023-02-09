public class CommonFunctions {

	public static void printMatrix(double [][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.printf("%4.2f\t", matrix[i][j]);
			}
			System.out.println();
		}
	}
}
