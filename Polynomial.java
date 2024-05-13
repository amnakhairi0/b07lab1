class Polynomial {
    double[] coefficients;

    Polynomial(){
        this.coefficients = new double[0];
    }

    Polynomial(double[] coefficients){
        this.coefficients = coefficients;
    }

    Polynomial add(Polynomial toAdd){
        Polynomial toReturn = new Polynomial();
        if(toAdd.coefficients.length >= this.coefficients.length){
            toReturn.coefficients = toAdd.coefficients;
            for(int i = 0; i < this.coefficients.length; i++){
                toReturn.coefficients[i] += this.coefficients[i];
            }
            return toReturn;
        } else{
            toReturn.coefficients = this.coefficients;
            for(int i = 0; i < toAdd.coefficients.length; i++){
                toReturn.coefficients[i] += toAdd.coefficients[i];
            }
            return toReturn;
        }
    }

    double evaluate(double x){
        double count = 0;
        for(int i = 0; i < this.coefficients.length; i++){
            count += this.coefficients[i]*(Math.pow(x,i));
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