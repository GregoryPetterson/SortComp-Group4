import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;



public class Group4 {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		if (args.length < 2) {
			System.out.println(
					"Please run with two command line arguments: input and output file names");
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];
		
		//runTests();

		String[] data = readData(inputFileName); // read data as strings
		
		String[] toSort = data.clone(); // clone the data

		sort(toSort); // call the sorting method once for JVM warmup
		
		toSort = data.clone(); // clone again

		Thread.sleep(10); // to let other things finish before timing; adds stability of runs

		long start = System.currentTimeMillis();

		Data[] sorted = sort(toSort); // sort again

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(sorted, outFileName); // write out the results

	}


	private static String[] readData(String inputFileName) throws FileNotFoundException {
		ArrayList<String> input = new ArrayList<>();
		Scanner in = new Scanner(new File(inputFileName));

		while (in.hasNext()) {
			input.add(in.next());
		}

		in.close();

		// the string array is passed just so that the correct type can be created
		return input.toArray(new String[0]);
	}

	// YOUR SORTING METHOD GOES HERE.
	// You may call other methods and use other classes.
	// Note: you may change the return type of the method.
	// You would need to provide your own function that prints your sorted array to
	// a file in the exact same format that my program outputs
	private static Data[] sort(String[] toSort) {
		int toSortlength = (toSort.length);
		Data[] gonnaSort = new Data[toSortlength];

		for (int i = 0; i < toSortlength; i++) {
			gonnaSort[i] = new Data(toSort[i]);
			//System.out.println(gonnaSort[i].numerator);
			//System.out.println(gonnaSort[i].denominator);
		}
		
		Arrays.sort(gonnaSort, new SortingCompetitionComparator());
		return gonnaSort;
	}
	
	private static class Data {
		public String str;
		public long numerator;
		public long denominator;
		public BigInteger bignumerator;
		public BigInteger bigdenominator;
		public int numlength;
		public int denlength;
		
		public Data(String string){
			if(string.contains("/")){
				String[] saFrac = string.split("/");
				this.str = string;

				this.numlength = saFrac[0].length();
				this.denlength = saFrac[1].length();

				if(denlength<18 && numlength<18){
					this.numerator = Long.valueOf(saFrac[0]);
					this.denominator = Long.valueOf(saFrac[1]);
				}else{
					this.numerator = 1234567891234567891L;
					this.denominator = 1234567891234567891L;
				}
				
				this.bignumerator = new BigInteger(saFrac[0]);
				this.bigdenominator = new BigInteger(saFrac[1]);
			
			}else{
				String[] saDec = string.split("\\.");
				this.str = string;

				this.numlength = saDec[0].length();
				if(saDec.length > 1){
					this.denlength = saDec[1].length();
				}
				
					if(saDec.length == 1){
						this.numerator = Long.valueOf(saDec[0]);
						this.denominator = 1;	
						
						this.bignumerator = new BigInteger(saDec[0]);
						this.bigdenominator = new BigInteger("1");
					}else{
						if(denlength<18 && numlength<18){
							this.numerator = Long.valueOf(saDec[0]);
							this.denominator = Long.valueOf(saDec[1]);
						}else{
							this.numerator = 1234567891234567891L;
							this.denominator = 1234567891234567891L;

						}

					}
							

					
			}
		}		
	}




	private static class SortingCompetitionComparator implements Comparator<Data> {

		@Override
		public int compare(Data fraction1, Data fraction2) {
			// compare fraction by multiplication as big integers,
			// to make sure we are not losing precision
			if(((fraction1.numlength+fraction2.denlength)<18)&&((fraction1.denlength+fraction2.numlength)<18)){
				Long crossMult1 = (fraction1.numerator * fraction2.denominator);
				Long crossMult2 = (fraction2.numerator * fraction1.denominator);
				int res;

				if(crossMult1<crossMult2){
					res = -1;
				}else{
					if(crossMult1==crossMult2){
						res = 0;
					}else{
						res = 1;
					}
					
				}

				if (res != 0) return res;

				if(fraction1.numerator<fraction2.numerator){
					return -1;
				}else{
					if(crossMult1==crossMult2){
						return 0;
					}else{
						return 1;
					}
					
				}
				
			}else{
			
			
			BigInteger crossMult1 = fraction1.bignumerator.multiply(fraction2.bigdenominator);
			BigInteger crossMult2 = fraction2.bignumerator.multiply(fraction1.bigdenominator);
			
			int res = crossMult1.compareTo(crossMult2);	
			
			if (res != 0) return res;
			
			return fraction1.bignumerator.compareTo(fraction2.bignumerator); // note: the numerator may be negative, that would reverse the ordering for negatives
			}
		}
	}
	
	private static void writeOutResult(Data[] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (Data s : sorted) {
			out.println(s.str);
		}
		out.close();
	}
	
}