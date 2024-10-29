import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class TestMath {
        
    private static int _comparaisonSinus(float theta){
        float difference = Mathjava.sin(theta)-(float)Math.sin(theta);
        float differenceEnUlp = difference/Mathjava.ulp(Mathjava.sin(theta));
        return Math.abs(Math.round(differenceEnUlp));
    }

    private static int _comparaisonAsin(float x) {
        float difference = Mathjava.asin(x) - (float) Math.asin(x);
        float differenceEnUlp = difference / Mathjava.ulp(Mathjava.asin(x));
        return Math.abs(Math.round(differenceEnUlp));
    }
    
    private static int _comparaisonAtan(float x) {
        float difference = Mathjava.atan(x) - (float) Math.atan(x);
        float differenceEnUlp = difference / Mathjava.ulp(Mathjava.atan(x));
        return Math.abs(Math.round(differenceEnUlp));
    }
    
    private static int _comparaisonCosinus(float x) {
        float difference = Mathjava.cos(x) - (float) Math.cos(x);
        float differenceEnUlp = difference / Mathjava.ulp(Mathjava.cos(x));
        return Math.abs(Math.round(differenceEnUlp));
    }


    public static void _rapportComparaisonFonctionTrigo(int nbEssais, float valeurMin, float valeurMax,
                                           Function<Float, Integer> fonctionComparaison, String nomFonction) {
        float pas = (valeurMax-valeurMin)/nbEssais;
        
        // Utilisation d'une Map pour stocker le nombre d'occurrences pour chaque valeur
        Map<Integer, Integer> occurrencesMap = new HashMap<>();

        for (float f = valeurMin; f < valeurMax; f += pas) {
            int valeurComparaison = fonctionComparaison.apply(f);
            int nbOccurences = occurrencesMap.getOrDefault(valeurComparaison, 0) + 1;

            // Mise à jour de la Map
            occurrencesMap.put(valeurComparaison, nbOccurences);
        }

        // Affichage des résultats
        System.out.println("Rapport de comparaison pour " + nomFonction);
        System.out.println("Fenêtre : [" + valeurMin + ", " + valeurMax + "]");
        System.out.println("Nombre de points : " + nbEssais);

        System.out.println("ULP non fiables\t\tNombre d'occurrences\t\tProportion\t\tCumul.");
        
        // Obtenir les entrées triées par clés (valeurs comparées)
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>(occurrencesMap);

        // Afficher seulement les premières valeurs
        int limit = Math.min(10, sortedMap.size());
        float pourcentage;
        float pourcentageSomme = 0;
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            int key = entry.getKey();
            pourcentage = 100*entry.getValue() / ((float) nbEssais);
            pourcentageSomme += pourcentage;
            System.out.printf("%d\t\t\t\t%d\t\t\t\t%.1f %%\t\t\t%.1f %%%n", key, entry.getValue(), pourcentage, pourcentageSomme);

            if (limit-- == 0) break;
        }
    }

    public static void _rapportComparaisonUlp(int puissanceMin, int puissanceMax) {
        assert(puissanceMin <= puissanceMax);
        System.out.println("Comparaison des valeurs ULP pour les puissances de 2 :");
        System.out.println("Puissance\tMath.ulp\tMathjava.ulp");

        for (int puissance = puissanceMin; puissance <= puissanceMax; puissance++) {
            float valeur = (float) Math.pow(2, puissance);

            float ulpMath = Math.ulp(valeur);
            float ulpMathjava = Mathjava.ulp((float) valeur);

            float diff1 = Math.abs(ulpMath - ulpMathjava);

            System.out.printf("%d\t\t%.16f\t\t%.16f%n", puissance, diff1);
        }
    }


    private static void _rapportComparaisonAsin(int nbEssais, float valeurMin, float valeurMax) {
        _rapportComparaisonFonctionTrigo(nbEssais, valeurMin, valeurMax, TestMath::_comparaisonAsin, "Asin");
    }

    private static void _rapportComparaisonAtan(int nbEssais, float valeurMin, float valeurMax) {
        _rapportComparaisonFonctionTrigo(nbEssais, valeurMin, valeurMax, TestMath::_comparaisonAtan, "Atan");
    }

    private static void _rapportComparaisonCos(int nbEssais, float valeurMin, float valeurMax) {
        _rapportComparaisonFonctionTrigo(nbEssais, valeurMin, valeurMax, TestMath::_comparaisonCosinus, "Cosinus");
    }

    private static void _rapportComparaisonSin(int nbEssais, float valeurMin, float valeurMax) {
        _rapportComparaisonFonctionTrigo(nbEssais, valeurMin, valeurMax, TestMath::_comparaisonSinus, "Sinus");
    }

    public static void main(String[] args) {
        System.out.println();
        _rapportComparaisonAsin(10000, 0, 0.5f);
        System.out.println();
        _rapportComparaisonAtan(10000, 0, 1f);
        System.out.println();
        _rapportComparaisonSin(10000, 0, Mathjava.DEMI_PI);
        System.out.println();
        _rapportComparaisonCos(10000, 0, Mathjava.DEMI_PI);

        System.out.println();
        // _rapportComparaisonUlp(-150, 127);

    }
}