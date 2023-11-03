package edu.hillel;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.mockito.Mockito.*;

/**
 * Unit test for simple App.
 */
public class ClientTest {
    @Test
    public void testSendFile() throws IOException {

        OutputStream outputStream = mock(OutputStream.class);

        Client client = new Client();

        String errorSendFileMessage = client.sendFile("file test.txt", outputStream);

        Assert.assertNull(errorSendFileMessage);

        verify(outputStream, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, atLeastOnce()).flush();
    }

    @Test
    public void testSendFileEmptyFileName() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);

        Client client = new Client();

        String errorSendFileMessage = client.sendFile("file ", outputStream);

        Assert.assertEquals("File name is empty!", errorSendFileMessage);

    }

    @Test
    public void testSendFileEmptyFileNotExists() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);

        Client client = new Client();

        String errorSendFileMessage = client.sendFile("file 111", outputStream);

        Assert.assertEquals("File not exists!", errorSendFileMessage);

    }

    @Test
    public void testHandleClientMessages() throws IOException {

        OutputStream outputStream = mock(OutputStream.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        Client client = new Client();

        String scannerMessage = "file test.txt";
        Scanner scanner = new Scanner(scannerMessage);
        client.handleClientMessages(printWriter, outputStream,scanner);

        verify(printWriter, atLeastOnce()).println(scannerMessage);

        verify(outputStream, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, atLeastOnce()).flush();
    }

}
