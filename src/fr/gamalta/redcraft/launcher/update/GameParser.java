package fr.gamalta.redcraft.launcher.update;

import fr.gamalta.redcraft.launcher.utils.FileUtil;
import fr.gamalta.redcraft.launcher.utils.LauncherFile;
import fr.gamalta.redcraft.launcher.utils.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;

import static fr.gamalta.redcraft.launcher.utils.Constants.DIRECTORY;
import static fr.gamalta.redcraft.launcher.utils.Constants.DOWNLOAD_URL;

class GameParser {

    static void prepareFiles() {

        Logger.info("Lecture de fichier XML " + DOWNLOAD_URL + "downloads.xml");

        try {

            URL resourceUrl = new URL(DOWNLOAD_URL + "downloads.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(resourceUrl.openConnection().getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("Contents");

            long start = System.nanoTime();
            for (int i = 0; i < nodeLst.getLength(); i++) {
                Node node = nodeLst.item(i);
                if (node.getNodeType() == 1) {
                    Element element = (Element) node;
                    String key = element.getElementsByTagName("Key").item(0).getChildNodes().item(0).getNodeValue();
                    String etag = (element.getElementsByTagName("ETag") != null) ? element.getElementsByTagName("ETag").item(0).getChildNodes().item(0).getNodeValue() : "-";
                    long size = Long.parseLong(element.getElementsByTagName("Size").item(0).getChildNodes().item(0).getNodeValue());

                    File localFile = new File(DIRECTORY, key);
                    GameDownloader.addToVerifyIntegrity(key);
                    if (!localFile.isDirectory()) {
                        if (etag.length() > 1) {
                            etag = FileUtil.getEtag(etag);
                            if (localFile.exists()) {
                                if (localFile.isFile() && localFile.length() == size) {
                                    String localMd5 = FileUtil.getMD5(localFile);

                                    assert localMd5 != null;
                                    if (!localMd5.equals(etag) && !(DOWNLOAD_URL + key).endsWith("/")) {

                                        addFiles(DOWNLOAD_URL + key, size, localFile);

                                    }
                                } else if (!(DOWNLOAD_URL + key).endsWith("/")) {

                                    addFiles(DOWNLOAD_URL + key, size, localFile);

                                }

                            } else if (!(DOWNLOAD_URL + key).endsWith("/")) {

                                addFiles(DOWNLOAD_URL + key, size, localFile);

                            }

                        }
                    } else {

                        localFile.mkdir();
                        localFile.mkdirs();
                    }
                }
            }

            Logger.info("Temps pour comparer les fichiers: " + (System.nanoTime() - start / 1000000L) + " ms");
            
        } catch (Exception ex) {

            ex.printStackTrace();
            Logger.error("Impossible de télécharger les fichiers (" + ex + ")");
        }
    }

    private static void addFiles(String key, double size, File localFile){

        GameDownloader.needToDownload++;
        if (key.contains("jre-64") || key.contains("jre-32")) {
            if (System.getProperty("os.arch").contains("64")) {
                if (key.contains("jre-64")) {
                    GameUpdater.filesToUpdate.put(key, new LauncherFile(size, DOWNLOAD_URL + key, localFile.getAbsolutePath()));

                }
            } else if (key.contains("jre-32")) {
                GameUpdater.filesToUpdate.put(key, new LauncherFile(size, DOWNLOAD_URL + key, localFile.getAbsolutePath()));
            }

        } else {

            GameUpdater.filesToUpdate.put(key, new LauncherFile(size, DOWNLOAD_URL + key, localFile.getAbsolutePath()));
        }
    }
}
