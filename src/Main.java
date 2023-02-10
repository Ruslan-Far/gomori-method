public class Main {

	public static final double INF_MAX = 999999999;
	public static final double INF_MIN = -1;

//	public static final int[][] INITIAL_MATRIX = {
//			{1, 2, 3, 35},
//			{2, 3, 2, 45},
//			{3, 1, 1, 40},
//			{4, 5, 6, -1}
//	};

	public static final int[][] INITIAL_MATRIX = {
			{1, 2, 3, 35},
			{4, 3, 2, 45},
			{3, 1, 1, 40},
			{4, 5, 6, -1}
	};

//	public static final int[][] INITIAL_MATRIX = {
//			{0, 4, 4},
//			{1, 3, 3},
//			{4, -2, 7},
//			{1, 3, 6},
//			{-4, 3, -1}
//	};

	//////////// min

//	public static final int[][] INITIAL_MATRIX = {
//			{-3, 2, 3},
//			{3, 1, 5},
//			{-1, -1, -1},
//			{3, -3, -1}
//	};

//	public static final int[][] INITIAL_MATRIX = {
//			{1, -5, -6},
//			{0, -3, 4},
//			{2, 1, 8},
//			{-3, 3, 0},
//			{-1, 0, 0},
//			{0, -1, 0},
//			{1, 3, -1}
//	};

//	public static final int[][] INITIAL_MATRIX = {
//			{1, -4, -6},
//			{0, -3, 4},
//			{2, 1, 8},
//			{-3, 3, 0},
//			{1, 3, -1}
//	};

//	public static final int[][] INITIAL_MATRIX = {
//			{-1, -1, -6},
//			{-3, -4, -7},
//			{0, 4, 7},
//			{1, 4, -1}
//	};

	public static void main(String[] args) {
		double[][] simplexTable;
		double[][] simplexTableSecond;

		simplexTable = Simplex.method();
		if (simplexTable == null)
			return;
		CommonFunctions.printArray(Simplex.answer);
		CommonFunctions.printArray(Simplex.rows);
		while (isNotInt()) {
			int indexInAnswerMaxFraction;
			double[] q;
			double[] rationsRowFtoPenultimateRow;
			int indexMinRationRowFtoPenultimateRow;

			indexInAnswerMaxFraction = getIndexInAnswerMaxFraction();
			q = new double[simplexTable[0].length];
			initQ(simplexTable, q, Simplex.rows[indexInAnswerMaxFraction]);
			CommonFunctions.printArray(q);
			simplexTableSecond = genSimplexTableSecond(simplexTable, q);
			CommonFunctions.printMatrix(simplexTableSecond);
			rationsRowFtoPenultimateRow = new double[simplexTableSecond[0].length];
			initRationsRowFtoPenultimateRow(simplexTableSecond, rationsRowFtoPenultimateRow);
			CommonFunctions.printArray(rationsRowFtoPenultimateRow);
			indexMinRationRowFtoPenultimateRow = getIndexMinRationRowFtoPenultimateRow(rationsRowFtoPenultimateRow);
			System.out.println(indexMinRationRowFtoPenultimateRow);
			System.out.println(rationsRowFtoPenultimateRow[indexMinRationRowFtoPenultimateRow]);
			divideRowOnNum(simplexTableSecond, simplexTableSecond.length - 2, indexMinRationRowFtoPenultimateRow);
			CommonFunctions.printMatrix(simplexTableSecond);
			Simplex.addNewBasisVar(simplexTableSecond,
					simplexTableSecond.length - 2, indexMinRationRowFtoPenultimateRow);
			CommonFunctions.printMatrix(simplexTableSecond);
			getAnswer(simplexTableSecond, indexMinRationRowFtoPenultimateRow);
			simplexTable = simplexTableSecond;
//			return;
		}
		System.out.println("Целочисленное решение найдено");
	}

	private static boolean isNotInt() {
		for (int i = 0; i < Simplex.answer.length; i++) {
			if (Simplex.answer[i] % 1 != 0)
				return true;
		}
		return false;
	}

	private static int getIndexInAnswerMaxFraction() {
		double max;
		int index;

		max = INF_MIN;
		index = (int) INF_MIN;
		for (int i = 0; i < Simplex.answer.length; i++) {
			if (Simplex.answer[i] % 1 >= max) {
				index = i;
				max = Simplex.answer[i] % 1;
			}
		}
		return index;
	}

	private static void initQ(double[][] simplexTable, double[] q, int indexRow) {
		double fraction;

		for (int j = 0; j < q.length; j++) {
			fraction = simplexTable[indexRow][j] % 1;
			if (fraction < 0)
				fraction += 1;
			q[j] = -fraction;
		}
	}

	private static double[][] genSimplexTableSecond(double[][] simplexTable, double[] q) {
		double[][] simplexTableSecond;

		simplexTableSecond = new double[simplexTable.length + 1][simplexTable[0].length + 1];
		for (int i = 0; i < simplexTable.length; i++) {
			for (int j = 0; j < simplexTable[0].length; j++) {
				if (i == simplexTable.length - 1) {
					if (j == simplexTable[i].length - 1) {
						simplexTableSecond[i][j] = 1;
						simplexTableSecond[i][j + 1] = q[j];
						continue;
					}
					simplexTableSecond[i][j] = q[j];
					continue;
				}
				if (j == simplexTable[i].length - 1) {
					simplexTableSecond[i][j + 1] = simplexTable[i][j];
					continue;
				}
				simplexTableSecond[i][j] = simplexTable[i][j];
			}
		}
		for (int j = 0; j < simplexTable[0].length; j++) {
			if (j == simplexTable[0].length - 1) {
				simplexTableSecond[simplexTable.length][j + 1] = -simplexTable[simplexTable.length - 1][j];
				continue;
			}
			simplexTableSecond[simplexTable.length][j] = -simplexTable[simplexTable.length - 1][j];
		}
		return simplexTableSecond;
	}

	private static void initRationsRowFtoPenultimateRow(double[][] simplexTableSecond,
													 double[] rationsRowFtoPenultimateRow) {
		for (int j = 0; j < simplexTableSecond[0].length; j++) {
			if (simplexTableSecond[simplexTableSecond.length - 2][j] == 0
					|| j == simplexTableSecond[0].length - 2
					|| j == simplexTableSecond[0].length - 1) {
				rationsRowFtoPenultimateRow[j] = INF_MAX;
			}
			else
				rationsRowFtoPenultimateRow[j] =
						simplexTableSecond[simplexTableSecond.length - 1][j]
								/ simplexTableSecond[simplexTableSecond.length - 2][j];
		}
	}

	private static int getIndexMinRationRowFtoPenultimateRow(double[] rationsRowFtoPenultimateRow) {
		double min;
		int index;

		min = INF_MAX;
		index = -1;
		for (int i = 0; i < rationsRowFtoPenultimateRow.length; i++) {
			if (rationsRowFtoPenultimateRow[i] < min) {
				index = i;
				min = rationsRowFtoPenultimateRow[i];
			}
		}
		return index;
	}

	private static void divideRowOnNum(double[][] simplexTableSecond, int row, int column) {
		double num = simplexTableSecond[row][column];

		for (int j = 0; j < simplexTableSecond[row].length; j++) {
			simplexTableSecond[row][j] /= num;
		}
	}

	private static void getAnswer(double[][] simplexTableSecond, int indexMinRationRowFtoPenultimateRow) {
		for (int i = 0; i < Simplex.answer.length; i++) {
			if (indexMinRationRowFtoPenultimateRow != i) {
				if (Simplex.rows[i] != -1)
					Simplex.answer[i] = simplexTableSecond[Simplex.rows[i]][simplexTableSecond[0].length - 1];
			}
			else {
				Simplex.answer[i] = simplexTableSecond[simplexTableSecond.length - 2][simplexTableSecond[0].length - 1];
				Simplex.rows[i] = simplexTableSecond.length - 2;
			}
		}
		CommonFunctions.printArray(Simplex.answer);
		System.out.printf("Значение целевой функции: %.3f\n",
				-simplexTableSecond[simplexTableSecond.length - 1][simplexTableSecond[0].length - 1]); // max
//		System.out.printf("Значение целевой функции: %.3f\n",
//				simplexTableSecond[simplexTableSecond.length - 1][simplexTableSecond[0].length - 1]); // min
	}
}
