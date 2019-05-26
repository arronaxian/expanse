import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        final String packageCacheDir = "c:\\temp\\";
        File[] directories = new File(packageCacheDir).listFiles(File::isDirectory);
        String apexDeploymentDir = null;
        for ( File p : directories) {
            System.out.println(p.getAbsolutePath());
            if ( p.getName().toLowerCase().startsWith("deployment-agent-cli-client") ) {
                apexDeploymentDir = p.get();
            }
        }

        System.out.println(apexDeploymentDir);
    }
}
