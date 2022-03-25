package fr.gamalta.redcraft.launcher.update;

import fr.gamalta.redcraft.launcher.utils.Logger;
import fr.gamalta.redcraft.launcher.utils.OSUtil;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.gamalta.redcraft.launcher.utils.Constants.*;


class GameLaunch {

    private static String gameDir = (new File(DIRECTORY, "bin")).getAbsolutePath();
    private static String gameFile = (new File(gameDir, "1.12.2.jar")).getAbsolutePath();
    private static String nativesDir = (new File(DIRECTORY, "natives")).getAbsolutePath();
    private static String assetsDir = (new File(DIRECTORY, "assets")).getAbsolutePath();
    private static String librariesDir = (new File(DIRECTORY, "libraries")).getAbsolutePath();
    private static String minecraftArguments = "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userType ${user_type} --tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker --versionType Forge";

    static void createInstance() {

        ProcessBuilder pb = new ProcessBuilder();
        ArrayList<String> commands = new ArrayList<>();
        commands.add(getJavaDir());

        commands.add("-Xmx" + RAM + "G");

        if (OSUtil.getOS().equals(OSUtil.OperatingSystem.windows)) {

            commands.add("-XX:+UnlockExperimentalVMOptions");
            commands.add("-XX:+UseG1GC");
            commands.add("-XX:G1NewSizePercent=20");
            commands.add("-XX:G1ReservePercent=20");
            commands.add("-XX:MaxGCPauseMillis=50");
            commands.add("-XX:G1HeapRegionSize=32M");
            commands.add("-XX:-UseAdaptiveSizePolicy");
            commands.add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
            commands.add("-Dos.name=Windows 10");
            commands.add("-Dos.version=10.0");
            commands.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
            commands.add("-Dfml.ignorePatchDiscrepancies=true");
            commands.add("-Djava.library.path=" + (new File(nativesDir)).getAbsolutePath());
            commands.add("-Dminecraft.launcher.brand=Minecraft");
            commands.add("-Dminecraft.launcher.version=30");
            commands.add("-Dminecraft.client.jar=" + gameFile);

        } else if (OSUtil.getOS().equals(OSUtil.OperatingSystem.linux)) {

            commands.add("-Xdock:name=Minecraft");
            commands.add("-Xdock:icon=" + (new File((new File(assetsDir)).getAbsolutePath(), "icons/minecraft.icns")).getAbsolutePath());
            commands.add("-XX:+UnlockExperimentalVMOptions");
            commands.add("-XX:+UseG1GC");
            commands.add("-XX:G1NewSizePercent=20");
            commands.add("-XX:G1ReservePercent=20");
            commands.add("-XX:MaxGCPauseMillis=50");
            commands.add("-XX:G1HeapRegionSize=32M");
            commands.add("-XX:-UseAdaptiveSizePolicy");
            commands.add("-Dos.name=Windows 10");
            commands.add("-Dos.version=10.0");
            commands.add("-Djava.library.path=" + (new File(nativesDir)).getAbsolutePath());
            commands.add("-Dminecraft.launcher.brand=Minecraft");
            commands.add("-Dminecraft.launcher.version=30");
            commands.add("-Dminecraft.client.jar=" + gameFile);

        } else if (OSUtil.getOS().equals(OSUtil.OperatingSystem.macos)) {

            commands.add("-Xdock:name=Minecraft");
            commands.add("-Xdock:icon=" + (new File((new File(assetsDir)).getAbsolutePath(), "icons/minecraft.icns")).getAbsolutePath());
            commands.add("-XX:+UnlockExperimentalVMOptions");
            commands.add("-XX:+UseG1GC");
            commands.add("-XX:G1NewSizePercent=20");
            commands.add("-XX:G1ReservePercent=20");
            commands.add("-XX:MaxGCPauseMillis=50");
            commands.add("-XX:G1HeapRegionSize=32M");
            commands.add("-XX:-UseAdaptiveSizePolicy");
            commands.add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
            commands.add("-Dos.name=Windows 10");
            commands.add("-Dos.version=10.0");
            commands.add("-Djava.library.path=" + (new File(nativesDir)).getAbsolutePath());
            commands.add("-Dminecraft.launcher.brand=Minecraft");
            commands.add("-Dminecraft.launcher.version=30");
            commands.add("-Dminecraft.client.jar=" + gameFile);
        }

        commands.add("-cp");
        commands.add(getClasspath());
        String mainClass = "net.minecraft.launchwrapper.Launch";
        commands.add(mainClass);
        minecraftArguments = minecraftArguments + " --width ${resolution_width} --height ${resolution_height}";
        minecraftArguments = minecraftArguments.replace("${auth_player_name}", USERNAME);
        minecraftArguments = minecraftArguments.replace("${version_name}", "1.12.2");
        minecraftArguments = minecraftArguments.replace("${game_directory}", gameDir);
        minecraftArguments = minecraftArguments.replace("${assets_root}", assetsDir);
        minecraftArguments = minecraftArguments.replace("${assets_index_name}", "1.12.2");
        minecraftArguments = minecraftArguments.replace("${auth_uuid}", UUID);
        minecraftArguments = minecraftArguments.replace("${auth_access_token}", ACCESS_TOKEN);
        minecraftArguments = minecraftArguments.replace("${user_properties}", "{}");
        minecraftArguments = minecraftArguments.replace("${user_type}", "legacy");
        minecraftArguments = minecraftArguments.replace("${version_type}", "release");
        minecraftArguments = minecraftArguments.replace("${resolution_width}", 854 + "");
        minecraftArguments = minecraftArguments.replace("${resolution_height}", 480 + "");

        String[] launchArgs = minecraftArguments.split(" ");

        commands.addAll(Arrays.asList(launchArgs));
        Logger.info("Lancement du jeu...");


        if (OSUtil.getOS() == OSUtil.OperatingSystem.windows) {
            createProcess(pb, commands, 0);
        } else if (OSUtil.getOS() == OSUtil.OperatingSystem.macos) {
            createProcess(pb, commands, 1);
        } else if (OSUtil.getOS() == OSUtil.OperatingSystem.linux) {
            createProcess(pb, commands, 2);
        }
    }

    private static void createProcess(ProcessBuilder pb, ArrayList<String> commands, int os) {
        pb.directory(DIRECTORY);
        File launchFile;
        if (os == 0) {
            OSUtil.writeFile("offwin.bat", commands.toString());
            pb.command(commands);
            pb.inheritIO();
            try {
                pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Platform.exit();
                System.exit(0);
            }

        } else if (os == 1) {
            launchFile = OSUtil.writeFile("offlinux.sh", commands.toString());
            assert launchFile != null;
            launchFile.setExecutable(true);
            launchFile.setReadable(true);
            launchFile.setWritable(true);
            Process proc = null;
            try {
                proc = pb.redirectErrorStream(true).start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                assert proc != null;
                proc.waitFor();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Logger.info(commands.toString());
            pb.command(commands);
            pb.inheritIO();
            try {
                pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Platform.exit();
                System.exit(0);
            }

        } else if (os == 2) {
            launchFile = OSUtil.writeFile("offothers.sh", commands.toString());
            assert launchFile != null;
            launchFile.setExecutable(true);
            launchFile.setReadable(true);
            launchFile.setWritable(true);
            try {
                Runtime.getRuntime().exec("chmod u=rwx,g=r,o=- " + launchFile.getAbsolutePath());
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            Process proc = null;
            try {
                proc = pb.redirectErrorStream(true).start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                assert proc != null;
                proc.waitFor();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Logger.info(commands.toString());
            pb.command(commands);
            pb.inheritIO();
            try {
                pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Platform.exit();
                System.exit(0);
            }
        }
    }

    private static String getClasspath() {

        StringBuilder classpath = new StringBuilder();
        ArrayList<File> libs = OSUtil.list(new File(librariesDir));
        String separator = System.getProperty("path.separator");

        for (File lib : libs) {

            classpath.append(lib.getAbsolutePath()).append(separator);
        }

        return classpath + gameFile;
    }


    private static String getJavaDir() {

        String separator = System.getProperty("file.separator");
        String path = OSUtil.getDirectory() + separator + "bin" + separator + "jre-64" + separator + "bin" + separator;

        if (OSUtil.getOS() == OSUtil.OperatingSystem.windows) {
            if (System.getProperty("os.arch").contains("64")) {
                if ((new File(path + "javaw.exe")).isFile()) {
                    Logger.info("Used JRE(x64) " + path + "javaw.exe");
                    return path + "javaw.exe";
                }
            } else {

                path = OSUtil.getDirectory() + separator + "bin" + separator + "jre-32" + separator + "bin" + separator;
                if (OSUtil.getOS() == OSUtil.OperatingSystem.windows && (new File(path + "javaw.exe")).isFile()) {
                    Logger.info("Used JRE(x32)" + path + "javaw.exe");
                    return path + "javaw.exe";
                }
            }
        }
        Logger.info("No JRE installed in " + OSUtil.getDirectory() + "/bin/jre. Use default, it may not work correctly.");
        path = System.getProperty("java.home") + separator + "bin" + separator;
        if (OSUtil.getOS() == OSUtil.OperatingSystem.windows && (new File(path + "javaw.exe")).isFile()) {
            return path + "javaw.exe";
        }
        return path + "java";
    }
}
