package fr.ensimag.deca;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl44
 * @date 01/01/2024
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println();
            System.out.println(" ██████╗ ██╗     ██╗  ██╗██╗  ██╗        Aymeric BARON");
            System.out.println("██╔════╝ ██║     ██║  ██║██║  ██║        Carmel BENOIT");
            System.out.println("██║  ███╗██║     ███████║███████║        Jérôme DUVEAU");
            System.out.println("██║   ██║██║     ╚════██║╚════██║        Eliott DUMONT");
            System.out.println("╚██████╔╝███████╗     ██║     ██║        Jules ROQUES");
            System.out.println(" ╚═════╝ ╚══════╝     ╚═╝     ╚═╝                    ");
            System.out.println();
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
            System.exit(1);
        }
        
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.


            int numProcessors = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(numProcessors);

            List<Future<Boolean>> futures = new LinkedList<>();

            // Soumettre une tâche par fichier
            for (File source : options.getSourceFiles()) {
                Callable<Boolean> compileTask = () -> {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    return compiler.compile();
                };
                futures.add(executor.submit(compileTask));
            }

            // Attendre que toutes les compilations soient terminées
            for (Future<Boolean> future : futures) {
                try {
                    boolean result = future.get();
                    if (result) {
                        error = true;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace(); // Gérer les exceptions
                }
            }

            executor.shutdown();
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
