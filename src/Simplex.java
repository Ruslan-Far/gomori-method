import java.util.Arrays;

public class Simplex {

	public static void method() {
		double[][] simplexTable;
		int countIteration;
		int[] columns;

		countIteration = 0;
		columns = new int[Main.INITIAL_MATRIX[0].length - 1];
		Arrays.fill(columns, -1);
		System.out.println("------Симплекс-метод------");
		simplexTable = genSimplexTable();
		System.out.println("Сгенерированная симплекс-таблица");
		CommonFunctions.printMatrix(simplexTable);
		while (!isOptimal(simplexTable)) {
			System.out.println(++countIteration + " итерация");
			int[] coordsMinInRowF = getCoordsMinInRowF(simplexTable);
			System.out.println("Координаты минимального числа в строке F: " + coordsMinInRowF[0] + " " + coordsMinInRowF[1]);
			System.out.println("Минимальное число в строке F: " + simplexTable[coordsMinInRowF[0]][coordsMinInRowF[1]]);
			int[] coordsMinRatioColBtoColBasis = getCoordsMinRatioColBtoColBasis(simplexTable, coordsMinInRowF);
			System.out.println("Координаты свободного члена в столбце B, после деления которого получилось минимальное число: " + coordsMinRatioColBtoColBasis[0] + " " + coordsMinRatioColBtoColBasis[1]);
			System.out.println("Координаты числа, на которое будем делить всю строку: " + coordsMinRatioColBtoColBasis[0] + " " + coordsMinInRowF[1]);
			columns[coordsMinRatioColBtoColBasis[0]] = coordsMinInRowF[1];
			System.out.println("Число, на которое будем делить всю строку: " + simplexTable[coordsMinRatioColBtoColBasis[0]][coordsMinInRowF[1]]);
			divideRowOnNum(simplexTable, coordsMinInRowF, coordsMinRatioColBtoColBasis);
			System.out.println("После деления");
			CommonFunctions.printMatrix(simplexTable);
			addNewBasisVar(simplexTable, coordsMinRatioColBtoColBasis[0], coordsMinInRowF[1]);
			System.out.println("После добавления базисной переменной");
			CommonFunctions.printMatrix(simplexTable);
			System.out.println("\n\n");
		}
		getAnswer(simplexTable, columns);
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

		for (int j = 0; j < simplexTable[coordsMinRatioColBtoColBasis[0]].length; j++) {
			simplexTable[coordsMinRatioColBtoColBasis[0]][j] /= num;
		}
	}

	private static void addNewBasisVar(double[][] simplexTable, int rowOneBasisVar, int colOneBasisVar) {
		for (int i = 0; i < simplexTable.length; i++) {
			if (i == rowOneBasisVar)
				continue;
			double factor = -simplexTable[i][colOneBasisVar];
			for (int j = 0; j < simplexTable[i].length; j++) {
				simplexTable[i][j] += factor * simplexTable[rowOneBasisVar][j];
			}
		}
	}

	private static void getAnswer(double[][] simplexTable, int[] columns) {
		int count;

		System.out.println("Оптимальное решение получено");
		count = 0;
		System.out.print("(");
		while (count != columns.length) {
			for (int i = 0; i < columns.length; i++) {
				if (count == columns[i]) {
					System.out.printf(" %.3f", simplexTable[i][simplexTable[i].length - 1]);
					break;
				}
				if (i == columns.length - 1)
					System.out.print(" 0,000");
			}
			count++;
		}
		System.out.println(" )");
		System.out.printf("Значение целевой функции: %.3f\n", simplexTable[simplexTable.length - 1][simplexTable[0].length - 1]);
	}
}
