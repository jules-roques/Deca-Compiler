// Grenoble INP - Ensimag projet GL -*- mode: java -*-
// Library for class Math of Deca, coded in Deca

class Mathjava {

    static float MIN_VALUE = 0x0.000002p-126f;
    static float MAX_VALUE = 0x1.fffffep127f;
    static float INFINITY = MAX_VALUE + 0x1.000002p127f;
    static float NAN = INFINITY + 0x1.0000a0p127f;

    static float PI = 0x1.921fb6p1f;
    static float DEMI_PI = 0x1.921fb6p0f;
    static float PI_SUR_QUATRE = 0x1.921fb6p-1f; 
    static float DEUX_PI = 0x1.921fb6p2f;

    // Méthode pour calculer la factorielle d'un nombre
    public static float _factorielle(int n) {
        if (n == 0 || n == 1) {
            return 1;
        } else {
            return n * _factorielle(n - 1);
        }
    }
    public static float _factorielleParite(int n) {
        if (n == 0 || n == 1) {
            return 1f;
        } else {
            return n * _factorielle(n - 2);
        }
    }

    static int _puissanceDeMoinsUn (int n){        
        if (n % 2 == 0){
            return 1;
        } else {
            return -1;
        }
    }

    static float _puissance(float f, int n){  //méthode testée en java
        if (n == 0){
            return 1;
        }
        if (n > 0){
            return f*_puissance(f,n-1);
        }
        return _puissance(f, n+1) / f;
    }
    

    static float _sinTaylor(float theta){
        if (theta > PI_SUR_QUATRE){
            return _cosTaylor(DEMI_PI - theta);
        }
        int n = 5;
        float somme = 0;
        float theta2 = theta * theta;
        while (n >= 1){
            somme = somme + _puissanceDeMoinsUn(n) / _factorielle(2*n+1);
            somme = somme * theta2;
            n = n - 1;   
        }
        somme = somme + 1;
        somme = somme * theta;
        return somme;
    }

    static float _cosTaylor(float theta) {
        int n = 6;
        float somme = 0;
        float theta2 = theta * theta;
        while (n >= 1){
            somme = somme + _puissanceDeMoinsUn(n) / _factorielle(2*n);
            somme = somme * theta2;
            n = n - 1;   
        }
        somme = somme + 1;
        return somme;
    }

    static float sin(float theta) {
        float resultat;
        if (theta > PI) {
            while (theta > PI){
                theta = theta - DEUX_PI;
            }
        }
        if (theta < -PI) {
            while (theta < -PI){
                theta = theta + DEUX_PI;
            }
        }                           //Maintenant -PI <= theta <= PI
        if (theta > 0){
            if (theta > DEMI_PI){
                resultat = -_cosTaylor(theta - DEMI_PI);
            }
            else{
                resultat = _sinTaylor(theta);
            }
        }
        else if (theta < 0){
            if (theta < -DEMI_PI){
                resultat = -_sinTaylor(theta + PI);
            }
            else{
                resultat = -_cosTaylor(theta + DEMI_PI);
            }
        }
        else {
            resultat = 0;
        }
        if (resultat < -1){
            resultat = -1;
        }
        if (resultat > 1){
            resultat = 1;
        }
        return resultat;
    }


    static float cos(float theta) {
        float resultat;
        if (theta > PI) {
            while (theta > PI){
                theta = theta - DEUX_PI;
            }
        }
        if (theta < -PI) {
            while (theta < -PI){
                theta = theta + DEUX_PI;
            }
        }                               //Maintenant -PI <= theta <= PI
        if (theta > 0){
            if (theta > DEMI_PI){
                resultat = -_sinTaylor(theta - DEMI_PI);
            }
            else{
                resultat = _cosTaylor(theta);
            }
        }
        else if (theta < 0){
            if (theta < -DEMI_PI){
                resultat = -_cosTaylor(theta + PI);
            }
            else{
                resultat = _sinTaylor(theta + DEMI_PI);
            }
        }
        else resultat = 1;
        if (resultat < -1f){
            resultat = -1;
        }
        if (resultat > 1f){
            resultat = 1;
        }
        return resultat;
    }

    static float _asinTaylor(float x){
        int n = 17;
        float somme = 0;
        float x2 = x * x;
        while (n >= 1){
            somme = somme + _factorielleParite(2*n-1) / (_factorielleParite(2*n) * (2*n+1));
            somme = somme * x2;
            n = n - 1;   
        }
        somme = somme + 1;
        somme = somme * x;
        return somme;
    }
    static float asin(float x) {
        if(x < 0){
            x = -x;
        } 
        return _asinTaylor(x);
    }

    // Série de Taylor pour arctan(x) pour des valeurs de x proches de zéro
    public static float _arctanTaylor(float x) {
        int n = 100;
        float somme = 0;
        float x2 = x * x;
        while (n >= 1){
            somme = somme + (float)_puissanceDeMoinsUn(n) / (2 * n + 1);
            somme = somme * x2;
            n = n - 1;   
        }
        somme = somme + 1;
        somme = somme * x;
        return somme;
    }

    public static float atan(float f) {
        if (f < -1 || f > 1) {
            if (f > 1){
                return DEMI_PI - _arctanTaylor(1/f);
            }                               //Car atan(f)=signe(f)*PI/2 - atan(1/x)
            else {
                return -DEMI_PI - _arctanTaylor(1/f);
            }
        }

        return _arctanTaylor(f);
    }
            

    public static float ulp(float f) {

        float puissance_de_deux = 1;
        if (f<0) {
            f = -f;       // car ulp(f) = ulp(-f)
        }
        if (f == 0){
            return MIN_VALUE;  //peut-être pas la bonne représentation de "Float.MIN_VALUE"
        }
        if (f >= MAX_VALUE){
            if (f == MAX_VALUE){
                return _puissance(2, 104);  //104=127-23
            }
            if (f == INFINITY){
                return INFINITY;
            }
            return NAN;            
        }
        if (f < 1){
            while (f < puissance_de_deux){
                puissance_de_deux = puissance_de_deux / 2;
            }
            return puissance_de_deux * _puissance(2, -23);
        }
        while (f >= puissance_de_deux){
            puissance_de_deux = puissance_de_deux * 2;
        }
        return puissance_de_deux * _puissance(2, -22);
    }
}


// End of Deca Math library
