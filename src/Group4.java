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

		//sort(toSort); // call the sorting method once for JVM warmup
		
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
			gonnaSort[i] = new Data(toSort[i].toString());
			System.out.println(gonnaSort[i].numerator);
			System.out.println(gonnaSort[i].denominator);
			
		}

		Arrays.sort(gonnaSort, new SortingCompetitionComparator());
		return gonnaSort;
	}
	
	private static class Data {
		public String str;

		boolean boofrac;

		public int numerator;
		public int denominator;
		
		public int numlength;
		public int denlength;
		
		
		public Data(String string){
			if(string.contains("/")){
				this.str = string;
				this.boofrac = true;

				String[] saFrac = string.split("/");
				
				if(saFrac[0].length()<4){
				this.numlength = saFrac[0].length();
				}else{
				this.numlength = 4;
				}

				if(saFrac[1].length()<4){
					this.denlength = saFrac[1].length();
					}else{
					this.denlength = 4;
					}
			
					// We're only storing the 4 most significant digits so we can we can cross multiply and have it
					// still fit in an int.
				this.numerator = Integer.valueOf(saFrac[0].substring(0, this.numlength));
				this.denominator = Integer.valueOf(saFrac[1].substring(0, this.denlength));
				
			}else{
				this.str = string;
				this.boofrac = false;

				String[] saDec = string.split("\\.");
				
				if(saDec.length == 1){
					this.numerator = Integer.valueOf(saDec[0]);
					this.denominator = 1;

				}else{
					if(saDec[0].length()<4){
					this.numlength = saDec[0].length();
					}else{
						this.numlength = 4;
					}

					if(saDec[1].length()<4){
						this.denlength = saDec[1].length();
					}else{
						this.denlength = 4;
					}
	
					int wholenum = Integer.valueOf(saDec[0]);
					this.numerator = Integer.valueOf(saDec[1].substring(0, this.denlength));
					this.denominator = (int) Math.pow(10, this.denlength);
					
					if(saDec[0].contains("-")){
						this.numerator = ((this.denominator * wholenum) - this.numerator);
						if(this.numerator<-9999){
							String temp = String.valueOf(this.numerator);
							this.numerator = Integer.valueOf(temp.substring(0, 5));
						}
					}else{
						this.numerator = ((this.denominator * wholenum) + this.numerator);
						if(this.numerator>9999){
							String temp = String.valueOf(this.numerator);
							this.numerator = Integer.valueOf(temp.substring(0, 4));
						}
					}
				}
			}
		}		
	}

	private static class SortingCompetitionComparator implements Comparator<Data> {

		@Override
		public int compare(Data fraction1, Data fraction2) {
			// Compare fractions by cross multiplication. Each part of the fraction was limited to the 4 most significant digits so 
			// overflow doesn't occur. If they are equal for as many digits an int can hold, then we call
			// a function to compare them more precisely.
			int crossMult1 = (fraction1.numerator * fraction2.denominator);
			int crossMult2 = (fraction2.numerator * fraction1.denominator);
			
			if(crossMult1<crossMult2){
				return -1;
			}else{
				if(crossMult1==crossMult2){
					return checkPrecision(fraction1, fraction2);  
				}else{
					return 1;
				}
			}		
		}

		private int checkPrecision(Data fraction1, Data fraction2){
			// If 4 digits isn't sufficient we need more precision. If they're still equal we need to make sure
			// that non-simplified fractions are considered bigger than simpler ones and fractional representations
			// are considered bigger than decimal representations.
			return 0;
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