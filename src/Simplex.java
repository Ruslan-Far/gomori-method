public class Simplex {

	public static void method() {
		double[][] simplexTable;

		simplexTable = genSimplexTable();
		CommonFunctions.printMatrix(simplexTable);
		while (!isOptimal(simplexTable)) {
			int[] coordsMinInRowF = getCoordsMinInRowF(simplexTable);
//			System.out.println(coordsMinInRowF[0] + " " + coordsMinInRowF[1]);
//			System.out.println(simplexTable[coordsMinInRowF[0]][coordsMinInRowF[1]]);
			int[] coordsMinRatioColBtoColBasis = getCoordsMinRatioColBtoColBasis(simplexTable, coordsMinInRowF);
//			System.out.println(coordsMinRatioColBtoColBasis[0] + " " + coordsMinRatioColBtoColBasis[1]);
//			System.out.println(simplexTable[coordsMinRatioColBtoColBasis[0]][coordsMinRatioColBtoColBasis[1]]);
			divideRowOnNum(simplexTable, coordsMinInRowF, coordsMinRatioColBtoColBasis);
			CommonFunctions.printMatrix(simplexTable);
			return;
		}
	}

	private static double[][] genSimplexTable() {
		double[][] simplexTable = new double
				[Main.INITIAL_MATRIX.length][(Main.INITIAL_MATRIX[0].length - 1) * 2 + 1];
		for (int i = 0; i < simplexTable.length - 1; i++) {
			for (int j = 0; j < Main.INITIAL_MATRIX[0].length - 1; j++) {
				simplexTable[i][j] = Main.INITIAL_MATRIX[i][j];
			}
			simplexTable[i][Main.INITIAL_MATRIX[0].length - 1 + i] = 1;
			simplexTable[i][simplexTable[i].length - 1] = Main.INITIAL_MATRIX[i][Main.INITIAL_MATRIX[i].length - 1];
		}
		for (int j = 0; j < Main.INITIAL_MATRIX[0].length - 1; j++) {
			simplexTable[simplexTable.length - 1][j] = -Main.INITIAL_MATRIX[Main.INITIAL_MATRIX.length - 1][j];
		}
		return simplexTable;
	}

	private static boolean isOptimal(double[][] simplexTable) {
		for (int j = 0; j < Main.INITIAL_MATRIX[0].length - 1; j++) {
			if (simplexTable[simplexTable.length - 1][j] < 0)
				return false;
		}
		return true;
	}

	private static int[] getCoordsMinInRowF(double[][] simplexTable) {
		int[] coords = new int[2];
		double min = 999999999;

		coords[0] = simplexTable.length - 1;
		for (int j = 0; j < Main.INITIAL_MATRIX[0].length - 1; j++) {
			if (simplexTable[simplexTable.length - 1][j] < min) {
				coords[1] = j;
				min = simplexTable[simplexTable.length - 1][j];
			}
		}
		return coords;
	}

	private static int[] getCoordsMinRatioColBtoColBasis(double[][] simplexTable, int[] coordsMinInRowF) {
		int[] coordsMinRatioColBtoColBasis = new int[2];
		double min = 999999999;
		double ratio;

		coordsMinRatioColBtoColBasis[1] = simplexTable[0].length - 1;
		for (int i = 0; i < simplexTable.length - 1; i++) {
			if (simplexTable[i][coordsMinInRowF[1]] == 0)
				continue;
			ratio = simplexTable[i][coordsMinRatioColBtoColBasis[1]] / simplexTable[i][coordsMinInRowF[1]];
			if (ratio < 0)
				continue;
			if (ratio < min) {
				coordsMinRatioColBtoColBasis[0] = i;
				min = ratio;
			}
		}
		return coordsMinRatioColBtoColBasis;
	}

	private static void divideRowOnNum(double[][] simplexTable,
										  int[] coordsMinInRowF, int[] coordsMinRatioColBtoColBasis) {
		double num = simplexTable[coordsMinRatioColBtoColBasis[0]][coordsMinInRowF[1]];

		System.out.println("num=" + num);
		for (int j = 0; j < simplexTable[coordsMinRatioColBtoColBasis[0]].length; j++) {
			simplexTable[coordsMinRatioColBtoColBasis[0]][j] /= num;
		}
	}
}
