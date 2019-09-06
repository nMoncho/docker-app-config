package net.gdemicheli.petshop;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.io.File;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.javalin.http.UploadedFile;

public class Main {

    private static int PORT = 7000;
    private static File picsFolder = new File("upload");
    private static Logger log = LoggerFactory.getLogger(Main.class);

    // Allowed extensions
    private static String JPG_EXT = "jpg";
    private static String JPEG_EXT = "jpeg";
    private static String PNG_EXT = "png";

    public static void main(String[] args) {
        int port = parsePort(args);
        log.info("Starting Petshop at port {}", port);
        picsFolder.mkdirs();

        // Add static resources paths.
        Javalin app = Javalin.create(config -> {
          config.addStaticFiles("/static");
          config.addStaticFiles(picsFolder.getPath(), Location.EXTERNAL);
        }).start(port);

        app
          .get("/pictures", ctx -> { // Get available files
            List<File> pics = Arrays.asList(
              picsFolder.listFiles(f -> f.getName().endsWith(JPG_EXT) || f.getName().endsWith(JPEG_EXT) || f.getName().endsWith(PNG_EXT))
            );

            // Send list of files a array of strings.
            ctx.json(
              pics
                .stream()
                .map(f -> f.getName())
                .collect(Collectors.toList())
            );
          })
          .post("/upload", ctx -> { // Upload file
            ctx.uploadedFiles("picture").forEach(file -> {
              try {
                FileUtils.copyInputStreamToFile(file.getContent(), new File(picsFolder, file.getFilename()));
                ctx.redirect("/index.html");
              } catch (java.io.IOException ex) {
                log.error("Something went wrong uploading an image", ex);
                ctx.redirect("/error.html");
              }
            });
          });
    }

    private static int parsePort(String[] args) {
      if (args.length > 0) {
        try {
          return Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
          log.warn("Cannot transform {} into an int, going with default port {}", args[0], PORT);
          return PORT;
        }
      } else {
        return PORT;
      }
    }
}
