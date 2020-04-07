package cmd.util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketServer
{
    public static void main( String[] args ) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket( 8080 );
        Socket socket = serverSocket.accept();
        System.out.println("服务端建立连接");
        BufferedReader br;
        PrintWriter bw;
        Scanner sc = new Scanner( System.in );

        br = new BufferedReader(
                new InputStreamReader( socket.getInputStream() ) );
        bw = new PrintWriter(
                new OutputStreamWriter( socket.getOutputStream() ) );

        bw.println( "已建立连接" );
        bw.flush();
        System.out.println("服务器发送连接成功信号给客户端");
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
