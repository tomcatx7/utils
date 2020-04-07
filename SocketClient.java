package cmd.util;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient
{
    public static void main( String[] args ) throws IOException
    {
        Socket socket = new Socket( "localhost", 8080 );

        System.out.println("开始建立连接"+socket.isConnected());
        BufferedReader br;
        PrintWriter bw;
        Scanner sc = new Scanner( System.in );

        br = new BufferedReader(
                new InputStreamReader( socket.getInputStream() ) );
        bw = new PrintWriter(
                new OutputStreamWriter( socket.getOutputStream() ) );

        while( true )
        {
            while( br.ready() )
            {
                String s = br.readLine();
                System.out.println( "收到消息\n" + s );

                String out = sc.next();
                bw.println( out );
                bw.flush();
            }
        }
    }
}
