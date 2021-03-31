package TextAnalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TextAnalysisTest {
    // Testing
    @Test
    public void helloTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\hello.txt");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("5", String.valueOf(app.getNbChars()));
        assertEquals("0", String.valueOf(app.getNbSpaces()));
        assertEquals("1", String.valueOf(app.getNbWords()));
        assertEquals("1", String.valueOf(app.getNbLines()));
    }

    @Test
    public void spacesTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\spaces.txt");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("0", String.valueOf(app.getNbChars()));
        assertEquals("14", String.valueOf(app.getNbSpaces()));
        assertEquals("0", String.valueOf(app.getNbWords()));
        assertEquals("1", String.valueOf(app.getNbLines()));
    }

    @Test
    public void invalidFileInputTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "INVALID DATA PATH");
        assertEquals(-1, exitCode);

        // white box testing
        assertEquals("0", String.valueOf(app.getNbChars()));
        assertEquals("0", String.valueOf(app.getNbSpaces()));
        assertEquals("0", String.valueOf(app.getNbWords()));
        assertEquals("0", String.valueOf(app.getNbLines()));
    }

    @Test
    public void wordFileTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\wordFile.docx");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("71", String.valueOf(app.getNbChars()));
        assertEquals("20", String.valueOf(app.getNbWords()));
        assertEquals("3", String.valueOf(app.getNbLines()));
    }

    @Test
    public void moreSophisticatedWordFileTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\moreSophisticatedWord.docx");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("501", String.valueOf(app.getNbChars()));
        assertEquals("95", String.valueOf(app.getNbWords()));
        assertEquals("2", String.valueOf(app.getNbLines()));
    }



    @Test
    public void pdfFileTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\pdfFile.pdf");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("71", String.valueOf(app.getNbChars()));
        assertEquals("20", String.valueOf(app.getNbWords()));
        assertEquals("3", String.valueOf(app.getNbLines()));
    }

    
    @Test
    public void moreSophisticatedPDFFileTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\moreSophisticatedPDF.pdf");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("501", String.valueOf(app.getNbChars()));
        assertEquals("95", String.valueOf(app.getNbWords()));
        assertEquals("8", String.valueOf(app.getNbLines()));
    }

    @Test
    public void moreSophisticatedTextFileTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\moreSophisticatedText.txt");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("501", String.valueOf(app.getNbChars()));
        assertEquals("95", String.valueOf(app.getNbWords()));
        assertEquals("3", String.valueOf(app.getNbLines()));
    }

    @Test
    public void emptyLinesTextTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\emptyLines.txt");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("0", String.valueOf(app.getNbChars()));
        assertEquals("0", String.valueOf(app.getNbWords()));
        assertEquals("10", String.valueOf(app.getNbLines()));
    }

    @Test
    public void lastLineEmptyTest() {
        TextAnalysis app = new TextAnalysis();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\data\\helloWithLastLineEmpty.txt");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("5", String.valueOf(app.getNbChars()));
        assertEquals("1", String.valueOf(app.getNbWords()));
        assertEquals("1", String.valueOf(app.getNbLines()));
    }
}