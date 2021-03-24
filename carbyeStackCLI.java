import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

@Command(name = "CarbyeStackCLI", version = "Carbye Stack CLI 1.0", mixinStandardHelpOptions = true)
public class carbyeStackCLI implements Callable<Integer> {

    @Option(names = { "-f", "--fileInput" }, paramLabel = "<Input-file>", description = "Input via a TEXT file.") 
    File inputFile;

    @Option(names = "--nbLines", description = "Output number of lines instead of number of human-readable characters.")
    boolean nbLinesOption;

    @Option(names = "--nbWords", description = "Output number of words instead of number of human-readable charcaters.")
    boolean nbWordsOption;

    @Option(names = "--exportResult", paramLabel = "<Output-path>", description = "Write result into a given file.")
    String exportPath;

    @Option(names = {"-t", "--typeInput"}, paramLabel = "<text>", description = "Type input directly into the console.")
    boolean directInputOption;

    @Override
    public Integer call() throws Exception {
        int nbChars = 0;
        int nbWords = 0;
        int nbLines = 0;
        String textInput = "";

        boolean fileInputOption;
        if(inputFile != null) fileInputOption = true;
        else fileInputOption = false;

        boolean exportResultOption;
        if(exportPath != null) exportResultOption = true;
        else exportResultOption = false;

        //read from words, stdin or from file
        if (directInputOption) {
            System.out.println("In the following, please insert your text input whose length ought to be calculated: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            textInput = reader.readLine();  
            nbChars = textInput.length();
            String[] Words = textInput.split(" ");
            nbWords = Words.length;
            String[] Lines = textInput.split("\n");
            nbLines = Lines.length;
            return printResult(fileInputOption, directInputOption, exportResultOption, nbWords, nbLines, nbChars, textInput, inputFile);
        } else if (fileInputOption) {
            try {
                BufferedReader reader = null;
                FileInputStream fileStream = new FileInputStream(inputFile);
                InputStreamReader input = new InputStreamReader(fileStream);
                reader = new BufferedReader(input);
                String data;
                while ((data = reader.readLine()) != null) {
                    textInput += data;
                    nbChars += data.length();
                    String[] Words = data.split(" ");
                    nbWords += Words.length;
                    nbLines++;
                }
                textInput += "\n \t(Content of file '" + inputFile.getName() + "')"; 
                reader.close();
                return printResult(fileInputOption, directInputOption, exportResultOption, nbWords, nbLines, nbChars, textInput, inputFile);
                
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int printResult(boolean fileInputOption, boolean directInputOption, boolean exportResultOption, int nbWords, int nbLines, int nbChars, String textInput, File inputFile){
        String outputType;
        if(directInputOption) outputType = "Your input";
        else if(fileInputOption) outputType = "The file '" + inputFile.getName() + "'";
        else return -1;
        
        if(!exportResultOption){
            if(nbWordsOption) System.out.println(outputType + " has " + nbWords + " word(s). ");
            if(nbLinesOption) System.out.println(outputType + " has " + nbLines + " line(s). ");
            if(!nbLinesOption & !nbWordsOption) System.out.println(outputType + " has " + nbChars + " human-readable character(s). ");
            return 0;    
        } else {
            File outputFile = new File(exportPath);
            try {
                FileWriter myWriter = new FileWriter(outputFile);
                myWriter.write("INPUT: \n" + "\t" + textInput + "\n");
                myWriter.write("RESULT: \n");
                if(nbWordsOption) myWriter.write("\t" + outputType + " has " + nbWords + " word(s). \n");
                if(nbLinesOption) myWriter.write("\t" + outputType + " has " + nbLines + " line(s). \n");
                if(!nbLinesOption & !nbWordsOption) myWriter.write("\t" + outputType + " has " + nbChars + " human-readable character(s). \n");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
                return 0;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return -1;
            }
        }
    }
    public static void main(String... args) {
        int exitCode = new CommandLine(new carbyeStackCLI()).execute(args);
        System.exit(exitCode);
    }
}