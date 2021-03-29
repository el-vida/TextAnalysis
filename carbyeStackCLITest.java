import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class carbyeStackCLITest {
    
    // Testing
    @Test
    public void helloTest() {
        carbyeStackCLI app = new carbyeStackCLI();
        CommandLine cmd = new CommandLine(app);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cmd.setOut(pw);

        // black box testing
        int exitCode = cmd.execute("--fileInput", "C:\\Users\\eu_el\\Desktop\\Bosch Coding Challenge\\BoschCodingChallenge\\src\\hello.txt");
        assertEquals(0, exitCode);

        // white box testing
        assertEquals("5", String.valueOf(app.getNbChars()));
        assertEquals("0", String.valueOf(app.getNbSpaces()));
        assertEquals("1", String.valueOf(app.getNbWords()));
        assertEquals("1", String.valueOf(app.getNbLines()));
    }

    @Test
    public void spacesTest() {
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
        carbyeStackCLI app = new carbyeStackCLI();
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
