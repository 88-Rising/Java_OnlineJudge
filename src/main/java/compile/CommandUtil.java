package compile;

import java.io.*;

// 借助这个类, 让 java 代码能够去执行一个具体的指令.
// 例如 javac Test.java
public class CommandUtil {
    // cmd 表示要执行的命令
    // stdoutFile 表示标准输出结果重定向到哪个文件中. 如果为 null 表示不需要重定向
    // stderrFile 表示标准错误结果重定向到哪个文件中.
    public static int run(String cmd, String stdoutFile,
                          String stderrFile) throws IOException, InterruptedException {
        // 1. 获取 Runtime 对象. Runtime 对象是一个单例的.只有这一个runtime对象，不可以通过New来创建
        Runtime runtime = Runtime.getRuntime();
        // 2. 通过 Runtime 对象中的 exec 方法来执行一个指令.  首先创建一个子进程然后进行程序替换（用户端）
        //    相当于在 命令行 中输入了 cmd 命令并执行.
        Process process = runtime.exec(cmd);
        // 3. 针对标准输出进行重定向
        if (stdoutFile != null) {
            // 进程的标准输出中的结果就可以通过这个 InputStream 获取到.
            InputStream stdoutFrom = process.getInputStream();
            OutputStream stdoutTo = new FileOutputStream(stdoutFile);
            int ch = -1;
            while ((ch = stdoutFrom.read()) != -1) {
                stdoutTo.write(ch);
            }
            stdoutFrom.close();
            stdoutTo.close();
        }
        // 4. 针对标准错误也进行重定向.
        if (stderrFile != null) {
            InputStream stderrFrom = process.getErrorStream();
            OutputStream stderrTo = new FileOutputStream(stderrFile);
            int ch = -1;
            while ((ch = stderrFrom.read()) != -1) {
                stderrTo.write(ch);
            }
            stderrFrom.close();
            stderrTo.close();
        }
        // 5. 为了确保 子进程 先执行完, 需要加上进程等待.
        //    父进程会在 waitFor 阻塞等待, 直到子进程执行结束, 再继续往下执行.
        int exitCode = process.waitFor();//当进程(父进程获取到子进程的退出码)结束之后会返回一个退出码
        return exitCode;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        run("javac", "d:/stdout.txt", "d:/stderr.txt");
    }
}