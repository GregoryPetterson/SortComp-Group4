import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.lang.Long;

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

		sort(toSort); // sort again

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(toSort, outFileName); // write out the results

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
	private static void sort(String[] toSort) {
		Arrays.sort(toSort, new SortingCompetitionComparator());
	}

	private static class SortingCompetitionComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			if(o1.contains("/")){
				if(o2.contains("/")){
					return compareFractions(o1, o2);
				}else{
					return compareFractionAndDecimal(o1, o2);
				}

			}else{
				if(o2.contains("/")){
					return -compareFractionAndDecimal(o2, o1);
				}else{
					return compareDecimals(o1,o2);
				}
			}

		}

		
		private int compareFractionAndDecimal(String fraction,String decimal) {
			String[] saFrac = fraction.split("/");
			BigInteger numerator1 = new BigInteger(saFrac[0]);
			BigInteger denominator1 = new BigInteger(saFrac[1]);
			
			//find the length of the decimal's fractional part
			String[] saDec = decimal.split("\\."); // need \\ because . is a special symbol in regex
			
			BigInteger numerator2 = null;
			BigInteger denominator2 = null; 				
			
			if (saDec.length == 1) { // an integer, positive or negative
				numerator2 = new BigInteger(saDec[0]);
				denominator2 = new BigInteger("1");
			} else {
				// find the length of the decimal part
				int n = saDec[1].length();
				denominator2 = new BigInteger("1"); 
				// raising 10 to the power n
				for (int i = 0; i < n; ++i) {
					denominator2 = denominator2.multiply(new BigInteger("10"));
				}
				numerator2 = new BigInteger(saDec[1]);
				// adding the integer part
				BigInteger intPart = new BigInteger(saDec[0]);
				if (saDec[0].charAt(0) == '-') { // the number is negative 
					numerator2 = (intPart.multiply(denominator2)).subtract(numerator2);
				} else {
					numerator2 = numerator2.add(intPart.multiply(denominator2));
				}
			}
			
			BigInteger crossMult1 = numerator1.multiply(denominator2);
			BigInteger crossMult2 = numerator2.multiply(denominator1);
			
			int res = crossMult1.compareTo(crossMult2);
			
			if (res != 0) return res;
			
			if (numerator1.compareTo(new BigInteger("0")) >= 0) {
				return 1; // for positive, the decimal is smaller
			} else {
				return -1; // for negative, the decimal is larger
			}
		}

		private int compareFractions(String fraction1, String fraction2) {
			// compare fraction by multiplication as big integers,
			// to make sure we are not losing precision
			
			String[] sa1 = fraction1.split("/");
			//String numerator1 = sa1[0];
			//String denominator1 = sa1[1];
			
			String[] sa2 = fraction2.split("/");
			//String numerator2 = sa2[0];
			//String denominator2 = sa2[1];

			// Checks the num of digits of the product to see if we can store it as a long or if we need to store it as a big integer.
			if(((sa1[0].length() + sa2[1].length()) < 16) || ((sa2[0].length() + sa1[1].length()) < 16)){
				long num1 =  Long.parseLong(sa1[0]);
				long den1 =  Long.parseLong(sa1[1]);
				long num2 =  Long.parseLong(sa2[0]);
				long den2 =  Long.parseLong(sa2[1]);

				long crossMult1 = (num1 * den2);
				long crossMult2 = (num2 *den1);
			
			
			int res = (crossMult1<crossMult2 ? -1 : (crossMult1==crossMult2 ? 0 : 1));	
			
			if (res != 0) return res;
			
			return (num1<num2 ? -1 : (num1==num2 ? 0 : 1));  // note: the numerator may be negative, that would reverse the ordering for negative.
		
		}else{

			BigInteger bignumerator1 = new BigInteger(sa1[0]);
			BigInteger bigdenominator1 = new BigInteger(sa1[1]);

			BigInteger bignumerator2 = new BigInteger(sa2[0]);
			BigInteger bigdenominator2 = new BigInteger(sa2[1]);

			BigInteger crossMult1 = bignumerator1.multiply(bigdenominator2);
			BigInteger crossMult2 = bignumerator2.multiply(bigdenominator1);
			
			int res = crossMult1.compareTo(crossMult2);	
			
			if (res != 0) return res;
			
			return bignumerator1.compareTo(bignumerator2); // note: the numerator may be negative, that would reverse the ordering for negatives
			}
		}


		private int compareDecimals(String o1, String o2) {
			if (o1.length()<16 || o2.length()<16){
				double dec1 = Double.parseDouble(o1);
				double dec2 = Double.parseDouble(o2);
				
				return (dec1<dec2 ? -1 : (dec1==dec2 ? 0 : 1));

			}else{
				
				return (new BigDecimal(o1)).compareTo(new BigDecimal(o2));
			
			}
		}




	}
	
	private static void writeOutResult(String[] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (String s : sorted) {
			out.println(s);
		}
		out.close();
	}
	
	private static void runTests() {
		SortingCompetitionComparator comp = new SortingCompetitionComparator();
		
		// Two fractions, positive and negative
		System.out.println("-1/2 and 1/4:" + comp.compareFractions("-1/2","1/4"));
		System.out.println("1/2 and 1/3:" + comp.compareFractions("1/2","1/3"));
		System.out.println("1/2 and 2/4:" + comp.compareFractions("1/2","2/4"));
		System.out.println("-1/2 and -2/4:" + comp.compareFractions("-1/2","-2/4"));
		
		// Fraction and a decimal
		System.out.println("1/4 and 0.5:" + comp.compareFractionAndDecimal("1/4","0.5"));
		System.out.println("2/4 and 1.5:" + comp.compareFractionAndDecimal("2/4","1.5"));
		System.out.println("-2/4 and -1.5:" + comp.compareFractionAndDecimal("-2/4","-1.5"));
		System.out.println("-2/4 and 0:" + comp.compareFractionAndDecimal("-2/4","0"));
		System.out.println("1/2 and -0.5:" + comp.compareFractionAndDecimal("1/2","-0.5"));
		System.out.println("1/2 and 0.5:" + comp.compareFractionAndDecimal("1/2","0.5"));
		System.out.println("-1/2 and -0.5:" + comp.compareFractionAndDecimal("-1/2","-0.5"));
		System.out.println("1/3 and -0.5:" + comp.compareFractionAndDecimal("1/3","-0.5"));
		
		System.out.println("-4.9999999999999999999999999999999999 and -5:" + comp.compareDecimals("-4.9999999999999999999999999999999999", "-5"));
		System.out.println("4.9999999999999999999999999999999999 and 5:" + comp.compareDecimals("4.9999999999999999999999999999999999", "5"));
		
	}
	
}