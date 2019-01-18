package casTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallShell {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		File file = new File("../script/getCaslog.sh");
		Process process = Runtime.getRuntime().exec(file.getAbsolutePath());
		int waitFor = process.waitFor();

		// 处理调用shell后的输入流
		InputStreamReader isr = new InputStreamReader(process.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String str = null;
		StringBuffer sb = new StringBuffer();
		int i = 1;
		while ((str = br.readLine()) != null) {
			// logtime
			if ((i) % 12 == 1) {
				int first = str.indexOf("<");
				int second = str.indexOf("+");
				String time = str.substring(first + 1, second).trim();
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS").parse(time);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sb.append(date + ",");
			}
			// user
			if ((i) % 12 == 4) {
				int index = str.indexOf(":");
				String user = str.substring(index + 1).trim();
				sb.append(user + ",");
			}
			// text
			if ((i) % 12 == 6) {
				int index = str.indexOf(":");
				String opText = str.substring(index + 1).trim();
				sb.append(opText + ",");
			}
			// cip
			if ((i) % 12 == 9) {
				int index = str.indexOf(":");
				String cip = str.substring(index + 1).trim();
				sb.append(cip + ",");
			}
			// sip
			if ((i) % 12 == 10) {
				int index = str.indexOf(":");
				String sip = str.substring(index + 1).trim();
				sb.append(sip + "\n");
			}
			i++;
		}
		String result = sb.toString();
		String[] entitys = result.split("\n");
		for (String s : entitys) {
			String[] entity = s.split(",");
			for (String string : entity) {
				System.out.println(string);
			}
		}

		// 处理的执行shell过程中的错误流
		final InputStreamReader esr = new InputStreamReader(process.getErrorStream());
		final BufferedReader ebr = new BufferedReader(esr);

		// 任务执行完成返回1
		FutureTask<Integer> handleError = new FutureTask<>(new Runnable() {
			@Override
			public void run() {
				String str = null;
				try {
					while ((str = ebr.readLine()) != null) {
						System.out.println(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (esr != null) {
							esr.close();
						}
						if (ebr != null) {
							ebr.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 1);
		new Thread(handleError).start();

		// 获取fueture执行完成的返回值，会阻塞线程
		handleError.get();
	}
}
