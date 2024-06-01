import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class Polynomial {
    double[] coefficients;
    int[] exponents;
    String fileLine;

    Polynomial(){
        this.coefficients = new double[0];
        this.exponents = new int[0];
    }

    Polynomial(double[] coefficients, int[] exponents){
        if(coefficients.length != exponents.length){
            this.coefficients = new double[0];
            this.exponents = new int[0];
            return;
        }
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

   Polynomial(File file) {
    //use try to automatically close file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String fileLine = br.readLine(); // Read the first line only
            //use string to initialize
            fileInit(fileLine);
        } catch (IOException e) {
            //in case of IO error
        }
    }

    void fileInit(String line) {
        //split fileline into seperate terms while keeping the terms
        String[] terms = line.split("(?<=[+-])");
        //init new polynomials fields
        coefficients = new double[terms.length];
        exponents = new int[terms.length];

        for (int i = 0; i < terms.length; i++) {
            String term = terms[i];
            int xIndex = term.indexOf('x');
            if (xIndex == -1) {
                // Constant
                coefficients[i] = Double.parseDouble(term);
                exponents[i] = 0;
            } else {
                // x term
                String coefficientPart = term.substring(0, xIndex);
                String exponentPart = term.substring(xIndex + 1);
                if (coefficientPart.equals("") || coefficientPart.equals("+")) {
                    coefficients[i] = 1.0;
                } else if (coefficientPart.equals("-")) {
                    coefficients[i] = -1.0;
                } else {
                    coefficients[i] = Double.parseDouble(coefficientPart);
                }
                if (exponentPart.equals("")) {
                    exponents[i] = 1;
                } else {
                    exponents[i] = Integer.parseInt(exponentPart);
                }
            }
        }
    }


    void saveToFile(String fileName) {
        //use try and catch to automatically close file and catch errors
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            //use string builder to build polynomial in file form
            StringBuilder sb = new StringBuilder();
            // make polynomial string
            for (int i = 0; i < coefficients.length; i++) {
                if (i > 0 && coefficients[i] > 0) {
                    sb.append("+");
                }
                if (coefficients[i] == 1.0 && exponents[i] != 0) {
                    sb.append("");
                } else if (coefficients[i] == -1.0 && exponents[i] != 0) {
                    sb.append("-");
                } else {
                    sb.append(coefficients[i]);
                }
                if (exponents[i] != 0) {
                    sb.append("x");
                    if (exponents[i] != 1) {
                        sb.append(exponents[i]);
                    }
                }
            }
            //write constructed string to given file using writer
            writer.write(sb.toString());
        } catch (IOException e) {
            //in case of IO error
        }
        
    }

    Polynomial add(Polynomial toAdd){
        //calculate the maximum size of the array
        int biggestSize = this.coefficients.length + toAdd.coefficients.length;

        //initialize the product of the addition
        double[] tempCoefficients = new double[biggestSize];
        int[] tempExponents = new int[biggestSize];

        int i = 0, j = 0, k = 0;
        //add together terms with exponents common to both polynomials
        while (i < this.coefficients.length && j < toAdd.coefficients.length) {
            if (this.exponents[i] == toAdd.exponents[j]) {
                tempCoefficients[k] = this.coefficients[i] + toAdd.coefficients[j];
                tempExponents[k] = this.exponents[i];
                i++;
                j++;
            } else if (this.exponents[i] < toAdd.exponents[j]) {
                tempCoefficients[k] = this.coefficients[i];
                tempExponents[k] = this.exponents[i];
                i++;
            } else {
                tempCoefficients[k] = toAdd.coefficients[j];
                tempExponents[k] = toAdd.exponents[j];
                j++;
            }
            k++;
        }
        //remaining terms from longer polynomial
         while (i < this.coefficients.length) {
            tempCoefficients[k] = this.coefficients[i];
            tempExponents[k] = this.exponents[i];
            i++;
            k++;
        }

        while (j < toAdd.coefficients.length) {
            tempCoefficients[k] = toAdd.coefficients[j];
            tempExponents[k] = toAdd.exponents[j];
            j++;
            k++;
        }
        //create new polynomial with new expnents and coeffs
        return new Polynomial(tempCoefficients, tempExponents);
    }

    Polynomial multiply(Polynomial toMultiply){
        //caluculate the profucts maximum length
        int biggestLength = this.coefficients.length * toMultiply.coefficients.length;
        //initialize the products coeff annd exp arrays
        double[] tempCoefficients = new double[biggestLength];
        int[] tempExponents = new int[biggestLength];

        int k = 0;
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < toMultiply.coefficients.length; j++) {
                double newCoefficient = this.coefficients[i] * toMultiply.coefficients[j];
                int newExponent = this.exponents[i] + toMultiply.exponents[j];

                //no redudant exponenets
                boolean combined = false;
                for (int l = 0; l < k; l++) {
                    if (tempExponents[l] == newExponent) {
                        tempCoefficients[l] += newCoefficient;
                        combined = true;
                        break;
                    }
                }
                if (!combined) {
                    tempCoefficients[k] = newCoefficient;
                    tempExponents[k] = newExponent;
                    k++;
                }
            }
        }
        //create new polynomial with new expnents and coeffs
        return new Polynomial(tempCoefficients, tempExponents);
    }

    double evaluate(double x){
        double count = 0;
        for(int i = 0; i < this.coefficients.length; i++){
            count += this.coefficients[i]*(Math.pow(x,this.exponents[i]));
        }
        return count;
    }

    boolean hasRoot(double x){
        if(evaluate(x) == 0){
            return true;
        }
        return false;
    }
}