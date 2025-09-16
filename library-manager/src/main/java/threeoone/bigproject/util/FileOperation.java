package threeoone.bigproject.util;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Util for file operation
 *
 * @author HUY1902
 */
@RequiredArgsConstructor
public class FileOperation {

  private static final String defaultDestPath = "src/main/resources/Document";

  /**
   * Copy from srcPath to destFolderPath (default is src/main/resources/Document).
   * Rename if necessary (default is same name as copy file)
   *
   * @param srcPath        source Path
   * @param destFolderPath destination Folder Path
   * @param rename         new name for file
   *
   * @return path to new copying file
   */
  public static String copyFile(String srcPath, String destFolderPath, String rename) {
    if (destFolderPath == null || destFolderPath.isEmpty()) {
      destFolderPath = defaultDestPath;
      System.out.println(destFolderPath);
    }
    try {
      Path sourcePath = Paths.get(srcPath);
      Path destinationFolder = Paths.get(destFolderPath);
      String fileName = sourcePath.getFileName().toString();
      String typeFile = fileName.substring(fileName.lastIndexOf(".") + 1);
      if(rename == null || rename.isEmpty()) {
        rename = fileName.substring(0, fileName.lastIndexOf("."));
      }
      // Create destination folder if it doesn't exist
      if (Files.notExists(destinationFolder)) {
        Files.createDirectories(destinationFolder);
      }
      Path destinationPath = destinationFolder.resolve(rename + "." + typeFile);
      // Copy the file
      Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
//      Path newPath = destinationFolder.resolve(rename);
//      Files.move(destinationPath, newPath, StandardCopyOption.REPLACE_EXISTING);
        return destinationPath.toString();
    } catch (IOException e) {
      Alerts.showAlertWarning("Error when copy file!", e.getMessage());
      return null;
    }
  }
}
